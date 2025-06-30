package net.dinglezz.torgrays_trials.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class InputHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, interactKeyPressed, f3Pressed;
    public int maxCommandNumber = 0;
    // Debug
    public boolean debug = false;

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used, but required by KeyListener interface :/
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (Main.game.ui.uiState.defaultKeyboardInput) {
            switch (Main.game.gameState) {
                case TITLE -> titleState(code);
                case PLAY -> playState(code);
                case GAME_END -> gameOverState(code);
                case EXCEPTION -> exceptionState(code);
            }
        }

        switch (Main.game.ui.uiState) {
            case DIALOGUE -> dialogueState(code);
            case PAUSE -> pauseState(code);
            case TRADE -> tradeState(code);
            case CHARACTER -> characterState(code);
            case MAP -> mapState(code);
            case SAVE -> saveState(code);
        }

        // F3 Stuff
        if (f3Pressed && code == KeyEvent.VK_P) {
            Main.game.debugPathfinding = !Main.game.debugPathfinding;
            Main.game.ui.addMiniNotification("Debug Pathfinding: " + Main.game.debugPathfinding);
            debug = false;
        } else if (f3Pressed && code == KeyEvent.VK_R) {
            if (Main.game.BRendering) {
                Main.game.BRendering = false;
                Main.game.ui.addMiniNotification("BRendering: " + Main.game.BRendering);
            }
        } else if (f3Pressed && code == KeyEvent.VK_B) {
            Main.game.debugHitBoxes = !Main.game.debugHitBoxes;
            Main.game.ui.addMiniNotification("Debug Hit Boxes: " + Main.game.debugHitBoxes);
            debug = false;
        } else if (code == KeyEvent.VK_F3) {
            f3Pressed = true;
            debug = true;
        }
    }
    public void titleState(int code) {
        if (Objects.equals(Main.game.ui.subUIState, "Main Title")) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                Main.game.ui.commandNumber--;
                Sound.playSFX("Cursor");
                if (Main.game.ui.commandNumber < 0) {
                    Main.game.ui.commandNumber = 2;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                Main.game.ui.commandNumber++;
                Sound.playSFX("Cursor");
                if (Main.game.ui.commandNumber > 2) {
                    Main.game.ui.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (Main.game.ui.commandNumber == 0) {
                    Main.game.ui.subUIState = "Modes";
                    Main.game.ui.commandNumber = 1;
                } else if (Main.game.ui.commandNumber == 1) {
                    Main.game.ui.subUIState = "Saves";
                    Main.game.ui.commandNumber = 0;
                } else if (Main.game.ui.commandNumber == 2) {
                    System.exit(0);
                }
            }
        } else if (Objects.equals(Main.game.ui.subUIState, "Modes")) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                Main.game.ui.commandNumber--;
                Sound.playSFX("Cursor");
                if (Main.game.ui.commandNumber < 0) {
                    Main.game.ui.commandNumber = 3;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                Main.game.ui.commandNumber++;
                Sound.playSFX("Cursor");
                if (Main.game.ui.commandNumber > 3) {
                    Main.game.ui.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (Main.game.ui.commandNumber == 0) {
                    Main.game.gameState = States.GameStates.PLAY;
                    Main.game.difficulty = "Easy";
                    Main.game.adjustDifficulty();
                    Sound.playMapMusic();
                } else if (Main.game.ui.commandNumber == 1) {
                    Main.game.gameState = States.GameStates.PLAY;
                    Main.game.difficulty = "Medium";
                    Main.game.adjustDifficulty();
                    Sound.playMapMusic();
                } else if (Main.game.ui.commandNumber == 2) {
                    Main.game.gameState = States.GameStates.PLAY;
                    Main.game.difficulty = "Hard";
                    Main.game.adjustDifficulty();
                    Sound.playMapMusic();
                } else if (Main.game.ui.commandNumber == 3) {
                    Main.game.ui.subUIState = "Main Title";
                    Main.game.ui.commandNumber = 0;
                }
            }
        } else if (Objects.equals(Main.game.ui.subUIState, "Saves")) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                Main.game.ui.commandNumber--;
                Sound.playSFX("Cursor");
                if (Main.game.ui.commandNumber < 0) Main.game.ui.commandNumber = 3;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                Main.game.ui.commandNumber++;
                Sound.playSFX("Cursor");
                if (Main.game.ui.commandNumber > 3) Main.game.ui.commandNumber = 0;
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (Main.game.ui.commandNumber == 0) {
                    if (DataManager.loadData(1)) {
                        Main.game.gameState = States.GameStates.PLAY;
                        Main.game.adjustDifficulty();
                        Sound.playMapMusic();
                    }
                } else if (Main.game.ui.commandNumber == 1) {
                    if (DataManager.loadData(2)) {
                        Main.game.gameState = States.GameStates.PLAY;
                        Main.game.adjustDifficulty();
                        Sound.playMapMusic();
                    }
                } else if (Main.game.ui.commandNumber == 2) {
                    if (DataManager.loadData(3)) {
                        Main.game.gameState = States.GameStates.PLAY;
                        Main.game.adjustDifficulty();
                        Sound.playMapMusic();
                    }
                } else if (Main.game.ui.commandNumber == 3) {
                    Main.game.ui.subUIState = "Main Title";
                    Main.game.ui.commandNumber = 0;
                }
            }
        }
    }
    public void playState(int code) {
        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_E -> interactKeyPressed = true;
            case KeyEvent.VK_SPACE -> spacePressed = true;
            case KeyEvent.VK_ESCAPE -> {
                if (Main.game.ui.uiState == States.UIStates.JUST_DEFAULT || Main.game.ui.uiState == States.UIStates.INTERACT) {
                    Main.game.gameState = States.GameStates.PAUSE;
                    Main.game.ui.uiState = States.UIStates.PAUSE;
                    Main.game.ui.subUIState = "";
                    Main.game.ui.commandNumber = 0;
                }
            }
        }
    }
    public void pauseState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            if (Objects.equals(Main.game.ui.subUIState, "Main Pause")) {
                Main.game.gameState = States.GameStates.PLAY;
                Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
            } else {
                Main.game.ui.subUIState = "Main Pause";
                Main.game.ui.commandNumber = 0;
            }
        }

        maxCommandNumber = switch (Main.game.ui.subUIState) {
            case "Main Pause" -> 3;
            case "Settings Main" -> 5;
            case "Confirm" -> 1;
            default -> 0;
        };

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            Main.game.ui.commandNumber--;
            Sound.playSFX("Cursor");

            if (Main.game.ui.commandNumber < 0) Main.game.ui.commandNumber = maxCommandNumber;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            Main.game.ui.commandNumber++;
            Sound.playSFX("Cursor");

            if (Main.game.ui.commandNumber > maxCommandNumber) Main.game.ui.commandNumber = 0;
        }
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) spacePressed = true;

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (Main.game.ui.subUIState.equals("Settings Main")) {
                if (Main.game.ui.commandNumber == 0 && Sound.music.volumeScale > 0) {
                    Sound.music.volumeScale--;
                    Sound.music.checkVolume();
                    Sound.playSFX("Cursor");
                }
                if (Main.game.ui.commandNumber == 1 && Sound.sfx.volumeScale > 0) {
                    Sound.sfx.volumeScale--;
                    Sound.playSFX("Cursor");
                }
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (Main.game.ui.subUIState.equals("Settings Main")) {
                if (Main.game.ui.commandNumber == 0 && Sound.music.volumeScale < 5) {
                    Sound.music.volumeScale++;
                    Sound.music.checkVolume();
                    Sound.playSFX("Cursor");
                }
                if (Main.game.ui.commandNumber == 1 && Sound.sfx.volumeScale < 5) {
                    Sound.sfx.volumeScale++;
                    Sound.playSFX("Cursor");
                }
            }
        }
    }
    public void dialogueState(int code) {
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ESCAPE) {
            Main.game.player.cancelAttack();
            Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
        }
    }
    public void characterState(int code) {
        if (code == KeyEvent.VK_E || code == KeyEvent.VK_ESCAPE) {
            Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            Main.game.player.selectItem();
        }
        playerInventory(code);
    }
    public void playerInventory(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (Main.game.ui.playerSlotRow != 0) {
                Main.game.ui.playerSlotRow--;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (Main.game.ui.playerSlotCol != 0) {
                Main.game.ui.playerSlotCol--;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (Main.game.ui.playerSlotRow != 4) {
                Main.game.ui.playerSlotRow++;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (Main.game.ui.playerSlotCol != 4) {
                Main.game.ui.playerSlotCol++;
                Sound.playSFX("Cursor");
            }
        }
    }
    public void mobInventory(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (Main.game.ui.mobSlotRow != 0) {
                Main.game.ui.mobSlotRow--;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (Main.game.ui.mobSlotCol != 0) {
                Main.game.ui.mobSlotCol--;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (Main.game.ui.mobSlotRow != 4) {
                Main.game.ui.mobSlotRow++;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (Main.game.ui.mobSlotCol != 4) {
                Main.game.ui.mobSlotCol++;
                Sound.playSFX("Cursor");
            }
        }
    }

    public void gameOverState(int code) {
        if (Main.game.difficulty.equals("Easy")) {
            maxCommandNumber = 2;
        } else {
            maxCommandNumber = 1;
        }

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            Main.game.ui.commandNumber--;
            if (Main.game.ui.commandNumber < 0) {
                Main.game.ui.commandNumber = maxCommandNumber;
            }
            Sound.playSFX("Cursor");
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            Main.game.ui.commandNumber++;
            if (Main.game.ui.commandNumber > maxCommandNumber) {
                Main.game.ui.commandNumber = 0;
            }
            Sound.playSFX("Cursor");
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            if (Main.game.ui.commandNumber == 0) {
                Main.game.gameState = States.GameStates.PLAY;
                Main.game.restart(true);
                Sound.playMapMusic();
            } else if (Main.game.ui.commandNumber == maxCommandNumber) {
                Main.game.gameState = States.GameStates.TITLE;
                Main.game.ui.subUIState = "Main Title";
                Main.game.restart(true);
                Sound.playMusic("Tech Geek");
            } else if (Main.game.ui.commandNumber == 1) {
                Main.game.gameState = States.GameStates.PLAY;
                Main.game.respawn();
                Sound.playMapMusic();
            }
        }
    }
    public void exceptionState(int code) {
        if (Main.game.exceptionState == States.ExceptionStates.IGNORABLE_QUITABLE) maxCommandNumber = 2;
        else maxCommandNumber = 1;

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            Main.game.ui.commandNumber--;
            if (Main.game.ui.commandNumber < 0) Main.game.ui.commandNumber = maxCommandNumber;
            Sound.playSFX("Cursor");
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            Main.game.ui.commandNumber++;
            if (Main.game.ui.commandNumber > maxCommandNumber) Main.game.ui.commandNumber = 0;
            Sound.playSFX("Cursor");
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) spacePressed = true;
    }
    public void tradeState(int code) {
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) spacePressed = true;

        if (Objects.equals(Main.game.ui.subUIState, "Select")) {
            if (code == KeyEvent.VK_ESCAPE) {
                Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
                Main.game.ui.commandNumber = 0;
            }

            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                Main.game.ui.commandNumber--;
                if (Main.game.ui.commandNumber < 0) Main.game.ui.commandNumber = 2;

                Sound.playSFX("Cursor");
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                Main.game.ui.commandNumber++;
                if (Main.game.ui.commandNumber > 2) Main.game.ui.commandNumber = 0;
                Sound.playSFX("Cursor");
            }
        } else if (Objects.equals(Main.game.ui.subUIState, "Buy")) {
            mobInventory(code);

            if (code == KeyEvent.VK_ESCAPE) Main.game.ui.subUIState = "Select";
        } else if (Objects.equals(Main.game.ui.subUIState, "Sell")) {
            playerInventory(code);

            if (code == KeyEvent.VK_ESCAPE) Main.game.ui.subUIState = "Select";
        }
    }
    public void mapState(int code) {
        if (code == KeyEvent.VK_ESCAPE) Main.game.ui.uiState = States.UIStates.CHARACTER;
    }

    public void saveState(int code) {
        maxCommandNumber = 3;

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            Main.game.ui.commandNumber--;
            if (Main.game.ui.commandNumber < 0) Main.game.ui.commandNumber = maxCommandNumber;
            Sound.playSFX("Cursor");
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            Main.game.ui.commandNumber++;
            if (Main.game.ui.commandNumber > maxCommandNumber) Main.game.ui.commandNumber = 0;
            Sound.playSFX("Cursor");
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) spacePressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_F3 -> {
                f3Pressed = false;
                if (debug) {
                    Main.game.debug = !Main.game.debug;
                    Main.game.ui.addMiniNotification("Debug: " + Main.game.debug);
                    debug = false;
                }
            }
        }
    }
}
