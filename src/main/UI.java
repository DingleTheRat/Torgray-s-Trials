package main;

import entity.Entity;
import object.OBJ_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {
    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;
    BufferedImage heart, half_heart, lost_heart;
    public boolean interactable = false;
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameEnd = false;
    public String currentDialogue = "";
    public int commandNum = 0;
    public States titleScreenState = States.TITLE_STATE_MAIN;
    public int slotCol = 0;
    public int slotRow = 0;

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

        // Make a HUD object
        Entity obj_heart = new OBJ_Heart(gp);
        heart = obj_heart.image;
        half_heart = obj_heart.image2;
        lost_heart = obj_heart.image3;
    }

    public void addMessage(String message) {
        messages.add(message);
        messageCounter.add(0);
    }
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        if (gp.gameState == States.STATE_TILE) {
            drawTitleScreen();
        }
        if (gp.gameState == States.STATE_PLAY) {
            drawPlayerHealth();
            drawMessage();
            if (interactable) {
                drawInteractScreen();
            }
        }
        if (gp.gameState == States.STATE_PAUSE) {
            drawPauseScreen();
            drawPlayerHealth();
        }
        if (gp.gameState == States.STATE_DIALOGUE) {
            drawDialogueScreen();
            drawPlayerHealth();
        }
        if (gp.gameState == States.STATE_CHARACTER) {
            drawCharacterScreen();
            drawInventory();
        }
    }

    public void drawPlayerHealth() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        // Draw max health
        while (i < gp.player.maxHealth / 2) {
            g2.drawImage(lost_heart, x, y, null);
            i++;
            x += gp.tileSize;
        }

        // Reset
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        // Draw current health
        while (i < gp.player.health) {
            g2.drawImage(half_heart, x, y, null);
            i++;
            if (i < gp.player.health) {
                g2.drawImage(heart, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

    }
    public void drawMessage() {
        int messageX = gp.tileSize / 2;
        int messageY = gp.tileSize * 12 - gp.tileSize / 2;
        g2.setFont(g2.getFont().deriveFont(23f));

        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i) != null) {
                g2.setColor(Color.white);
                g2.drawString(messages.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1; // Counter ++
                messageCounter.set(i, counter); // Set counter = array
                messageY -= 30;

                if (messageCounter.get(i) > 120) {
                    messages.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }
    public void drawTitleScreen() {
        if (titleScreenState == States.TITLE_STATE_MAIN) {
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
        } else if (titleScreenState == States.TITLE_STATE_MODES) {
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

        drawSubWindow(x - 10, y - 75, gp.tileSize * 6 + 32, gp.tileSize * 2);
        g2.drawString(text, x, y);
    }
    public void drawInteractScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 35f));
        String text = "Press E to interact";
        int x = gp.screenWidth / 4 + 20;
        int y = gp.tileSize / 2 + (gp.tileSize * 8);

        int width = gp.tileSize * 7;
        int height = gp.tileSize * 2 - 13;
        drawSubWindow(x, y, width, height);
        y += 52;
        x += 16;
        g2.drawString(text, x, y);
    }
    public void drawDialogueScreen() {
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2 + (gp.tileSize * 7);
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28f));
        x += gp.tileSize / 2;
        y += gp.tileSize;

        for (String line : currentDialogue.split("/n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }
    public void drawCharacterScreen() {
        // Make a frame
        final int frameX = gp.tileSize / 2;
        final int frameY = gp.tileSize / 2;
        final int frameWidth = gp.tileSize * 6;
        final int frameHeight = gp.tileSize * 11;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(30f));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;

        // Titles
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("--------", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defence", textX, textY);
        textY += lineHeight;
        g2.drawString("--------", textX, textY);
        textY += lineHeight;
        g2.drawString("Health", textX, textY);
        textY += lineHeight;
        g2.drawString("Coins", textX, textY);
        textY += lineHeight;
        g2.drawString("--------", textX, textY);
        textY += lineHeight;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight;
        g2.drawString("Shield", textX, textY);

        // Values
        int tailX = (frameX + frameWidth) - 30;
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = alignXToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = alignXToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = alignXToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight * 2;

        value = String.valueOf(gp.player.strength);
        textX = alignXToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = alignXToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = alignXToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defence);
        textX = alignXToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight * 2;

        value = String.valueOf(gp.player.health + "/" + gp.player.maxHealth);
        textX = alignXToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coins);
        textX = alignXToRight(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight * 2 - (lineHeight / 4);

       g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY - 37, null);
       textY += gp.tileSize;
       g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY - 37, null);
    }
    public void drawInventory() {
        // Frame
        int frameX = gp.tileSize / 2 + gp.tileSize * 9;
        int frameY = gp.tileSize / 2;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 6;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Slots
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        // Draw Items
        for (int i = 0; i < gp.player.inventory.size(); i++) {
            // Equip Cursor
            if (gp.player.inventory.get(i) == gp.player.currentWeapon || gp.player.inventory.get(i) == gp.player.currentShield) {
                g2.setColor(new Color(240, 190, 90));
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }

            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += slotSize;

            if (i == 4 || i == 9 || i == 14 || i == 19) {
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // Cursor
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // Draw Cursor
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // Description Frame
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 4 - (gp.tileSize / 2);

        // Description Text
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;

        int itemIndex = getItemIndex();
        if (itemIndex < gp.player.inventory.size()) {
            // Window + Title
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            g2.setFont(g2.getFont().deriveFont(30f));
            g2.drawString(gp.player.inventory.get(itemIndex).name, textX, textY);
            textY += 10;

            // Description
            g2.setFont(g2.getFont().deriveFont(20f));
            for (String line : gp.player.inventory.get(itemIndex).description.split("/n")) {
                g2.drawString(line, textX, textY);
                textY += 30;
            }
        }
    }


    public int getItemIndex() {
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
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

    }    public int alignXToRight(String text, int tailX) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}
