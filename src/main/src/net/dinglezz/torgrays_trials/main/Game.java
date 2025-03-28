package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.Player;
import net.dinglezz.torgrays_trials.environment.EnvironmentManager;
import net.dinglezz.torgrays_trials.events.EventHandler;
import net.dinglezz.torgrays_trials.tile.TileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Game extends JPanel implements Runnable {
    public static final Logger LOGGER = LoggerFactory.getLogger("Torgray's Trials");

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
    public boolean fullScreen = false;
    public boolean BRendering = false;

    // FPS
    int FPS = 60;

    // System
    public TileManager tileManager = new TileManager(this);
    public InputHandler inputHandler = new InputHandler(this);
    Sound music = new Sound();
    Sound sound = new Sound();
    public EnvironmentManager environmentManager = new EnvironmentManager(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eventHandler = new EventHandler(this);
    public Config config = new Config(this);
    Thread gameThread;

    // Entities and Objects
    public Player player = new Player(this, inputHandler);
    public HashMap<String, HashMap<Integer, Entity>> npc = new HashMap<>();
    public HashMap<String, HashMap<Integer, Entity>> obj = new HashMap<>();
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
        playMusic("Tech Geek");
        gameState = States.STATE_TITLE;

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
        player.lightUpdated = true;
    }
    public void restart() {
        player.setDefaultValues();
        player.setItems();
        assetSetter.setObjects();
        assetSetter.setNPCs();
        assetSetter.setMonsters();
        currentMap = "Main Island";
        player.lightUpdated = true;
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
        double drawInterval = (double) 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                if (BRendering && gameState != States.STATE_TITLE) {
                    drawToTempScreen();
                    drawToScreen();
                } else {
                    repaint();
                }
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void update() {
        if (gameState == States.STATE_PLAY || gameState == States.STATE_CHARACTER || gameState == States.STATE_DIALOGUE || gameState == States.STATE_TRANSITION) {
            player.update();

            // NPCs
            for (int i = 0; i < npc.get(currentMap).size(); i++) {
                if (npc.get(currentMap).get(i) != null) {
                    npc.get(currentMap).get(i).update();
                }
            }
            // Monsters
            for (int i = 0; i < monster.get(currentMap).size(); i++) {
                if (monster.get(currentMap).get(i) != null) {
                    if (monster.get(currentMap).get(i).alive && !monster.get(currentMap).get(i).dying) {
                        monster.get(currentMap).get(i).update();
                    }
                    if (!monster.get(currentMap).get(i).alive) {
                        monster.get(currentMap).get(i).checkDrop();
                        monster.get(currentMap).put(i, null);

                        // Respawn if all monsters are dead
                        if (monster.get(currentMap).values().stream().allMatch(Objects::isNull)) {
                            assetSetter.setMonsters();
                        }
                    }
                }
            }
            // Particles
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    if (particleList.get(i).alive) {
                        particleList.get(i).update();
                    } else {
                        particleList.remove(i);
                    }
                }
            }

            environmentManager.update();
        }
    }

    public void drawToTempScreen() {
        // Debug
        long drawStart = 0;
        if (inputHandler.debug) {
            drawStart = System.nanoTime();
        }

        // Title Screen
        if (gameState == States.STATE_TITLE) {
            ui.draw(graphics2D);
        } else {
            // Draw :)
            tileManager.draw(graphics2D);

            // Add entities to list
            if (gameState != States.STATE_GAME_OVER) {
                entityList.add(player);
            }
            for (int i = 0; i < npc.get(currentMap).size(); i++) {
                if (npc.get(currentMap).get(i) != null) {
                    entityList.add(npc.get(currentMap).get(i));
                }
            }
            for (int i = 0; i < obj.get(currentMap).size(); i++) {
                if (obj.get(currentMap).get(i) != null) {
                    entityList.add(obj.get(currentMap).get(i));
                }
            }
            for (int i = 0; i < monster.get(currentMap).size(); i++) {
                if (monster.get(currentMap).get(i) != null) {
                    entityList.add(monster.get(currentMap).get(i));
                }
            }
            for (Entity value : particleList) {
                if (value != null) {
                    entityList.add(value);
                }
            }

            // Sort
            entityList.sort((e1, e2) -> {
                int result = Integer.compare(e1.worldY, e2.worldY);
                return result;
            });

            // Draw Entities
            for (Entity entity : entityList) {
                entity.draw(graphics2D);
            }
            // Empty Entity List
            entityList.clear();

            // More  drawing :D
            environmentManager.draw(graphics2D);
            ui.draw(graphics2D);
        }

        // Debug
        if (inputHandler.debug) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            graphics2D.setFont(new Font("Arial", Font.PLAIN, 20));
            graphics2D.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            // Player Position
            graphics2D.drawString("World X: " + player.worldX, x, y); y += lineHeight;
            graphics2D.drawString("World Y: " + player.worldY, x, y);  y += lineHeight;
            graphics2D.drawString("Col: " + (player.worldX + player.solidArea.x) / tileSize, x, y);  y += lineHeight;
            graphics2D.drawString("Row: " + (player.worldY + player.solidArea.y) / tileSize, x, y); y += lineHeight;

            graphics2D.drawString("Draw Time: " + passed, x ,y); y += lineHeight;
            graphics2D.drawString("Invincibility: " + player.invincibleCounter, x, y);
        }
    }

    public void drawToScreen() {
        Graphics graphics = getGraphics();
        graphics.drawImage(tempScreen,0,0,screenWidth2, screenHeight2, null);
        graphics.dispose();
    }
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;

        // Debug
        long drawStart = 0;
        if (inputHandler.debug) {
            drawStart = System.nanoTime();
        }

        // Title Screen
        if (gameState == States.STATE_TITLE) {
            ui.draw(graphics2D);
        } else {
            // Draw :)
            tileManager.draw(graphics2D);

            // Add entities to list
            if (gameState != States.STATE_GAME_OVER) {
                entityList.add(player);
            }
            for (int i = 0; i < npc.get(currentMap).size(); i++) {
                if (npc.get(currentMap).get(i) != null) {
                    entityList.add(npc.get(currentMap).get(i));
                }
            }
            for (int i = 0; i < obj.get(currentMap).size(); i++) {
                if (obj.get(currentMap).get(i) != null) {
                    entityList.add(obj.get(currentMap).get(i));
                }
            }
            for (int i = 0; i < monster.get(currentMap).size(); i++) {
                if (monster.get(currentMap).get(i) != null) {
                    entityList.add(monster.get(currentMap).get(i));
                }
            }
            for (Entity value : particleList) {
                if (value != null) {
                    entityList.add(value);
                }
            }

            // Sort
            entityList.sort((e1, e2) -> {
                int result = Integer.compare(e1.worldY, e2.worldY);
                return result;
            });

            // Draw Entities
            for (Entity entity : entityList) {
                entity.draw(graphics2D);
            }
            // Empty Entity List
            entityList.clear();

            // More  drawing :D
            environmentManager.draw(graphics2D);
            ui.draw(graphics2D);
        }

        // Debug
        if (inputHandler.debug) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            graphics2D.setFont(new Font("Arial", Font.PLAIN, 20));
            graphics2D.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            // Player Position
            graphics2D.drawString("World X: " + player.worldX, x, y); y += lineHeight;
            graphics2D.drawString("World Y: " + player.worldY, x, y);  y += lineHeight;
            graphics2D.drawString("Col: " + (player.worldX + player.solidArea.x) / tileSize, x, y);  y += lineHeight;
            graphics2D.drawString("Row: " + (player.worldY + player.solidArea.y) / tileSize, x, y); y += lineHeight;

            graphics2D.drawString("Draw Time: " + passed, x ,y); y += lineHeight;
            graphics2D.drawString("Invincibility: " + player.invincibleCounter, x, y);
        }

        // Toxic Waste
        graphics2D.dispose();
    }

    // Sounds
    public void playMusic(String songName) {
        music.getFile(songName);
        music.play();
        music.loop();
    }
    public void stopMusic() {
        music.stop();
    }
    public void playSound(String soundName) {
        sound.getFile(soundName);
        sound.play();
    }
 }
