package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.object.OBJ_Coins;
import net.dinglezz.torgrays_trials.object.OBJ_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

public class UI {
    Game game;
    Graphics2D graphics2D;
    Font maruMonica;
    BufferedImage heart, half_heart, lost_heart, coin;
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public String currentDialogue = "";
    public int commandNumber = 0;
    public States titleScreenState = States.TITLE_STATE_MAIN;
    public States subState = States.PAUSE_STATE_MAIN;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int entitySlotCol = 0;
    public int entitySlotRow = 0;
    public String actionMethod;
    public float transitionCounter = 0f;
    public boolean fadeBack = false;
    public Entity npc;

    public UI(Game game) {
        this.game = game;

        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        // Make a HUD object
        Entity obj_heart = new OBJ_Heart(game);
        heart = obj_heart.image;
        half_heart = obj_heart.image2;
        lost_heart = obj_heart.image3;
        Entity Coin = new OBJ_Coins(game, 1);
        coin = Coin.down1;
    }

    public void addMessage(String message) {
        messages.add(message);
        messageCounter.add(0);
    }
    public void draw(Graphics2D g2) {
        this.graphics2D = g2;
        g2.setFont(maruMonica);
        g2.setColor(Color.white);


        switch (game.gameState) {
            case STATE_TITLE: drawTitleScreen(); break;
            case STATE_PLAY: drawPlayerHealth(); drawMessage(); break;
            case STATE_PAUSE: drawPauseScreen(); break;
            case STATE_DIALOGUE: drawDialogueScreen(); drawPlayerHealth(); break;
            case STATE_CHARACTER: drawCharacterScreen(); drawInventory(game.player, true); break;
            case STATE_GAME_OVER: drawGameOverScreen(); break;
            case STATE_TRANSITION: drawTransitionScreen(); break;
            case STATE_TRADE: drawTradeScreen(); break;
            case STATE_MAP: drawMapScreen(); break;
        }
    }

