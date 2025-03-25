package net.dinglezz.torgrays_trials.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    Game game;
    public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, interactKeyPressed;
    public int maxCommandNumber = 0;
    // Debug
    public boolean debug = false;

    public KeyHandler(Game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (game.gameState) {
            case States.STATE_TITLE: titleState(code); break;
            case States.STATE_PLAY: playState(code); break;
            case States.STATE_PAUSE: pauseState(code); break;
            case States.STATE_DIALOGUE: dialogueState(code); playState(code); break;
            case States.STATE_CHARACTER: characterState(code); break;
            case States.STATE_GAME_OVER: gameOverState(code); break;
        }

        if (code == KeyEvent.VK_U && game.BRendering) {
            game.BRendering = false;
        }
    }
    public void titleState(int code) {
        if (game.ui.titleScreenState == States.TITLE_STATE_MAIN) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                game.ui.commandNumber--;
                game.playSound("Cursor");
                if (game.ui.commandNumber < 0) {
                    game.ui.commandNumber = 2;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                game.ui.commandNumber++;
                game.playSound("Cursor");
                if (game.ui.commandNumber > 2) {
                    game.ui.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (game.ui.commandNumber == 0) {
                    game.ui.titleScreenState = States.TITLE_STATE_MODES;
                }
                if (game.ui.commandNumber == 1) {
                    // For later
                }
                if (game.ui.commandNumber == 2) {
                    System.exit(0);
                }
            }
        } else if (game.ui.titleScreenState == States.TITLE_STATE_MODES) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                game.ui.commandNumber--;
                game.playSound("Cursor");
                if (game.ui.commandNumber < 0) {
                    game.ui.commandNumber = 3;
                }
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                game.ui.commandNumber++;
                game.playSound("Cursor");
                if (game.ui.commandNumber > 3) {
                    game.ui.commandNumber = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                if (game.ui.commandNumber == 0) {
                    game.gameState = States.STATE_PLAY;
                    game.gameMode = "Easy";
                    game.stopMusic();
                    game.playMusic("Journey");
                    System.out.println("Imagine Picking Easy");

                    // Modified Stats
                    game.player.strength = 2;
                    game.player.dexterity = 2;
                    game.player.nextLevelExp = 4;
                    game.player.attack = game.player.getAttack();
                    game.player.defence = game.player.getDefence();
                }
                if (game.ui.commandNumber == 1) {
                    game.gameState = States.STATE_PLAY;
                    game.gameMode = "Medium";
                    game.stopMusic();
                    game.playMusic("Journey");
                    System.out.println("Kinda a mid game mode lol");

                    // No modified stats since Medium is the default
                }
                if (game.ui.commandNumber == 2) {
                    game.gameState = States.STATE_PLAY;
                    game.gameMode = "Hard";
                    game.stopMusic();
                    game.playMusic("Journey");
                    System.out.println("You really think you are \"hardcore\"?");

                    // Modified Stats
                    game.player.dexterity = 0;
                    game.player.nextLevelExp = 6;
                    game.player.defence = game.player.getDefence();
                }
                if (game.ui.commandNumber == 3) {
                    game.ui.titleScreenState = States.TITLE_STATE_MAIN;
                }
            }
        }
    }
    public void playState(int code) {
        switch (code) {
            case KeyEvent.VK_W: upPressed = true; break;
            case KeyEvent.VK_A: leftPressed = true; break;
            case KeyEvent.VK_S: downPressed = true; break;
            case KeyEvent.VK_D: rightPressed = true; break;
            case KeyEvent.VK_E: interactKeyPressed = true; break;
            case KeyEvent.VK_SPACE: spacePressed = true; break;
            case KeyEvent.VK_ESCAPE: game.gameState = States.STATE_PAUSE; break;
        }

        // Debug
        if (code == KeyEvent.VK_F3) {
            if (!debug) {
                System.out.println("Debugging Enabled");
                debug = true;
            } else {
                System.out.println("Debugging Disabled");
                debug = false;
            }
        }
    }
    public void pauseState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            if (game.ui.subState == States.PAUSE_STATE_MAIN) {
                game.gameState = States.STATE_PLAY;
            }
            game.ui.subState = States.PAUSE_STATE_MAIN;
            game.ui.commandNumber = 0;
        }

        switch (game.ui.subState) {
            case States.PAUSE_STATE_MAIN: maxCommandNumber = 2; break;
            case States.PAUSE_SETTINGS_MAIN: maxCommandNumber = 5; break;
            case States.PAUSE_SETTINGS_CONFIRM: maxCommandNumber = 1; break;
        }

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            game.ui.commandNumber--;
            game.playSound("Cursor");

            if (game.ui.commandNumber < 0) {
                game.ui.commandNumber = maxCommandNumber;
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            game.ui.commandNumber++;
            game.playSound("Cursor");

            if (game.ui.commandNumber > maxCommandNumber) {
                game.ui.commandNumber = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }

        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (game.ui.subState == States.PAUSE_SETTINGS_MAIN) {
                if (game.ui.commandNumber == 2 && game.music.volumeScale > 0) {
                    game.music.volumeScale--;
                    game.music.checkVolume();
                    game.playSound("Cursor");
                }
                if (game.ui.commandNumber == 3 && game.sound.volumeScale > 0) {
                    game.sound.volumeScale--;
                    game.playSound("Cursor");
                }
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (game.ui.subState == States.PAUSE_SETTINGS_MAIN) {
                if (game.ui.commandNumber == 2 && game.music.volumeScale < 5) {
                    game.music.volumeScale++;
                    game.music.checkVolume();
                    game.playSound("Cursor");
                }
                if (game.ui.commandNumber == 3 && game.sound.volumeScale < 5) {
                    game.sound.volumeScale++;
                    game.playSound("Cursor");
                }
            }
        }
    }
    public void dialogueState(int code) {
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ESCAPE) {
            game.player.attackCanceled = true;
            game.gameState = States.STATE_PLAY;
        }
    }
    public void characterState(int code) {
        if (code == KeyEvent.VK_E || code == KeyEvent.VK_ESCAPE) {
            game.gameState = States.STATE_PLAY;
        }
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            if (game.ui.slotRow != 0) {
                game.ui.slotRow --;
                game.playSound("Cursor");
            }
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            if (game.ui.slotCol != 0) {
                game.ui.slotCol --;
                game.playSound("Cursor");
            }
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            if (game.ui.slotRow != 4) {
                game.ui.slotRow ++;
                game.playSound("Cursor");
            }
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            if (game.ui.slotCol != 4) {
                game.ui.slotCol ++;
                game.playSound("Cursor");
            }
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            game.player.selectItem();
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
            game.playSound("Cursor");
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            game.ui.commandNumber++;
            if (game.ui.commandNumber > maxCommandNumber) {
                game.ui.commandNumber = 0;
            }
            game.playSound("Cursor");
        }
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            if (game.ui.commandNumber == 0) {
                game.gameState = States.STATE_PLAY;
                game.restart();
                game.playMusic("Journey");
            } else if (game.ui.commandNumber == maxCommandNumber) {
                game.gameState = States.STATE_TITLE;
                game.ui.titleScreenState = States.TITLE_STATE_MAIN;
                game.restart();
                game.playMusic("Tech Geek");
            } else if (game.ui.commandNumber == 1) {
                game.gameState = States.STATE_PLAY;
                game.respawn();
                game.playMusic("Journey");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W: upPressed = false; break;
            case KeyEvent.VK_A: leftPressed = false; break;
            case KeyEvent.VK_S: downPressed = false; break;
            case KeyEvent.VK_D: rightPressed = false; break;
        }
    }
}
