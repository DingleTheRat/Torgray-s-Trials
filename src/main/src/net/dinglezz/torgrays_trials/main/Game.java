package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Player;
import net.dinglezz.torgrays_trials.environment.EnvironmentManager;
import net.dinglezz.torgrays_trials.events.EventHandler;
import net.dinglezz.torgrays_trials.pathfinding.Pathfinder;
import net.dinglezz.torgrays_trials.tile.MapHandler;
import net.dinglezz.torgrays_trials.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

public class Game extends JPanel implements Runnable {
    // Screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // Word Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    public final int maxMaps = 10;

    // Full Screen
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D graphics2D;
    
    // Settings
    public boolean fullScreen;
    public boolean BRendering;
    public boolean pathFinding;

    // Debug
    public boolean debug = false;
    public boolean debugPathfinding = false;
    public boolean debugHitBoxes = false;

    // FPS
    int FPS = 60;
    public long drawStart;

    // System
    public UI ui = new UI(this);
    public Config config = new Config(this);
    public TileManager tileManager = new TileManager(this);
    public MapHandler mapHandler = new MapHandler(this);
    public Pathfinder pathFinder = new Pathfinder(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public InputHandler inputHandler = new InputHandler(this);
    public EventHandler eventHandler = new EventHandler(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public EnvironmentManager environmentManager = new EnvironmentManager(this);
    Thread gameThread;

    // Entities and Objects
    public Player player = new Player(this, inputHandler);
    public HashMap<String, HashMap<Integer, Entity>> npc = new HashMap<>();
    public HashMap<String, HashMap<Integer, Entity>> object = new HashMap<>();
    public HashMap<String, HashMap<Integer, Entity>> monster = new HashMap<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    public ArrayList<Entity> entityList = new ArrayList<>();

    public States gameState = States.STATE_TITLE;
    public String currentMap = "Main Island";
    public String gameMode;

    public Game() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(inputHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        assetSetter.setObjects();
        assetSetter.setNPCs();
        assetSetter.setMonsters();
        environmentManager.setup();
        Sound.playMusic("Tech Geek");
        gameState = States.STATE_TITLE;
        player.setDefaultPosition();

        if (fullScreen) {
            setFullScreen();
        }

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        graphics2D = (Graphics2D)tempScreen.getGraphics();
    }
    public void respawn() {
        player.setDefaultPosition();
        player.restoreHealth();
        currentMap = "Main Island";
        environmentManager.lightUpdated = true;
    }
    public void restart() {
        player.setDefaultValues();
        player.setDefaultPosition();
        player.setItems();
        assetSetter.setObjects();
        assetSetter.setNPCs();
        assetSetter.setMonsters();
        currentMap = "Main Island";

        // Darkness reset
        environmentManager.lightUpdated = true;
        environmentManager.lighting.darknessCounter = 0;
        environmentManager.lighting.darknessState = States.DARKNESS_STATE_NIGHT;
        environmentManager.lighting.nextGloom = environmentManager.lighting.chooseNextGloom();
    }
    public void setFullScreen() {
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        graphicsDevice.setFullScreenWindow(Main.window);

        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        final double drawInterval = 1_000_000_000.0 / FPS;
        long lastTime = System.nanoTime();
        long timer = 0;
        double delta = 0;

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastTime;
            lastTime = currentTime;

            delta += elapsedTime / drawInterval;
            timer += elapsedTime;

            while (delta >= 1) {
                update();
                if (BRendering && gameState != States.STATE_TITLE) {
                    drawToTempScreen();
                    drawToScreen();
                } else {
                    repaint();
                }
                delta--;
            }

            if (timer >= 1_000_000_000) {
                timer = 0;
            }
        }
    }
    public void update() {
        if (gameState == States.STATE_PLAY ||
                gameState == States.STATE_CHARACTER ||
                gameState == States.STATE_DIALOGUE ||
                gameState == States.STATE_TRADE ||
                gameState == States.STATE_MAP) {

            // Player
            if (gameState != States.STATE_TRADE) {
                player.update();
            }

            // NPCs
            npc.getOrDefault(currentMap, new HashMap<>()).values().stream()
                    .filter(Objects::nonNull)
                    .forEach(Entity::update);

            // Monsters
            monster.getOrDefault(currentMap, new HashMap<>()).forEach((key, entity) -> {
                if (entity != null) {
                    if (entity.alive && !entity.dying) {
                        entity.update();
                    } else if (!entity.alive) {
                        entity.checkDrop();
                        monster.get(currentMap).put(key, null);

                        // Respawn if all monsters are dead
                        if (monster.get(currentMap).values().stream().allMatch(Objects::isNull)) {
                            assetSetter.setMonsters();
                        }
                    }
                }
            });

            // Particles
            particleList.removeIf(particle -> particle == null || !particle.alive);
            particleList.forEach(Entity::update);

            environmentManager.update();
        }
    }

    public void drawToTempScreen() {
        // Debug
        drawStart = 0;
        if (debug) {
            drawStart = System.nanoTime();
        }

        // Title Screen
        if (gameState == States.STATE_TITLE) {
            ui.draw(graphics2D);
        } else {
            // Draw :)
            tileManager.draw(graphics2D);

            // Add entities to list
            entityList.clear(); // Clear once at the start
            if (gameState != States.STATE_GAME_OVER) {
                entityList.add(player);
            }
            npc.getOrDefault(currentMap, new HashMap<>()).values().stream().filter(Objects::nonNull).forEach(entityList::add);
            object.getOrDefault(currentMap, new HashMap<>()).values().stream().filter(Objects::nonNull).forEach(entityList::add);
            monster.getOrDefault(currentMap, new HashMap<>()).values().stream().filter(Objects::nonNull).forEach(entityList::add);
            entityList.addAll(particleList);

            // Sort and draw entities
            entityList.stream()
                    .sorted(Comparator.comparingInt(e -> e.worldY))
                    .forEach(entity -> entity.draw(graphics2D));

            // Empty Entity List
            entityList.clear();

            // More  drawing :D
            environmentManager.draw(graphics2D);
            ui.draw(graphics2D);
        }
    }

    public void drawToScreen() {
        Graphics graphics = getGraphics();
        graphics.drawImage(tempScreen,0,0,screenWidth2, screenHeight2, null);
        graphics.dispose();
    }
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;

        if (debug) {
            drawStart = System.nanoTime();
        }

        if (gameState == States.STATE_TITLE) {
            ui.draw(graphics2D);
        } else {
            tileManager.draw(graphics2D);

            // Add entities to list
            if (gameState != States.STATE_GAME_OVER) {
                entityList.add(player);
            }
            npc.getOrDefault(currentMap, new HashMap<>()).values().stream().filter(Objects::nonNull).forEach(entityList::add);
            object.getOrDefault(currentMap, new HashMap<>()).values().stream().filter(Objects::nonNull).forEach(entityList::add);
            monster.getOrDefault(currentMap, new HashMap<>()).values().stream().filter(Objects::nonNull).forEach(entityList::add);
            entityList.addAll(particleList);

            // Sort and draw entities
            entityList.stream()
                    .sorted(Comparator.comparingInt(e -> e.worldY))
                    .forEach(entity -> entity.draw(graphics2D));

            entityList.clear();

            environmentManager.draw(graphics2D);
            ui.draw(graphics2D);
        }
        graphics2D.dispose();
    }
 }
