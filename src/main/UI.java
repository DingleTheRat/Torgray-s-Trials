package main;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameEnd = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public int titleScreenState = 0; //0 = Main Screen | 1 = GameMode Screen

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String text) {
        message = text;
        messageOn = true;
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        if (gp.gameState == gp.playState) {
            // for later...
        }
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }
    }

    public void drawTitleScreen() {
        if (titleScreenState == 0) {
            // Title Text
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 90f));
            String text = "Torgray's Trials";
            int x = getCentreX(text);
            int y = gp.tileSize * 3;

            // Title Shadow
            g2.setColor(new Color(147, 37, 37));
            g2.drawString(text, x + 4, y + 4);

            // Main Title
            g2.setColor(new Color(209, 25, 25));
            g2.drawString(text, x, y);

            // Torgray Image
            x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
            y += gp.tileSize * 1.5;
            g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

            // Menu
            g2.setColor(Color.white);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));
            text = "New Game";
            x = getCentreX(text);
            y += gp.tileSize * 3.5;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gp.tileSize,  y);
            }


            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));
            text = "Load Game";
            x = getCentreX(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x - gp.tileSize,  y);
            }

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));
            text = "Quit";
            x = getCentreX(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x - gp.tileSize,  y);
            }
        } else if (titleScreenState == 1) {
            // GameMode Selection
            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(42f));

            String text = "Select a GameMode";
            int x = getCentreX(text);
            int y = gp.tileSize * 3;
            g2.drawString(text, x, y);

            text = "Easy";
            x = getCentreX(text);
            y += gp.tileSize * 3;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Medium";
            x = getCentreX(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Hard";
            x = getCentreX(text);
            y += gp.tileSize;
            g2.drawString(text, x, y);
            if (commandNum == 2) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            text = "Back";
            x = getCentreX(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);
            if (commandNum == 3) {
                g2.drawString(">", x - gp.tileSize, y);
            }
        }

    }
    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80f));
        String text = "PAUSED";
        int x = getCentreX(text);
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }
    public void drawDialogueScreen() {
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2 + (gp.tileSize * 7);
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28f));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialogue.split("/n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }
    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0,0,0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255,255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10 ,25, 25);
    }

    public int getCentreX(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }
}
