package net.dinglezz.torgrays_trials.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    Game game;
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, interactKeyPressed, f3Pressed;
    public int maxCommandNumber = 0;
    // Debug
    public boolean debug = false;

    public InputHandler(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (game.gameState) {
            case TITLE -> titleState(code);
            case PLAY -> playState(code);
            case PAUSE -> pauseState(code);
            case DIALOGUE -> {dialogueState(code); playState(code);}
            case CHARACTER -> characterState(code);
            case GAME_OVER -> gameOverState(code);
            case EXCEPTION -> exceptionState(code);
            case TRADE -> tradeState(code);
            case MAP -> mapState(code);
        }

        // F3 Stuff
        if (f3Pressed && code == KeyEvent.VK_P) {
            game.debugPathfinding = !game.debugPathfinding;
            game.ui.addMiniNotification("Debug Pathfinding: " + game.debugPathfinding);
            debug = false;
        } else if (f3Pressed && code == KeyEvent.VK_R) {
            if (game.BRendering) {
                game.BRendering = false;
                game.ui.addMiniNotification("BRendering: " + game.BRendering);
            }
        } else if (f3Pressed && code == KeyEvent.VK_B) {
            game.debugHitBoxes = !game.debugHitBoxes;
            game.ui.addMiniNotification("Debug Hit Boxes: " + game.debugHitBoxes);
            debug = false;
        } else if (code == KeyEvent.VK_F3) {
            f3Pressed = true;
            debug = true;
        }
    }
    public void titleState(int code) {
        if (game.ui.subState == States.UIStates.TITLE_STATE_MAIN) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                game.ui.commandNumber--;
                Sound.playSFX("Cursor");
                if (game.ui.commandNumber < 0) {
                    game.ui.commandNumber = 2;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                game.ui.commandNumber++;
                Sound.playSFX("Cursor");
                if (game.ui.commandNumber > 2) {
                    game.ui.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (game.ui.commandNumber == 0) {
                    game.ui.subState = States.UIStates.TITLE_STATE_MODES;
                    game.ui.commandNumber = 1;
                }
                if (game.ui.commandNumber == 1) {
                    // For later
                }
                if (game.ui.commandNumber == 2) {
                    System.exit(0);
                }
            }
        } else if (game.ui.subState == States.UIStates.TITLE_STATE_MODES) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                game.ui.commandNumber--;
                Sound.playSFX("Cursor");
                if (game.ui.commandNumber < 0) {
                    game.ui.commandNumber = 3;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                game.ui.commandNumber++;
                Sound.playSFX("Cursor");
                if (game.ui.commandNumber > 3) {
                    game.ui.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (game.ui.commandNumber == 0) {
                    game.gameState = States.GameStates.PLAY;
                    game.gameMode = "Easy";
                    Sound.playMapMusic();
                    System.out.println("Imagine Picking Easy");

                    // Modified Stats
                    game.player.strength = 2;
                    game.player.dexterity = 2;
                    game.player.nextLevelExp = 4;
                    game.player.attack = game.player.getAttack();
                    game.player.defence = game.player.getDefence();

                    // Modified Darkness State Stuff
                    game.environmentManager.lighting.nightLength = 18000;
                    game.environmentManager.lighting.gloomLength = 9000;

                    game.environmentManager.lighting.gloomChance = 35;
                    game.environmentManager.lighting.lightGloomChance = 50;
                    game.environmentManager.lighting.darkGloomChance = 15;
                }
                if (game.ui.commandNumber == 1) {
                    game.gameState = States.GameStates.PLAY;
                    game.gameMode = "Medium";
                    Sound.playMapMusic();
                    System.out.println("Kinda a mid game mode lol");

                    // No modified stats since Medium is the default
                }
                if (game.ui.commandNumber == 2) {
                    game.gameState = States.GameStates.PLAY;
                    game.gameMode = "Hard";
                    Sound.playMapMusic();
                    System.out.println("You really think you are \"hardcore\"?");

                    // Modified Stats
                    game.player.dexterity = 0;
                    game.player.nextLevelExp = 6;
                    game.player.defence = game.player.getDefence();

                    // Modified State Stuff
                    game.environmentManager.lighting.nightLength = 7200;
                    game.environmentManager.lighting.gloomLength = 144000;

                    game.environmentManager.lighting.gloomChance = 35;
                    game.environmentManager.lighting.lightGloomChance = 10;
                    game.environmentManager.lighting.darkGloomChance = 55;
                }
                if (game.ui.commandNumber == 3) {
                    game.ui.subState = States.UIStates.TITLE_STATE_MAIN;
                    game.ui.commandNumber = 0;
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
            case KeyEvent.VK_ESCAPE -> {game.gameState = States.GameStates.PAUSE; game.ui.subState = States.UIStates.PAUSE_STATE_MAIN; game.ui.commandNumber = 0;}
        }
    }
    public void pauseState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            if (game.ui.subState == States.UIStates.PAUSE_STATE_MAIN) {
                game.gameState = States.GameStates.PLAY;
            }
            game.ui.subState = States.UIStates.PAUSE_STATE_MAIN;
            game.ui.commandNumber = 0;
        }

        maxCommandNumber = switch (game.ui.subState) {
            case PAUSE_STATE_MAIN -> 3;
            case PAUSE_STATE_SETTINGS_MAIN -> 5;
            case PAUSE_STATE_CONFIRM -> 1;
            default -> 0;
        };

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            game.ui.commandNumber--;
            Sound.playSFX("Cursor");

            if (game.ui.commandNumber < 0) {
                game.ui.commandNumber = maxCommandNumber;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            game.ui.commandNumber++;
            Sound.playSFX("Cursor");

            if (game.ui.commandNumber > maxCommandNumber) {
                game.ui.commandNumber = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (game.ui.subState == States.UIStates.PAUSE_STATE_SETTINGS_MAIN) {
                if (game.ui.commandNumber == 0 && Sound.music.volumeScale > 0) {
                    Sound.music.volumeScale--;
                    Sound.music.checkVolume();
                    Sound.playSFX("Cursor");
                }
                if (game.ui.commandNumber == 1 && Sound.sfx.volumeScale > 0) {
                    Sound.sfx.volumeScale--;
                    Sound.playSFX("Cursor");
                }
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (game.ui.subState == States.UIStates.PAUSE_STATE_SETTINGS_MAIN) {
                if (game.ui.commandNumber == 0 && Sound.music.volumeScale < 5) {
                    Sound.music.volumeScale++;
                    Sound.music.checkVolume();
                    Sound.playSFX("Cursor");
                }
                if (game.ui.commandNumber == 1 && Sound.sfx.volumeScale < 5) {
                    Sound.sfx.volumeScale++;
                    Sound.playSFX("Cursor");
                }
            }
        }
    }
    public void dialogueState(int code) {
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ESCAPE) {
            game.player.attackCanceled = true;
            game.gameState = States.GameStates.PLAY;
        }
    }
    public void characterState(int code) {
        if (code == KeyEvent.VK_E || code == KeyEvent.VK_ESCAPE) {
            game.gameState = States.GameStates.PLAY;
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            game.player.selectItem();
        }
        playerInventory(code);
    }
    public void playerInventory(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (game.ui.playerSlotRow != 0) {
                game.ui.playerSlotRow--;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (game.ui.playerSlotCol != 0) {
                game.ui.playerSlotCol--;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (game.ui.playerSlotRow != 4) {
                game.ui.playerSlotRow++;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (game.ui.playerSlotCol != 4) {
                game.ui.playerSlotCol++;
                Sound.playSFX("Cursor");
            }
        }
    }
    public void entityInventory(int code) {
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (game.ui.entitySlotRow != 0) {
                game.ui.entitySlotRow--;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (game.ui.entitySlotCol != 0) {
                game.ui.entitySlotCol--;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (game.ui.entitySlotRow != 4) {
                game.ui.entitySlotRow++;
                Sound.playSFX("Cursor");
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (game.ui.entitySlotCol != 4) {
                game.ui.entitySlotCol++;
                Sound.playSFX("Cursor");
            }
        }
    }
    
    public void gameOverState(int code) {
        if (game.gameMode.equals("Easy")) {
            maxCommandNumber = 2;
        } else {
            maxCommandNumber = 1;
        }

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            game.ui.commandNumber--;
            if (game.ui.commandNumber < 0) {
                game.ui.commandNumber = maxCommandNumber;
            }
            Sound.playSFX("Cursor");
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            game.ui.commandNumber++;
            if (game.ui.commandNumber > maxCommandNumber) {
                game.ui.commandNumber = 0;
            }
            Sound.playSFX("Cursor");
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            if (game.ui.commandNumber == 0) {
                game.gameState = States.GameStates.PLAY;
                game.restart();
                Sound.playMapMusic();
            } else if (game.ui.commandNumber == maxCommandNumber) {
                game.gameState = States.GameStates.TITLE;
                game.ui.subState = States.UIStates.TITLE_STATE_MAIN;
                game.restart();
                Sound.playMusic("Tech Geek");
            } else if (game.ui.commandNumber == 1) {
                game.gameState = States.GameStates.PLAY;
                game.respawn();
                Sound.playMapMusic();
            }
        }
    }
    public void exceptionState(int code) {
        if (game.exceptionState == States.ExceptionStates.IGNORABLE_QUITABLE) {
            System.err.println("E");
            maxCommandNumber = 2;
        } else {
            maxCommandNumber = 1;
        }

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            game.ui.commandNumber--;
            if (game.ui.commandNumber < 0) {
                game.ui.commandNumber = maxCommandNumber;
            }
            Sound.playSFX("Cursor");
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            game.ui.commandNumber++;
            if (game.ui.commandNumber > maxCommandNumber) {
                game.ui.commandNumber = 0;
            }
            Sound.playSFX("Cursor");
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            spacePressed = true;
        }
    }
    public void tradeState(int code) {
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            spacePressed = true;
        }

        if (game.ui.subState == States.UIStates.TRADE_STATE_SELECT) {
            if (code == KeyEvent.VK_ESCAPE) {
                game.gameState = States.GameStates.PLAY;
                game.ui.commandNumber = 0;
            }

            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                game.ui.commandNumber--;
                if (game.ui.commandNumber < 0) {
                    game.ui.commandNumber = 2;
                }
                Sound.playSFX("Cursor");
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                game.ui.commandNumber++;
                if (game.ui.commandNumber > 2) {
                    game.ui.commandNumber = 0;
                }
                Sound.playSFX("Cursor");
            }
        } else if (game.ui.subState == States.UIStates.TRADE_STATE_BUY) {
            entityInventory(code);

            if (code == KeyEvent.VK_ESCAPE) {
                game.ui.subState = States.UIStates.TRADE_STATE_SELECT;
            }
        } else if (game.ui.subState == States.UIStates.TRADE_STATE_SELL) {
            playerInventory(code);

            if (code == KeyEvent.VK_ESCAPE) {
                game.ui.subState = States.UIStates.TRADE_STATE_SELECT;
            }
        }
    }
    public void mapState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            game.gameState = States.GameStates.CHARACTER;
        }
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
                    game.debug = !game.debug;
                    game.ui.addMiniNotification("Debug: " + game.debug);
                    debug = false;
                }
            }
        }
    }
}
