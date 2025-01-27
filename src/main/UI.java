package main;

import entity.Entity;
import object.OBJ_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class UI {
    GamePanel gamePanel;
    Graphics2D graphics2D;
    Font maruMonica;
    BufferedImage heart, half_heart, lost_heart;
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public String currentDialogue = "";
    public int commandNum = 0;
    public States titleScreenState = States.TITLE_STATE_MAIN;
    public States subState = States.PAUSE_STATE_MAIN;
    public int slotCol = 0;
    public int slotRow = 0;
    public String actionMethod;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Make a HUD object
        Entity obj_heart = new OBJ_Heart(gamePanel);
        heart = obj_heart.image;
        half_heart = obj_heart.image2;
        lost_heart = obj_heart.image3;
    }

    public void addMessage(String message) {
        messages.add(message);
        messageCounter.add(0);
    }
    public void draw(Graphics2D g2) {
        this.graphics2D = g2;
        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        if (gamePanel.gameState == States.STATE_TILE) {
            drawTitleScreen();
        }
        if (gamePanel.gameState == States.STATE_PLAY) {
            drawPlayerHealth();
            drawMessage();
        }
        if (gamePanel.gameState == States.STATE_PAUSE) {
            drawPauseScreen();
        }
        if (gamePanel.gameState == States.STATE_DIALOGUE) {
            drawDialogueScreen();
            drawPlayerHealth();
        }
        if (gamePanel.gameState == States.STATE_CHARACTER) {
            drawCharacterScreen();
            drawInventory();
        }
    }

    public void drawPlayerHealth() {
        int x = gamePanel.tileSize / 2;
        int y = gamePanel.tileSize / 2;
        int i = 0;

        // Draw max health
        while (i < gamePanel.player.maxHealth / 2) {
            graphics2D.drawImage(lost_heart, x, y, null);
            i++;
            x += gamePanel.tileSize;
        }

        // Reset
        x = gamePanel.tileSize / 2;
        y = gamePanel.tileSize / 2;
        i = 0;

        // Draw current health
        while (i < gamePanel.player.health) {
            graphics2D.drawImage(half_heart, x, y, null);
            i++;
            if (i < gamePanel.player.health) {
                graphics2D.drawImage(heart, x, y, null);
            }
            i++;
            x += gamePanel.tileSize;
        }

    }
    public void drawMessage() {
        int messageX = gamePanel.tileSize / 2;
        int messageY = gamePanel.tileSize * 12 - gamePanel.tileSize / 2;
        graphics2D.setFont(graphics2D.getFont().deriveFont(23f));

        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i) != null) {
                graphics2D.setColor(Color.white);
                graphics2D.drawString(messages.get(i), messageX, messageY);

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
            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 90f));
            String text = "Torgray's Trials";
            int x = getCentreX(text);
            int y = gamePanel.tileSize * 3;

            // Title Shadow
            graphics2D.setColor(new Color(147, 37, 37));
            graphics2D.drawString(text, x + 4, y + 4);

            // Main Title
            graphics2D.setColor(new Color(209, 25, 25));
            graphics2D.drawString(text, x, y);

            // Torgray Image
            x = gamePanel.screenWidth / 2 - (gamePanel.tileSize * 2) / 2;
            y += gamePanel.tileSize * 1.5;
            graphics2D.drawImage(gamePanel.player.down1, x, y, gamePanel.tileSize * 2, gamePanel.tileSize * 2, null);

            // Menu
            graphics2D.setColor(Color.white);

            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48f));
            text = "New Game";
            x = getCentreX(text);
            y += gamePanel.tileSize * 3.5;
            graphics2D.drawString(text, x, y);
            if (commandNum == 0) {
                graphics2D.drawString(">", x - gamePanel.tileSize,  y);
            }


            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48f));
            text = "Load Game";
            x = getCentreX(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNum == 1) {
                graphics2D.drawString(">", x - gamePanel.tileSize,  y);
            }

            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48f));
            text = "Quit";
            x = getCentreX(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNum == 2) {
                graphics2D.drawString(">", x - gamePanel.tileSize,  y);
            }
        } else if (titleScreenState == States.TITLE_STATE_MODES) {
            // GameMode Selection
            graphics2D.setColor(Color.white);
            graphics2D.setFont(graphics2D.getFont().deriveFont(42f));

            String text = "Select a GameMode";
            int x = getCentreX(text);
            int y = gamePanel.tileSize * 3;
            graphics2D.drawString(text, x, y);

            text = "Easy";
            x = getCentreX(text);
            y += gamePanel.tileSize * 3;
            graphics2D.drawString(text, x, y);
            if (commandNum == 0) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Medium";
            x = getCentreX(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNum == 1) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Hard";
            x = getCentreX(text);
            y += gamePanel.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNum == 2) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "Back";
            y += gamePanel.tileSize * 2;
            graphics2D.drawString(text, x, y);
            if (commandNum == 3) {
                graphics2D.drawString(">", x - gamePanel.tileSize, y);
            }
        }

    }
    public void drawPauseScreen() {
        graphics2D.setColor(Color.white);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48f));

        // Sub-Window
        int frameX = gamePanel.tileSize * 6;
        int frameY = gamePanel.tileSize * 3;
        int frameWidth = gamePanel.tileSize * 8;
        int frameHeight = gamePanel.tileSize * 2;

        if (subState == States.PAUSE_SETTINGS_MAIN || subState == States.PAUSE_CONTROLS) {
            frameY = gamePanel.tileSize;
            frameHeight = gamePanel.tileSize * 10;
        }
        else if (subState == States.PAUSE_SETTINGS_NOTIFICATION || subState == States.PAUSE_SETTINGS_CONFIRM) {
            frameY = gamePanel.tileSize * 3;
            frameHeight = gamePanel.tileSize * 6;
        }
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case States.PAUSE_STATE_MAIN:
                // Title
                String text = "Game Paused";
                int x = getCentreX(text);
                int y = frameY + gamePanel.tileSize + (gamePanel.tileSize / 3);
                graphics2D.drawString(text, x, y);

                // Options Frame
                x = frameX + gamePanel.tileSize;
                y = frameY + (gamePanel.tileSize * 2) + (gamePanel.tileSize / 4);
                drawSubWindow(x, y, frameWidth - (gamePanel.tileSize * 2), frameHeight + gamePanel.tileSize + (gamePanel.tileSize / 2));

                // Settings
                graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32f));
                y += gamePanel.tileSize;
                x += gamePanel.tileSize + (gamePanel.tileSize / 2);
                graphics2D.drawString("Settings", x, y);
                if (commandNum == 0) {
                    graphics2D.drawString(">", x - gamePanel.tileSize, y);

                    if (gamePanel.keyHandler.spacePressed) {
                        subState = States.PAUSE_SETTINGS_MAIN;
                        commandNum = 0;
                    }
                }

                // End Game
                y += gamePanel.tileSize;
                graphics2D.drawString("End Game", x, y);
                if (commandNum == 1) {
                    graphics2D.drawString(">", x - gamePanel.tileSize, y);

                    if (gamePanel.keyHandler.spacePressed) {
                        commandNum = 1;
                        subState = States.PAUSE_SETTINGS_CONFIRM;
                        currentDialogue = "Are you sure you wanna /nend this game? Your data /nwon't be saved.";
                        actionMethod = "yesGameEnd";
                    }
                }

                // Resume
                y += gamePanel.tileSize;
                graphics2D.drawString("Resume", x, y);
                if (commandNum == 2) {
                    graphics2D.drawString(">", x - gamePanel.tileSize, y);

                    if (gamePanel.keyHandler.spacePressed) {
                        gamePanel.gameState = States.STATE_PLAY;
                        commandNum = 0;
                    }
                }
                break;
            case States.PAUSE_SETTINGS_MAIN: settingsMain(frameX, frameY); break;
            case States.PAUSE_SETTINGS_NOTIFICATION: settingsNotification(frameX, frameY); break;
            case States.PAUSE_SETTINGS_CONFIRM: settingsConfirm(frameX, frameY); break;
            case States.PAUSE_CONTROLS: controlsPanel(frameX, frameY); break;
        }

        gamePanel.keyHandler.spacePressed = false;
    }
    public void settingsMain(int frameX, int frameY) {
        int textX;
        int textY;

        // Title
        String text = "Settings";
        textX = getCentreX(text);
        textY = frameY + gamePanel.tileSize + (gamePanel.tileSize / 4);
        graphics2D.drawString(text, textX, textY);

        // Fullscreen
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32f));
        textX = frameX + gamePanel.tileSize;
        textY += gamePanel.tileSize * 2;
        graphics2D.drawString("Full Screen", textX, textY);
        if (commandNum == 0) {
            graphics2D.drawString(">", textX - 30, textY);

            if (gamePanel.keyHandler.spacePressed) {
                if (!gamePanel.fullScreen && !gamePanel.BRendering) {
                    commandNum = 1;
                    subState = States.PAUSE_SETTINGS_CONFIRM;
                    currentDialogue = "Are you sure you wanna /nenter full screen? It won't /nscale without BRendering.";
                    actionMethod = "yesFullScreen";
                } else {
                    yesFullScreen();
                }
            }
        }
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32f));
        textX = frameX + gamePanel.tileSize;
        textY += gamePanel.tileSize;
        graphics2D.drawString("BRendering", textX, textY);
        if (commandNum == 1) {
            graphics2D.drawString(">", textX - 30, textY);

            if (gamePanel.keyHandler.spacePressed) {
                if (!gamePanel.BRendering) {
                    commandNum = 1;
                    subState = States.PAUSE_SETTINGS_CONFIRM;
                    currentDialogue = "BRendering is a highly /nexperimental mode that /ncan break the game if on";
                    actionMethod = "yesBRendering";
                } else {
                    yesBRendering();
                }
            }
        }

        // Music
        textY += gamePanel.tileSize;
        graphics2D.drawString("Music", textX, textY);
        if (commandNum == 2) {
            graphics2D.drawString(">", textX - 30, textY);
        }


        // Sound Effects
        textY += gamePanel.tileSize;
        graphics2D.drawString("Sounds", textX, textY);
        if (commandNum == 3) {
            graphics2D.drawString(">", textX - 30, textY);
        }


        // Controls
        textY += gamePanel.tileSize;
        graphics2D.drawString("Controls", textX, textY);
        if (commandNum == 4) {
            graphics2D.drawString(">", textX - 30, textY);

            if (gamePanel.keyHandler.spacePressed) {
                subState = States.PAUSE_CONTROLS;
                commandNum = 0;
            }
        }

        // Close
        textY += gamePanel.tileSize * 2;
        graphics2D.drawString("Close", textX, textY);
        if (commandNum == 5) {
            graphics2D.drawString(">", textX - 30, textY);

            if (gamePanel.keyHandler.spacePressed) {
                subState = States.PAUSE_STATE_MAIN;
                commandNum = 0;
            }
        }

        // Full Screen Check Box
        textX = frameX + gamePanel.tileSize * 5;
        textY = frameY + gamePanel.tileSize * 2 + (gamePanel.tileSize / 2) + 14;
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRect(textX, textY, 24 , 24);

        if (gamePanel.fullScreen) {
            graphics2D.fillRect(textX, textY, 24, 24);
        }

        // BRendering Check Box
        textY += gamePanel.tileSize;
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRect(textX, textY, 24 , 24);

        if (gamePanel.BRendering) {
            graphics2D.fillRect(textX, textY, 24, 24);
        }

        // Music Slider
        textY += gamePanel.tileSize;
        textX -= gamePanel.tileSize;
        graphics2D.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24 * gamePanel.music.volumeScale;
        graphics2D.fillRect(textX, textY, volumeWidth, 24);

        // Sound Slider
        textY += gamePanel.tileSize;
        graphics2D.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * gamePanel.sound.volumeScale;
        graphics2D.fillRect(textX, textY, volumeWidth, 24);

        // Save Data
        gamePanel.config.saveConfig();
    }
    public void settingsNotification(int frameX, int frameY) {
        // Title
        String text = "Note:";
        int textX = getCentreX(text);
        int textY = frameY + gamePanel.tileSize + (gamePanel.tileSize / 4);
        graphics2D.drawString(text, textX, textY);

        // Text
        textX = frameX + gamePanel.tileSize / 2;
        textY = frameY + gamePanel.tileSize * 2 + (gamePanel.tileSize / 4);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 28f));

        for (String line : currentDialogue.split("/n")) {
            graphics2D.drawString(line, textX, textY);
            textY += 40;
        }

        // Close
        textY = frameY = gamePanel.tileSize * 8 + (gamePanel.tileSize / 2);
        textX += gamePanel.tileSize / 2;
        graphics2D.drawString("Close", textX, textY);
        if (commandNum == 0) {
            graphics2D.drawString(">", textX - 30, textY);
            if (gamePanel.keyHandler.spacePressed) {
                subState = States.PAUSE_SETTINGS_MAIN;
                commandNum = 0;
            }
        }
    }
    public void settingsConfirm(int frameX, int frameY) {
        // Title
        String text = "Are You Sure?";
        int textX = getCentreX(text);
        int textY = frameY + gamePanel.tileSize + (gamePanel.tileSize / 4);
        graphics2D.drawString(text, textX, textY);

        // Text
        textX = frameX + gamePanel.tileSize / 2;
        textY = frameY + gamePanel.tileSize * 2 + (gamePanel.tileSize / 4);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN,28f));

        for (String line : currentDialogue.split("/n")) {
            graphics2D.drawString(line, textX, textY);
            textY += 40;
        }

        // Yes
        textY = frameY = gamePanel.tileSize * 8 - (gamePanel.tileSize / 4);
        textX += gamePanel.tileSize / 2;
        graphics2D.drawString("Yes", textX, textY);
        if (commandNum == 0) {
            graphics2D.drawString(">", textX - 30, textY);
            if (gamePanel.keyHandler.spacePressed) {
                try {
                    Method method = this.getClass().getMethod(actionMethod);
                    method.invoke(this);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        // No
        textY += 40;
        graphics2D.drawString("No", textX, textY);
        if (commandNum == 1) {
            graphics2D.drawString(">", textX - 30, textY);
            if (gamePanel.keyHandler.spacePressed) {
                subState = States.PAUSE_SETTINGS_MAIN;
                commandNum = 4;
            }
        }
    }
    public void yesGameEnd() {
        subState = States.PAUSE_SETTINGS_MAIN;
        gamePanel.gameState = States.STATE_TILE;
        titleScreenState = States.TITLE_STATE_MAIN;
        gamePanel.music.stop();
        gamePanel.playMusic(5);
    }
    public void yesFullScreen() {
        gamePanel.fullScreen = !gamePanel.fullScreen;
        subState = States.PAUSE_SETTINGS_NOTIFICATION;
        currentDialogue = "Full Screen will only be /nenabled/disabled when /nrelaunching the game.";
    }
    public void yesBRendering() {
        gamePanel.BRendering = !gamePanel.BRendering;
        if (gamePanel.BRendering) {
            commandNum = 0;
            subState = States.PAUSE_SETTINGS_NOTIFICATION;
            currentDialogue = "You can emergency /ndisable BRendering by /npressing U.";
        } else {
            commandNum = 1;
            subState = States.PAUSE_SETTINGS_MAIN;
        }
    }
    public void controlsPanel(int frameX, int frameY) {
        int textX;
        int textY;

        // Title
        String text = "Controls:";
        textX = getCentreX(text);
        textY = frameY + gamePanel.tileSize + (gamePanel.tileSize / 4);
        graphics2D.drawString(text, textX, textY);

        // Control Titles
        textX = frameX + gamePanel.tileSize / 2;
        textY = frameY + gamePanel.tileSize * 2 + (gamePanel.tileSize / 4);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 28f));

        graphics2D.drawString("Move", textX, textY); textY += gamePanel.tileSize;
        graphics2D.drawString("Confirm", textX, textY); textY += gamePanel.tileSize;
        graphics2D.drawString("Attack", textX, textY); textY += gamePanel.tileSize;
        graphics2D.drawString("Interact", textX, textY); textY += gamePanel.tileSize;
        graphics2D.drawString("Inventory", textX, textY); textY += gamePanel.tileSize;

        // Control Keys
        textX = frameX + gamePanel.tileSize * 5 + (gamePanel.tileSize / 2);
        textY = frameY + gamePanel.tileSize * 2 + (gamePanel.tileSize / 4);
        graphics2D.drawString("WASD", textX, textY); textY += gamePanel.tileSize;
        graphics2D.drawString("Space", textX, textY); textY += gamePanel.tileSize;
        graphics2D.drawString("Space", textX, textY); textY += gamePanel.tileSize;
        graphics2D.drawString("      E", textX, textY); textY += gamePanel.tileSize;
        graphics2D.drawString("      E", textX, textY); textY += gamePanel.tileSize;

        // Close
        textY = frameY = gamePanel.tileSize * 10 + (gamePanel.tileSize / 2);
        textX = frameX + gamePanel.tileSize;
        graphics2D.drawString("Close", textX, textY);
        if (commandNum == 0) {
            graphics2D.drawString(">", textX - 30, textY);
            if (gamePanel.keyHandler.spacePressed) {
                subState = States.PAUSE_SETTINGS_MAIN;
                commandNum = 3;
            }
        }

    }
    public void drawDialogueScreen() {
        int x = gamePanel.tileSize * 4;
        int y = gamePanel.tileSize / 2 + (gamePanel.tileSize * 7);
        int width = gamePanel.screenWidth - (gamePanel.tileSize * 9);
        int height = gamePanel.tileSize * 4;
        drawSubWindow(x, y, width, height);

        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 28f));
        x += gamePanel.tileSize / 2;
        y += gamePanel.tileSize;

        for (String line : currentDialogue.split("/n")) {
            graphics2D.drawString(line, x, y);
            y += 40;
        }
    }
    public void drawCharacterScreen() {
        // Make a frame
        final int frameX = gamePanel.tileSize / 2;
        final int frameY = gamePanel.tileSize / 2;
        final int frameWidth = gamePanel.tileSize * 6;
        final int frameHeight = gamePanel.tileSize * 11;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text
        graphics2D.setColor(Color.white);
        graphics2D.setFont(graphics2D.getFont().deriveFont(30f));

        int textX = frameX + 20;
        int textY = frameY + gamePanel.tileSize;
        final int lineHeight = 35;

        // Titles
        graphics2D.drawString("Level", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Next Level", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Exp", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("--------", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Strength", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Attack", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Defence", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("--------", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Health", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Coins", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("--------", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Weapon", textX, textY);
        textY += lineHeight;
        graphics2D.drawString("Shield", textX, textY);

        // Values
        int tailX = (frameX + frameWidth) - 30;
        textY = frameY + gamePanel.tileSize;
        String value;

        value = String.valueOf(gamePanel.player.level);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.nextLevelExp);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.exp);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight * 2;

        value = String.valueOf(gamePanel.player.strength);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.dexterity);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.attack);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.defence);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight * 2;

        value = String.valueOf(gamePanel.player.health + "/" + gamePanel.player.maxHealth);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.coins);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight * 2 - (lineHeight / 4);

       graphics2D.drawImage(gamePanel.player.currentWeapon.down1, tailX - gamePanel.tileSize, textY - 37, null);
       textY += gamePanel.tileSize;
       graphics2D.drawImage(gamePanel.player.currentShield.down1, tailX - gamePanel.tileSize, textY - 37, null);
    }
    public void drawInventory() {
        // Frame
        int frameX = gamePanel.tileSize / 2 + gamePanel.tileSize * 13;
        int frameY = gamePanel.tileSize / 2;
        int frameWidth = gamePanel.tileSize * 6;
        int frameHeight = gamePanel.tileSize * 6;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Slots
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gamePanel.tileSize + 3;

        // Draw Items
        for (int i = 0; i < gamePanel.player.inventory.size(); i++) {
            // Equip Cursor
            if (gamePanel.player.inventory.get(i) == gamePanel.player.currentWeapon ||
                    gamePanel.player.inventory.get(i) == gamePanel.player.currentShield ||
                    gamePanel.player.inventory.get(i) == gamePanel.player.currentLight) {
                graphics2D.setColor(new Color(240, 190, 90));
                graphics2D.fillRoundRect(slotX, slotY, gamePanel.tileSize, gamePanel.tileSize, 10, 10);
            }

            graphics2D.drawImage(gamePanel.player.inventory.get(i).down1, slotX, slotY, null);

            // Draw Amount
            if (gamePanel.player.inventory.get(i).amount > 1) {
                graphics2D.setFont(graphics2D.getFont().deriveFont(28f));
                int amountX;
                int amountY;

                String s = "" + gamePanel.player.inventory.get(i).amount;
                amountX = alignXToRight(s, slotX + 44);
                amountY = slotY + gamePanel.tileSize;

                // Shadow
                graphics2D.setColor(new Color(60, 60, 60));
                graphics2D.drawString(s, amountX, amountY);

                // Text
                graphics2D.setColor(Color.white);
                graphics2D.drawString(s, amountX - 3, amountY - 3);
            }

            slotX += slotSize;

            if (i == 4 || i == 9 || i == 14 || i == 19) {
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // Cursor
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gamePanel.tileSize;
        int cursorHeight = gamePanel.tileSize;

        // Draw Cursor
        graphics2D.setColor(Color.white);
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // Description Frame
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gamePanel.tileSize * 4 - (gamePanel.tileSize / 2);

        // Description Text
        int textX = dFrameX + 20;
        int textY = dFrameY + gamePanel.tileSize;

        int itemIndex = getItemIndex();
        if (itemIndex < gamePanel.player.inventory.size()) {
            // Window + Title
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            graphics2D.setFont(graphics2D.getFont().deriveFont(30f));
            graphics2D.drawString(gamePanel.player.inventory.get(itemIndex).name, textX, textY);
            textY += 10;

            // Description
            graphics2D.setFont(graphics2D.getFont().deriveFont(20f));
            for (String line : gamePanel.player.inventory.get(itemIndex).description.split("/n")) {
                graphics2D.drawString(line, textX, textY);
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
        graphics2D.setColor(c);
        graphics2D.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255,255);
        graphics2D.setColor(c);
        graphics2D.setStroke(new BasicStroke(5));
        graphics2D.drawRoundRect(x + 5, y + 5, width - 10, height - 10 ,25, 25);
    }

    public int getCentreX(String text) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = gamePanel.screenWidth / 2 - length / 2;
        return x;

    }    public int alignXToRight(String text, int tailX) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        int x = tailX - length;
        return x;
    }
}
