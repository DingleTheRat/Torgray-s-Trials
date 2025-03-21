package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    GamePanel gamePanel;
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, interactKeyPressed;
    public int maxCommandNumber = 0;
    // Debug
    public boolean debug = false;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (gamePanel.gameState == States.STATE_TILE) {titleState(code);}
        else if (gamePanel.gameState == States.STATE_PLAY) {playState(code);}
        else if (gamePanel.gameState == States.STATE_PAUSE) {pauseState(code);}
        else if (gamePanel.gameState == States.STATE_DIALOGUE) {dialogueState(code); playState(code);}
        else if (gamePanel.gameState == States.STATE_CHARACTER) {characterState(code);}
        else if (gamePanel.gameState == States.STATE_GAME_OVER) {gameOverState(code);}

        if (gamePanel.BRendering) {
            if (code == KeyEvent.VK_U) {
                gamePanel.BRendering = false;
            }
        }
    }
    public void titleState(int code) {
        if (gamePanel.ui.titleScreenState == States.TITLE_STATE_MAIN) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gamePanel.ui.commandNumber--;
                gamePanel.playSound(8);
                if (gamePanel.ui.commandNumber < 0) {
                    gamePanel.ui.commandNumber = 2;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gamePanel.ui.commandNumber++;
                gamePanel.playSound(8);
                if (gamePanel.ui.commandNumber > 2) {
                    gamePanel.ui.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (gamePanel.ui.commandNumber == 0) {
                    gamePanel.ui.titleScreenState = States.TITLE_STATE_MODES;
                }
                if (gamePanel.ui.commandNumber == 1) {
                    // For later
                }
                if (gamePanel.ui.commandNumber == 2) {
                    System.exit(0);
                }
            }
        } else if (gamePanel.ui.titleScreenState == States.TITLE_STATE_MODES) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gamePanel.ui.commandNumber--;
                gamePanel.playSound(8);
                if (gamePanel.ui.commandNumber < 0) {
                    gamePanel.ui.commandNumber = 3;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gamePanel.ui.commandNumber++;
                gamePanel.playSound(8);
                if (gamePanel.ui.commandNumber > 3) {
                    gamePanel.ui.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (gamePanel.ui.commandNumber == 0) {
                    gamePanel.gameState = States.STATE_PLAY;
                    gamePanel.gameMode = "Easy";
                    gamePanel.stopMusic();
                    gamePanel.playMusic(0);
                    System.out.println("Imagine Picking Easy");

                    // Modified Stats
                    gamePanel.player.strength = 2;
                    gamePanel.player.dexterity = 2;
                    gamePanel.player.nextLevelExp = 4;
                    gamePanel.player.attack = gamePanel.player.getAttack();
                    gamePanel.player.defence = gamePanel.player.getDefence();
                }
                if (gamePanel.ui.commandNumber == 1) {
                    gamePanel.gameState = States.STATE_PLAY;
                    gamePanel.gameMode = "Medium";
                    gamePanel.stopMusic();
                    gamePanel.playMusic(0);
                    System.out.println("Kinda a mid game mode lol");

                    // No modified stats since Medium is the default
                }
                if (gamePanel.ui.commandNumber == 2) {
                    gamePanel.gameState = States.STATE_PLAY;
                    gamePanel.gameMode = "Hard";
                    gamePanel.stopMusic();
                    gamePanel.playMusic(0);
                    System.out.println("You really think you are \"hardcore\"?");

                    // Modified Stats
                    gamePanel.player.dexterity = 0;
                    gamePanel.player.nextLevelExp = 6;
                    gamePanel.player.defence = gamePanel.player.getDefence();
                }
                if (gamePanel.ui.commandNumber == 3) {
                    gamePanel.ui.titleScreenState = States.TITLE_STATE_MAIN;
                }
            }
        }
    }
    public void playState(int code) {
        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            gamePanel.gameState = States.STATE_PAUSE;
        }
        if (code == KeyEvent.VK_E) {
            interactKeyPressed = true;
        }
        if (code == KeyEvent.VK_E) {
            interactKeyPressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }

        // Debug
        if (code == KeyEvent.VK_F3) {
            if (!debug) {
                System.out.println("Debugging Enabled");
                debug = true;
            } else if (debug) {
                System.out.println("Debugging Disabled");
                debug = false;
            }
        }
    }
    public void pauseState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            gamePanel.gameState = States.STATE_PLAY;
            gamePanel.ui.subState = States.PAUSE_STATE_MAIN;
            gamePanel.ui.commandNumber = 0;
        }

        switch (gamePanel.ui.subState) {
            case States.PAUSE_STATE_MAIN: maxCommandNumber = 2; break;
            case States.PAUSE_SETTINGS_MAIN: maxCommandNumber = 5; break;
            case States.PAUSE_SETTINGS_CONFIRM: maxCommandNumber = 1; break;
        }

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gamePanel.ui.commandNumber--;
            gamePanel.playSound(8);

            if (gamePanel.ui.commandNumber < 0) {
                gamePanel.ui.commandNumber = maxCommandNumber;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gamePanel.ui.commandNumber++;
            gamePanel.playSound(8);

            if (gamePanel.ui.commandNumber > maxCommandNumber) {
                gamePanel.ui.commandNumber = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (gamePanel.ui.subState == States.PAUSE_SETTINGS_MAIN) {
                if (gamePanel.ui.commandNumber == 2 && gamePanel.music.volumeScale > 0) {
                    gamePanel.music.volumeScale--;
                    gamePanel.music.checkVolume();
                    gamePanel.playSound(8);
                }
                if (gamePanel.ui.commandNumber == 3 && gamePanel.sound.volumeScale > 0) {
                    gamePanel.sound.volumeScale--;
                    gamePanel.playSound(8);
                }
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (gamePanel.ui.subState == States.PAUSE_SETTINGS_MAIN) {
                if (gamePanel.ui.commandNumber == 2 && gamePanel.music.volumeScale < 5) {
                    gamePanel.music.volumeScale++;
                    gamePanel.music.checkVolume();
                    gamePanel.playSound(8);
                }
                if (gamePanel.ui.commandNumber == 3 && gamePanel.sound.volumeScale < 5) {
                    gamePanel.sound.volumeScale++;
                    gamePanel.playSound(8);
                }
            }
        }
    }
    public void dialogueState(int code) {
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ESCAPE) {
            gamePanel.player.attackCanceled = true;
            gamePanel.gameState = States.STATE_PLAY;
        }
    }
    public void characterState(int code) {
        if (code == KeyEvent.VK_E || code == KeyEvent.VK_ESCAPE) {
            gamePanel.gameState = States.STATE_PLAY;
        }
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (gamePanel.ui.slotRow != 0) {
                gamePanel.ui.slotRow --;
                gamePanel.playSound(8);
            }
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (gamePanel.ui.slotCol != 0) {
                gamePanel.ui.slotCol --;
                gamePanel.playSound(8);
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (gamePanel.ui.slotRow != 4) {
                gamePanel.ui.slotRow ++;
                gamePanel.playSound(8);
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (gamePanel.ui.slotCol != 4) {
                gamePanel.ui.slotCol ++;
                gamePanel.playSound(8);
            }
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            gamePanel.player.selectItem();
        }
    }
    public void gameOverState(int code) {
        maxCommandNumber = 1;
        if (gamePanel.gameMode.equals("Easy")) {
            maxCommandNumber = 2;
        }

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            gamePanel.ui.commandNumber--;
            if (gamePanel.ui.commandNumber < 0) {
                gamePanel.ui.commandNumber = maxCommandNumber;
            }
            gamePanel.playSound(8);
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            gamePanel.ui.commandNumber++;
            if (gamePanel.ui.commandNumber > maxCommandNumber) {
                gamePanel.ui.commandNumber = 0;
            }
            gamePanel.playSound(8);
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            if (gamePanel.ui.commandNumber == 0) {
                gamePanel.gameState = States.STATE_PLAY;
                gamePanel.restart();
            } else if (gamePanel.ui.commandNumber == maxCommandNumber) {
                gamePanel.gameState = States.STATE_TILE;
                gamePanel.ui.titleScreenState = States.TITLE_STATE_MAIN;
                gamePanel.restart();
            } else if (gamePanel.ui.commandNumber == 1) {
                gamePanel.gameState = States.STATE_PLAY;
                gamePanel.respawn();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
}
