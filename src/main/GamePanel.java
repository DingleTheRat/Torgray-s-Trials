package main;

import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import events.EventHandler;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class GamePanel extends JPanel implements Runnable {

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

    // Full Screen
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D graphics2D;
    public boolean fullScreen = false;

    // FPS
    int FPS = 60;

    // System
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound sound = new Sound();
    EnvironmentManager eManager = new EnvironmentManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter assetS = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    public States gameState;
    Thread gameThread;

    // Entities and Objects
    public Player player = new Player(this, keyH);
    public HashMap<Integer, Entity> npc = new HashMap<>();
    public HashMap<Integer, Entity> obj = new HashMap<>();
    public HashMap<Integer, Entity> mob = new HashMap<>();
    public ArrayList<Entity> particleList = new ArrayList<>();
    public ArrayList<Entity> entityList = new ArrayList<>();

    // Game States;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        assetS.setObject();
        assetS.setNPC();
        assetS.setMonster();
        eManager.setup();
        playMusic(5);
        gameState = States.STATE_TILE;

        //setFullScreen();

        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        graphics2D = (Graphics2D)tempScreen.getGraphics();
    }
    public void setFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(Main.window);

        screenWidth2 = Main.window.getWidth();
        screenHeight2 = Main.window.getHeight();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
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
                repaint();
//                drawToTempScreen();
//                drawToScreen();
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
        if (gameState == States.STATE_PLAY || gameState == States.STATE_CHARACTER || gameState == States.STATE_DIALOGUE) {
            player.update();

            // NPCs
            for (int i = 0; i < npc.size(); i++) {
                if (npc.get(i) != null) {
                    npc.get(i).update();
                }
            }
            // Mobs
            for (int i = 0; i < mob.size(); i++) {
                if (mob.get(i) != null) {
                    if (mob.get(i).alive && !mob.get(i).dying) {
                        mob.get(i).update();
                    }
                    if (!mob.get(i).alive) {
                        mob.get(i).checkDrop();
                        mob.put(i, null);
                    }
                }
            }
            // Particles
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    if (particleList.get(i).alive) {
                        particleList.get(i).update();
                    }
                    if (!particleList.get(i).alive) {
                        particleList.remove(i);
                    }
                }
            }


            eManager.update();
        }
        if (gameState == States.STATE_PAUSE) {
            // NOTHIN!
        }
    }

    public void drawToTempScreen() {
        // Debug
        long drawStart = 0;
        if (keyH.debug) {
            drawStart = System.nanoTime();
        }

        // Title Screen
        if (gameState == States.STATE_TILE) {
            ui.draw(graphics2D);
        } else {
            // Draw :)
            tileM.draw(graphics2D);

            // Add entities to list
            entityList.add(player);
            for (int i = 0; i < npc.size(); i++) {
                if (npc.get(i) != null) {
                    entityList.add(npc.get(i));
                }
            }
            for (int i = 0; i < obj.size(); i++) {
                if (obj.get(i) != null) {
                    entityList.add(obj.get(i));
                }
            }
            for (int i = 0; i < mob.size(); i++) {
                if (mob.get(i) != null) {
                    entityList.add(mob.get(i));
                }
            }
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    entityList.add(particleList.get(i));
                }
            }

            // Sort
            entityList.sort((e1, e2) -> {
                int result = Integer.compare(e1.worldY, e2.worldY);
                return result;
            });

            // Draw Entities
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(graphics2D);
            }
            // Empty Entity List
            entityList.clear();

            // More  drawing :D
            eManager.draw(graphics2D);
            ui.draw(graphics2D);
        }

        // Debug
        if (keyH.debug) {
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
        Graphics g = getGraphics();
        g.drawImage(tempScreen,0,0,screenWidth2, screenHeight2, null);
        g.dispose();;
    }
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;

        // Debug
        long drawStart = 0;
        if (keyH.debug) {
            drawStart = System.nanoTime();
        }

        // Title Screen
        if (gameState == States.STATE_TILE) {
            ui.draw(graphics2D);
        } else {
            // Draw :)
            tileM.draw(graphics2D);

            // Add entities to list
            entityList.add(player);
            for (int i = 0; i < npc.size(); i++) {
                if (npc.get(i) != null) {
                    entityList.add(npc.get(i));
                }
            }
            for (int i = 0; i < obj.size(); i++) {
                if (obj.get(i) != null) {
                    entityList.add(obj.get(i));
                }
            }
            for (int i = 0; i < mob.size(); i++) {
                if (mob.get(i) != null) {
                    entityList.add(mob.get(i));
                }
            }
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    entityList.add(particleList.get(i));
                }
            }

            // Sort
            entityList.sort((e1, e2) -> {
                int result = Integer.compare(e1.worldY, e2.worldY);
                return result;
            });

            // Draw Entities
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(graphics2D);
            }
            // Empty Entity List
            entityList.clear();

            // More  drawing :D
            eManager.draw(graphics2D);
            ui.draw(graphics2D);
        }

        // Debug
        if (keyH.debug) {
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
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }
    public void stopMusic() {
        music.stop();
    }
    public void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }
 }