    public void drawPlayerHealth() {
        int x = game.tileSize / 2;
        int y = game.tileSize / 2;
        int i = 0;

        // Draw max health
        while (i < game.player.maxHealth / 2) {
            graphics2D.drawImage(lost_heart, x, y, null);
            i++;
            x += game.tileSize;
        }

        // Reset
        x = game.tileSize / 2;
        y = game.tileSize / 2;
        i = 0;

        // Draw current health
        while (i < game.player.health) {
            graphics2D.drawImage(half_heart, x, y, null);
            i++;
            if (i < game.player.health) {
                graphics2D.drawImage(heart, x, y, null);
            }
            i++;
            x += game.tileSize;
        }

    }
    public void drawMessage() {
        int messageX = game.tileSize / 2;
        int messageY = game.tileSize * 12 - game.tileSize / 2;
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
            int y = game.tileSize * 3;

            // Title Shadow
            graphics2D.setColor(new Color(147, 37, 37));
            graphics2D.drawString(text, x + 4, y + 4);

            // Main Title
            graphics2D.setColor(new Color(209, 25, 25));
            graphics2D.drawString(text, x, y);

            // Torgray Image
            x = game.screenWidth / 2 - (game.tileSize * 2) / 2;
            y += game.tileSize + (game.tileSize / 2);
            graphics2D.drawImage(game.player.down1, x, y, game.tileSize * 2, game.tileSize * 2, null);

            // Menu
            graphics2D.setColor(Color.white);

            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48f));
            text = "New Game";
            x = getCentreX(text);
            y += (game.tileSize * 3) + (game.tileSize / 2);
            graphics2D.drawString(text, x, y);
            if (commandNumber == 0) {
                graphics2D.drawString(">", x - game.tileSize,  y);
            }


            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48f));
            text = "Load Game";
            x = getCentreX(text);
            y += game.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNumber == 1) {
                graphics2D.drawString(">", x - game.tileSize,  y);
            }

            graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48f));
            text = "Quit";
            x = getCentreX(text);
            y += game.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNumber == 2) {
                graphics2D.drawString(">", x - game.tileSize,  y);
            }
        } else if (titleScreenState == States.TITLE_STATE_MODES) {
            // GameMode Selection
            graphics2D.setColor(Color.white);
            graphics2D.setFont(graphics2D.getFont().deriveFont(42f));

            String text = "Select a GameMode";
            int x = getCentreX(text);
            int y = game.tileSize * 3;
            graphics2D.drawString(text, x, y);

            text = "Easy";
            x = getCentreX(text);
            y += game.tileSize * 3;
            graphics2D.drawString(text, x, y);
            if (commandNumber == 0) {
                graphics2D.drawString(">", x - game.tileSize, y);
            }

            text = "Medium";
            x = getCentreX(text);
            y += game.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNumber == 1) {
                graphics2D.drawString(">", x - game.tileSize, y);
            }

            text = "Hard";
            x = getCentreX(text);
            y += game.tileSize;
            graphics2D.drawString(text, x, y);
            if (commandNumber == 2) {
                graphics2D.drawString(">", x - game.tileSize, y);
            }

            text = "Back";
            y += game.tileSize * 2;
            graphics2D.drawString(text, x, y);
            if (commandNumber == 3) {
                graphics2D.drawString(">", x - game.tileSize, y);
            }
        }

    }
    public void drawPauseScreen() {
        graphics2D.setColor(Color.white);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48f));

        // Sub-Window
        int frameX = game.tileSize * 6;
        int frameY = game.tileSize * 3;
        int frameWidth = game.tileSize * 8;
        int frameHeight = game.tileSize * 2;

        if (subState == States.PAUSE_STATE_SETTINGS_MAIN || subState == States.PAUSE_STATE_CONTROLS) {
            frameY = game.tileSize;
            frameHeight = game.tileSize * 10;
        }
        else if (subState == States.PAUSE_STATE_NOTIFICATION || subState == States.PAUSE_STATE_CONFIRM) {
            frameY = game.tileSize * 3;
            frameHeight = game.tileSize * 6;
        }
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case States.PAUSE_STATE_MAIN:
                // Title
                String text = "Game Paused";
                int x = getCentreX(text);
                int y = frameY + game.tileSize + (game.tileSize / 3);
                graphics2D.drawString(text, x, y);

                // Options Frame
                x = frameX + game.tileSize;
                y = frameY + (game.tileSize * 2) + (game.tileSize / 4);
                drawSubWindow(x, y, frameWidth - (game.tileSize * 2), frameHeight + game.tileSize + (game.tileSize / 2));

                // Settings
                graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32f));
                y += game.tileSize;
                x += game.tileSize + (game.tileSize / 2);
                graphics2D.drawString("Settings", x, y);
                if (commandNumber == 0) {
                    graphics2D.drawString(">", x - game.tileSize, y);

                    if (game.inputHandler.spacePressed) {
                        subState = States.PAUSE_STATE_SETTINGS_MAIN;
                        commandNumber = 0;
                    }
                }

                // End Game
                y += game.tileSize;
                graphics2D.drawString("End Game", x, y);
                if (commandNumber == 1) {
                    graphics2D.drawString(">", x - game.tileSize, y);

                    if (game.inputHandler.spacePressed) {
                        commandNumber = 1;
                        subState = States.PAUSE_STATE_CONFIRM;
                        currentDialogue = "Are you sure you wanna /nend this game? Your data /nwon't be saved.";
                        actionMethod = "yesGameEnd";
                    }
                }

                // Resume
                y += game.tileSize;
                graphics2D.drawString("Resume", x, y);
                if (commandNumber == 2) {
                    graphics2D.drawString(">", x - game.tileSize, y);

                    if (game.inputHandler.spacePressed) {
                        game.gameState = States.STATE_PLAY;
                        commandNumber = 0;
                    }
                }
                break;
            case States.PAUSE_STATE_SETTINGS_MAIN: settingsMain(frameX, frameY); break;
            case States.PAUSE_STATE_NOTIFICATION: settingsNotification(frameX, frameY); break;
            case States.PAUSE_STATE_CONFIRM: settingsConfirm(frameX, frameY); break;
            case States.PAUSE_STATE_CONTROLS: controlsPanel(frameX, frameY); break;
        }

        game.inputHandler.spacePressed = false;
    }

    public void settingsMain(int frameX, int frameY) {
        int textX;
        int textY;

        // Title
        String text = "Settings";
        textX = getCentreX(text);
        textY = frameY + game.tileSize + (game.tileSize / 4);
        graphics2D.drawString(text, textX, textY);

        // Fullscreen
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32f));
        textX = frameX + game.tileSize;
        textY += game.tileSize * 2;
        graphics2D.drawString("Full Screen", textX, textY);
        if (commandNumber == 0) {
            graphics2D.drawString(">", textX - 30, textY);

            if (game.inputHandler.spacePressed) {
                if (!game.fullScreen && !game.BRendering) {
                    commandNumber = 1;
                    subState = States.PAUSE_STATE_CONFIRM;
                    currentDialogue = "Are you sure you wanna /nenter full screen? It won't /nscale without BRendering.";
                    actionMethod = "yesFullScreen";
                } else {
                    yesFullScreen();
                }
            }
        }
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 32f));
        textX = frameX + game.tileSize;
        textY += game.tileSize;
        graphics2D.drawString("BRendering", textX, textY);
        if (commandNumber == 1) {
            graphics2D.drawString(">", textX - 30, textY);

            if (game.inputHandler.spacePressed) {
                if (!game.BRendering) {
                    commandNumber = 1;
                    subState = States.PAUSE_STATE_CONFIRM;
                    currentDialogue = "BRendering is a highly /nexperimental mode that /ncan break the game if on";
                    actionMethod = "yesBRendering";
                } else {
                    yesBRendering();
                }
            }
        }

        // Music
        textY += game.tileSize;
        graphics2D.drawString("Music", textX, textY);
        if (commandNumber == 2) {
            graphics2D.drawString(">", textX - 30, textY);
        }


        // Sound Effects
        textY += game.tileSize;
        graphics2D.drawString("Sounds", textX, textY);
        if (commandNumber == 3) {
            graphics2D.drawString(">", textX - 30, textY);
        }


        // Controls
        textY += game.tileSize;
        graphics2D.drawString("Controls", textX, textY);
        if (commandNumber == 4) {
            graphics2D.drawString(">", textX - 30, textY);

            if (game.inputHandler.spacePressed) {
                subState = States.PAUSE_STATE_CONTROLS;
                commandNumber = 0;
            }
        }

        // Close
        textY += game.tileSize * 2;
        graphics2D.drawString("Close", textX, textY);
        if (commandNumber == 5) {
            graphics2D.drawString(">", textX - 30, textY);

            if (game.inputHandler.spacePressed) {
                subState = States.PAUSE_STATE_MAIN;
                commandNumber = 0;
            }
        }

        // Full Screen Check Box
        textX = frameX + game.tileSize * 5;
        textY = frameY + game.tileSize * 2 + (game.tileSize / 2) + 14;
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRect(textX, textY, 24 , 24);

        if (game.fullScreen) {
            graphics2D.fillRect(textX, textY, 24, 24);
        }

        // BRendering Check Box
        textY += game.tileSize;
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRect(textX, textY, 24 , 24);

        if (game.BRendering) {
            graphics2D.fillRect(textX, textY, 24, 24);
        }

        // Music Slider
        textY += game.tileSize;
        textX -= game.tileSize;
        graphics2D.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24 * game.music.volumeScale;
        graphics2D.fillRect(textX, textY, volumeWidth, 24);

        // Sound Slider
        textY += game.tileSize;
        graphics2D.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * game.sound.volumeScale;
        graphics2D.fillRect(textX, textY, volumeWidth, 24);

        // Save Data
        game.config.saveConfig();
    }
    public void settingsNotification(int frameX, int frameY) {
        // Title
        String text = "Note:";
        int textX = getCentreX(text);
        int textY = frameY + game.tileSize + (game.tileSize / 4);
        graphics2D.drawString(text, textX, textY);

        // Text
        textX = frameX + game.tileSize / 2;
        textY = frameY + game.tileSize * 2 + (game.tileSize / 4);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 28f));

        for (String line : currentDialogue.split("/n")) {
            graphics2D.drawString(line, textX, textY);
            textY += 40;
        }

        // Close
        textY = frameY = game.tileSize * 8 + (game.tileSize / 2);
        textX += game.tileSize / 2;
        graphics2D.drawString("Close", textX, textY);
        if (commandNumber == 0) {
            graphics2D.drawString(">", textX - 30, textY);
            if (game.inputHandler.spacePressed) {
                subState = States.PAUSE_STATE_MAIN;
                commandNumber = 0;
            }
        }
    }
    public void settingsConfirm(int frameX, int frameY) {
        // Title
        String text = "Are You Sure?";
        int textX = getCentreX(text);
        int textY = frameY + game.tileSize + (game.tileSize / 4);
        graphics2D.drawString(text, textX, textY);

        // Text
        textX = frameX + game.tileSize / 2;
        textY = frameY + game.tileSize * 2 + (game.tileSize / 4);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN,28f));

        for (String line : currentDialogue.split("/n")) {
            graphics2D.drawString(line, textX, textY);
            textY += 40;
        }

        // Yes
        textY = frameY = game.tileSize * 8 - (game.tileSize / 4);
        textX += game.tileSize / 2;
        graphics2D.drawString("Yes", textX, textY);
        if (commandNumber == 0) {
            graphics2D.drawString(">", textX - 30, textY);
            if (game.inputHandler.spacePressed) {
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
        if (commandNumber == 1) {
            graphics2D.drawString(">", textX - 30, textY);
            if (game.inputHandler.spacePressed) {
                subState = States.PAUSE_STATE_MAIN;
                commandNumber = 0;
            }
        }
    }
    @SuppressWarnings("unused")
    public void yesGameEnd() {
        subState = States.PAUSE_STATE_MAIN;
        game.gameState = States.STATE_TITLE;
        titleScreenState = States.TITLE_STATE_MAIN;
        game.music.stop();
        game.playMusic("Tech Geek");
        game.restart();
    }
    @SuppressWarnings("unused")
    public void yesFullScreen() {
        game.fullScreen = !game.fullScreen;
        subState = States.PAUSE_STATE_NOTIFICATION;
        currentDialogue = "Full Screen will only be /nenabled/disabled when /nrelaunching the game.";
    }
    @SuppressWarnings("unused")
    public void yesBRendering() {
        game.BRendering = !game.BRendering;
        if (game.BRendering) {
            commandNumber = 0;
            subState = States.PAUSE_STATE_NOTIFICATION;
            currentDialogue = "You can emergency /ndisable BRendering by /npressing U.";
        } else {
            commandNumber = 1;
            subState = States.PAUSE_STATE_SETTINGS_MAIN;
        }
    }
    public void controlsPanel(int frameX, int frameY) {
        int textX;
        int textY;

        // Title
        String text = "Controls:";
        textX = getCentreX(text);
        textY = frameY + game.tileSize + (game.tileSize / 4);
        graphics2D.drawString(text, textX, textY);

        // Control Titles
        textX = frameX + game.tileSize / 2;
        textY = frameY + game.tileSize * 2 + (game.tileSize / 4);
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 28f));

        graphics2D.drawString("Move", textX, textY); textY += game.tileSize;
        graphics2D.drawString("Confirm", textX, textY); textY += game.tileSize;
        graphics2D.drawString("Exit", textX, textY); textY += game.tileSize;
        graphics2D.drawString("Attack", textX, textY); textY += game.tileSize;
        graphics2D.drawString("Interact", textX, textY); textY += game.tileSize;
        graphics2D.drawString("Inventory", textX, textY); textY += game.tileSize;

        // Control Keys
        textX = frameX + game.tileSize * 5 + (game.tileSize / 2);
        textY = frameY + game.tileSize * 2 + (game.tileSize / 4);
        graphics2D.drawString("WASD", textX, textY); textY += game.tileSize;
        graphics2D.drawString("Space", textX, textY); textY += game.tileSize;
        graphics2D.drawString("    Esc", textX, textY); textY += game.tileSize;
        graphics2D.drawString("Space", textX, textY); textY += game.tileSize;
        graphics2D.drawString("       E", textX, textY); textY += game.tileSize;
        graphics2D.drawString("       E", textX, textY); textY += game.tileSize;

        // Close
        textY = frameY = game.tileSize * 10 + (game.tileSize / 2);
        textX = frameX + game.tileSize;
        graphics2D.drawString("Close", textX, textY);
        if (commandNumber == 0) {
            graphics2D.drawString(">", textX - 30, textY);
            if (game.inputHandler.spacePressed) {
                subState = States.PAUSE_STATE_SETTINGS_MAIN;
                commandNumber = 3;
            }
        }

    }
    public void drawDialogueScreen() {
        int x = game.tileSize * 4;
        int y = game.tileSize / 2 + (game.tileSize * 7);
        int width = game.screenWidth - (game.tileSize * 9);
        int height = game.tileSize * 4;
        drawSubWindow(x, y, width, height);

        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 28f));
        x += game.tileSize / 2;
        y += game.tileSize;

        for (String line : currentDialogue.split("/n")) {
            graphics2D.drawString(line, x, y);
            y += 40;
        }
    }
    public void drawCharacterScreen() {
        // Make a frame
        final int frameX = game.tileSize / 2;
        final int frameY = game.tileSize / 2;
        final int frameWidth = game.tileSize * 6;
        final int frameHeight = game.tileSize * 11;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text
        graphics2D.setColor(Color.white);
        graphics2D.setFont(graphics2D.getFont().deriveFont(30f));

        int textX = frameX + 20;
        int textY = frameY + game.tileSize;
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
        textY = frameY + game.tileSize;
        String value;

        value = String.valueOf(game.player.level);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(game.player.nextLevelExp);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(game.player.exp);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight * 2;

        value = String.valueOf(game.player.strength);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(game.player.dexterity);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(game.player.attack);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(game.player.defence);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight * 2;

        value = String.valueOf(game.player.health + "/" + game.player.maxHealth);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(game.player.coins);
        textX = alignXToRight(value, tailX);
        graphics2D.drawString(value, textX, textY);
        textY += lineHeight * 2 - (lineHeight / 4);

       graphics2D.drawImage(game.player.currentWeapon.down1, tailX - game.tileSize, textY - 37, null);
       textY += game.tileSize;
       graphics2D.drawImage(game.player.currentShield.down1, tailX - game.tileSize, textY - 37, null);
    }
    public void drawInventory(Entity entity, boolean cursor) {
        // Frame
        int frameX;
        int frameY;
        int frameWidth;
        int frameHeight;
        int slotCol;
        int slotRow;
        if (entity == game.player) {
            frameX = game.tileSize / 2 + game.tileSize * 13;
            frameY = game.tileSize / 2;
            frameWidth = game.tileSize * 6;
            frameHeight = game.tileSize * 6;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } else {
            frameX = game.tileSize / 2;
            frameY = game.tileSize / 2;
            frameWidth = game.tileSize * 6;
            frameHeight = game.tileSize * 6;
            slotCol = entitySlotCol;
            slotRow = entitySlotRow;
        }
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Slots
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = game.tileSize + 3;

        // Draw Items
        for (int i = 0; i < entity.inventory.size(); i++) {
            // Equip Cursor
            if (entity.inventory.get(i) == entity.currentWeapon ||
                    entity.inventory.get(i) == entity.currentShield ||
                    entity.inventory.get(i) == entity.currentLight) {
                graphics2D.setColor(new Color(240, 190, 90));
                graphics2D.fillRoundRect(slotX, slotY, game.tileSize, game.tileSize, 10, 10);
            }

            graphics2D.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);

            // Draw Amount
            if (entity.inventory.get(i).amount > 1) {
                graphics2D.setFont(graphics2D.getFont().deriveFont(28f));
                int amountX;
                int amountY;

                String string;
                if (Objects.equals(entity.inventory.get(i).name, "Coins")) {
                    string = "" + entity.coins;
                } else {
                    string = "" + entity.inventory.get(i).amount;
                }
                amountX = alignXToRight(string, slotX + 44);
                amountY = slotY + game.tileSize;

                // Shadow
                graphics2D.setColor(new Color(60, 60, 60));
                graphics2D.drawString(string, amountX, amountY);

                // Text
                graphics2D.setColor(Color.white);
                graphics2D.drawString(string, amountX - 3, amountY - 3);
            }

            slotX += slotSize;

            if (i == 4 || i == 9 || i == 14 || i == 19) {
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        // Cursor
        if (cursor) {
            int cursorX = slotXStart + (slotSize * slotCol);
            int cursorY = slotYStart + (slotSize * slotRow);
            int cursorWidth = game.tileSize;
            int cursorHeight = game.tileSize;

            // Draw Cursor
            graphics2D.setColor(Color.white);
            graphics2D.setStroke(new BasicStroke(3));
            graphics2D.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            // Description Frame
            int dFrameY = frameY + frameHeight;
            int dFrameHeight = game.tileSize * 4 - (game.tileSize / 2);

            // Description Text
            int textX = frameX + 20;
            int textY = dFrameY + game.tileSize;

            int itemIndex = getItemIndex(slotCol, slotRow);
            if (itemIndex < entity.inventory.size()) {
                // Window + Title
                drawSubWindow(frameX, dFrameY, frameWidth, dFrameHeight);
                graphics2D.setFont(graphics2D.getFont().deriveFont(30f));
                graphics2D.drawString(entity.inventory.get(itemIndex).name, textX, textY);
                textY += 10;

                // Description
                graphics2D.setFont(graphics2D.getFont().deriveFont(20f));
                for (String line : entity.inventory.get(itemIndex).description.split("/n")) {
                    graphics2D.drawString(line, textX, textY);
                    textY += 30;
                }
            }
        }
    }
    public void drawGameOverScreen() {
        graphics2D.setColor(new Color(0, 0, 0, 150));
        graphics2D.fillRect(0, 0, game.screenWidth, game.screenHeight);

        int x;
        int y;
        String text;
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 110f));
        text = "Game Over";

        // Shadow
        graphics2D.setColor(new Color(147, 37, 37));
        x = getCentreX(text);
        y = game.tileSize * 4;
        graphics2D.drawString(text, x, y);

        // Main
        graphics2D.setColor(new Color(209, 25, 25));
        graphics2D.drawString(text, x - 4, y -4);


        // Restart
        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 50f));
        graphics2D.setColor(Color.white);
        text = "Restart";
        x = getCentreX(text);
        y += game.tileSize * 4;
        graphics2D.drawString(text, x, y);
        if (commandNumber == 0) {
            graphics2D.drawString(">", x - 40, y);
        }

        // Respawn
        if (game.gameMode.equals("Easy")) {
            text = "Respawn";
            x = getCentreX(text);
            y += 55;
            graphics2D.drawString(text, x, y);
            if (commandNumber == 1) {
                graphics2D.drawString(">", x - 40, y);
            }
        }

        // Quit
        text = "Quit";
        x = getCentreX(text);
        y += 55;
        graphics2D.drawString(text, x, y);
        if (commandNumber == game.inputHandler.maxCommandNumber) {
            graphics2D.drawString(">", x - 40, y);
        }
    }
    public void drawTransitionScreen() {
        // Main
        if (!fadeBack && transitionCounter < 1f) {
            transitionCounter += 0.02f;
        } else if (fadeBack && transitionCounter > 0f) {
            transitionCounter -= 0.02f;
        }
        // Switch to next stage (if needed)
        else if (transitionCounter == 1f) {
            // Teleport
            game.currentMap = game.eventHandler.nextMap;
            game.player.worldX = game.eventHandler.nextCol;
            game.player.worldY = game.eventHandler.nextRow;
            game.player.lightUpdated = true;

            fadeBack = true;
        } else if (transitionCounter == 0f) {
            game.gameState = States.STATE_PLAY;
            fadeBack = false;
        }

        // Corrections (just in case)
        if (transitionCounter > 1f) {
            transitionCounter = 1f;
        } else if (transitionCounter < 0f) {
            transitionCounter = 0f;
        }

        // Draw Transition
        graphics2D.setColor(new Color(0, 0, 0, transitionCounter));
        graphics2D.fillRect(0, 0, game.screenWidth, game.screenHeight);
    }
    public void drawTradeScreen() {
        switch (subState) {
            case TRADE_STATE_SELECT: drawTradeSelectScreen(); break;
            case TRADE_STATE_BUY: drawTradeBuyScreen(); break;
            case TRADE_STATE_SELL: drawTradeSellScreen(); break;
        }
        game.inputHandler.spacePressed = false;
    }
    public void drawTradeSelectScreen() {
        drawDialogueScreen();

        // Draw Window
        int x = game.tileSize * 15;
        int y = (game.tileSize * 8) - (game.tileSize / 4);
        int width = game.tileSize * 3;
        int height = (game.tileSize * 3) + (game.tileSize / 2);
        drawSubWindow(x, y, width, height);

        // Buy Text
        x += game.tileSize;
        y += game.tileSize;
        graphics2D.drawString("Buy", x, y);
        if (commandNumber == 0) {
            graphics2D.drawString(">", x - 24, y);
            if (game.inputHandler.spacePressed) {
                game.ui.subState = States.TRADE_STATE_BUY;
            }
        }

        // Sell Text
        y += game.tileSize;
        graphics2D.drawString("Sell", x, y);
        if (commandNumber == 1) {
            graphics2D.drawString(">", x - 24, y);
            if (game.inputHandler.spacePressed) {
                game.ui.subState = States.TRADE_STATE_SELL;
            }
        }

        // Leave Text
        y += game.tileSize;
        graphics2D.drawString("Leave", x, y);
        if (commandNumber == 2) {
            graphics2D.drawString(">", x - 24, y);
            if (game.inputHandler.spacePressed) {
                game.gameState = States.STATE_PLAY;
                game.ui.subState = States.PAUSE_STATE_MAIN;
                game.ui.commandNumber = 0;
            }
        }
    }
    public void drawTradeBuyScreen() {
        // Inventories
        drawInventory(game.player, false);
        drawInventory(npc, true);

        // Hint Window
        graphics2D.setFont(graphics2D.getFont().deriveFont(30f));
        int x = (game.tileSize * 13) + (game.tileSize / 2);
        int y = (game.tileSize * 9) + (game.tileSize / 2);
        int width = game.tileSize * 6;
        int height = game.tileSize * 2;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("[ESC] to Exit", x + 24, y + 60);

        // Price Window
        graphics2D.setFont(graphics2D.getFont().deriveFont(27f));
        int itemIndex = getItemIndex(entitySlotCol, entitySlotRow);
        if (itemIndex < npc.inventory.size()) {
            x = game.tileSize * 5;
            y = game.tileSize * 6;
            width = (game.tileSize * 2) + (game.tileSize / 2);
            height = game.tileSize;
            drawSubWindow(x, y, width, height);
            graphics2D.drawImage(coin, x + 10, y + 8, 32, 32, null);

            int price = npc.inventory.get(itemIndex).price;
            String text = String.valueOf(price);
            x = alignXToRight(text, game.tileSize * 7 - 20);
            graphics2D.drawString(text, x, y + 34);

            // Buy an item
            if (game.inputHandler.spacePressed) {
                if (npc.inventory.get(itemIndex).price > game.player.coins) {
                    currentDialogue = "Sorry partner, your wallet declined :(";
                    game.gameState = States.STATE_DIALOGUE;
                    game.ui.subState = States.PAUSE_STATE_MAIN;
                    game.ui.commandNumber = 0;
                } else if (game.player.canObtainItem(npc.inventory.get(itemIndex))) {
                    game.player.coins -= price;
                } else {
                    currentDialogue = "Sorry partner, I don't think you can /ncarry this :(";
                    game.gameState = States.STATE_DIALOGUE;
                    game.ui.subState = States.PAUSE_STATE_MAIN;
                    game.ui.commandNumber = 0;
                }
            }
        }
    }
    public void drawTradeSellScreen() {
        // Inventory
        drawInventory(game.player, true);

        // Hint Window
        graphics2D.setFont(graphics2D.getFont().deriveFont(30f));
        int x = game.tileSize / 2;
        int y = (game.tileSize * 9) + (game.tileSize / 2);
        int width = game.tileSize * 6;
        int height = game.tileSize * 2;
        drawSubWindow(x, y, width, height);
        graphics2D.drawString("[ESC] to Exit", x + 24, y + 60);

        // Price Window
        graphics2D.setFont(graphics2D.getFont().deriveFont(27f));
        int itemIndex = getItemIndex(playerSlotCol, playerSlotRow);
        if (itemIndex < game.player.inventory.size()) {
            x = game.tileSize * 13;
            y = game.tileSize * 6;
            width = (game.tileSize * 2) + (game.tileSize / 2);
            height = game.tileSize;
            drawSubWindow(x, y, width, height);
            graphics2D.drawImage(coin, x + 10, y + 8, 32, 32, null);

            int price = game.player.inventory.get(itemIndex).price / 2;
            String text = String.valueOf(price);
            x = alignXToRight(text, game.tileSize * 15 - 20);
            graphics2D.drawString(text, x, y + 34);

            // Sell an item
            if (game.inputHandler.spacePressed) {
                if (game.player.inventory.get(itemIndex) == game.player.currentWeapon ||
                        game.player.inventory.get(itemIndex) == game.player.currentShield ||
                        game.player.inventory.get(itemIndex) == game.player.currentLight) {
                    currentDialogue = "Sorry partner, I can't buy equipped /nitems :(";
                    game.gameState = States.STATE_DIALOGUE;
                    game.ui.subState = States.PAUSE_STATE_MAIN;
                    game.ui.commandNumber = 0;
                } else if (game.player.inventory.get(itemIndex).tags.contains(EntityTags.TAG_NON_SELLABLE)) {
                    currentDialogue = "Sorry partner, I can't buy this item :(";
                    game.gameState = States.STATE_DIALOGUE;
                    game.ui.subState = States.PAUSE_STATE_MAIN;
                    game.ui.commandNumber = 0;
                } else {
                    game.player.coins += price;
                    if (game.player.inventory.get(itemIndex).amount > 1) {
                        game.player.inventory.get(itemIndex).amount--;
                    } else {
                        game.player.inventory.remove(itemIndex);
                    }
                }
            }
        }
    }
    public void drawMapScreen() {
int width = 500;
int height = 500;
int x = game.screenWidth / 2 - width / 2;
int y = game.screenHeight / 2 - height / 2;
drawSubWindow(x - 20, y - 20, width + 40, height + 40);
graphics2D.drawImage(game.tileManager.worldMap.get(game.currentMap), x, y, width, height, null);
    }

    public int getItemIndex(int slotCol, int slotRow) {
        return slotCol + (slotRow * 5);
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
        return game.screenWidth / 2 - length / 2;

    }
    public int alignXToRight(String text, int tailX) {
        int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
        return tailX - length;
    }
}
