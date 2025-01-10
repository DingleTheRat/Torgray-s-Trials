package main;

import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

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
    Thread gameThread;

    // Entities and Objects
    public Player player = new Player(this, keyH);
    public Entity[] npc = new Entity[10];
    public SuperObject[] obj = new SuperObject[10];

    // Game States
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;

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
        if (gameState == playState) {
            player.update();

            // NPCs
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
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
            // Tiles and Objects
            tileM.draw(g2);
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].draw(g2, this);
                }
            }

            // NPCs
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].draw(g2);
                }
            }

            // Other Drawing
            player.draw(g2);
            environmentM.draw(g2);
            ui.draw(g2);
        }

        // Debug
        if (keyH.debug) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, 10 ,400);
            System.out.println("Draw Time: " + passed);
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
