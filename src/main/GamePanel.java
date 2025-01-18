package main;

import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable{

    // Screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // Word Settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // FPS
    int FPS = 60;

    // System
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound sound = new Sound();
    EnvironmentManager environmentM = new EnvironmentManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter assetS = new AssetSetter(this);
    public UI ui = new UI(this);
    public  EventHandler eHandler = new EventHandler(this);
    Thread gameThread;

    // Entities and Objects
    public Player player = new Player(this, keyH);
    public Entity[] npc = new Entity[10];
    public Entity[] obj = new Entity[10];
    public Entity[] mob = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();

    // Game States
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public int characterState = 4;

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
        environmentM.setup();
        playMusic(5);
        gameState = titleState;
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
        if (gameState == playState || gameState == characterState || gameState == dialogueState) {
            player.update();

            // NPCs
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
            // Mobs
            for (int i = 0; i < mob.length; i++) {
                if (mob[i] != null) {
                    if (mob[i].alive && !mob[i].dying) {
                        mob[i].update();
                    }
                    if (!mob[i].alive) {
                        mob[i] = null;
                    }
                }
            }
        }
        if (gameState == pauseState) {
            // NOTHIN!
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // Debug
        long drawStart = 0;
        if (keyH.debug) {
            drawStart = System.nanoTime();
        }

        // Title Screen
        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            // Draw :)
            tileM.draw(g2);

            // Add entities to list
            entityList.add(player);
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }
            for (int i = 0; i < mob.length; i++) {
                if (mob[i] != null) {
                    entityList.add(mob[i]);
                }
            }

            // Sort
            entityList.sort((e1, e2) -> {
                int result = Integer.compare(e1.worldY, e2.worldY);
                return result;
            });

            // Draw Entities
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            // Empty Entity List
            entityList.clear();

            // More  drawing :D
            environmentM.draw(g2);
            ui.draw(g2);
        }

        // Debug
        if (keyH.debug) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            // Player Position
            g2.drawString("World X: " + player.worldX, x, y); y += lineHeight;
            g2.drawString("World Y: " + player.worldY, x, y);  y += lineHeight;
            g2.drawString("Col: " + (player.worldX + player.solidArea.x) / tileSize, x, y);  y += lineHeight;
            g2.drawString("Row: " + (player.worldY + player.solidArea.y) / tileSize, x, y); y += lineHeight;

            g2.drawString("Draw Time: " + passed, x ,y); y += lineHeight;
            g2.drawString("Invincibility: " + player.invincibleCounter, x, y);
        }

        // Toxic Waste
        g2.dispose();
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
