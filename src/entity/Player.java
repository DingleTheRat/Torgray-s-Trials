package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getImage();
    }

    public void getImage() {
        up1 = registerEntitySprite("/player/ghost_up_1");
        up2 = registerEntitySprite("/player/ghost_up_2");
        up3 = registerEntitySprite("/player/ghost_up_3");

        down1 = registerEntitySprite("/player/ghost_down_1");
        down2 = registerEntitySprite("/player/ghost_down_2");
        down3 = registerEntitySprite("/player/ghost_down_3");

        left1 = registerEntitySprite("/player/ghost_left_1");
        left2 = registerEntitySprite("/player/ghost_left_2");
        left3 = registerEntitySprite("/player/ghost_left_3");

        right1 = registerEntitySprite("/player/ghost_right_1");
        right2 = registerEntitySprite("/player/ghost_right_2");
        right3 = registerEntitySprite("/player/ghost_right_3");
    }
    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // Player Status
        maxHealth = 12;
        health = maxHealth;
    }
    public void update() {
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // Check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // Check OBJ collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check NPC collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // Check Event
            gp.eHandler.checkEvent();

            // Check Mob Collision
            int mobIndex = gp.cChecker.checkEntity(this, gp.mob);
            contactMob(mobIndex);

            gp.keyH.interactKeyPressed = false;

            if (!collisionOn) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            spriteCounter ++;
            if (spriteCounter > 10) {
                if (spriteNumber == 1) {
                    spriteNumber = 2;
                } else if (spriteNumber == 2) {
                    spriteNumber = 3;
                } else if (spriteNumber == 3) {
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }
        } else {
            spriteCounter++;

            if (spriteCounter == 20) {
                spriteNumber = 1;
                spriteCounter = 0;
            }
        }
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void pickUpObject(int i) {if (i != 999) {}}
    public void interactNPC(int i) {
        if (i != 999) {
            if (gp.keyH.interactKeyPressed) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            } else {
                gp.ui.interactable = true;
            }
        }
    }
    public void contactMob(int i) {
        if (i != 999) {
            if (!invincible) {
                health -= 2;
                invincible = true;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up": if (spriteNumber == 1) {image = up1;} else if (spriteNumber == 2) {image = up2;} else if (spriteNumber == 3) {image = up3;} break;
            case "down": if (spriteNumber == 1) {image = down1;} else if (spriteNumber == 2) {image = down2;} else if (spriteNumber == 3) {image = down3;} break;
            case "left": if (spriteNumber == 1) {image = left1;} else if (spriteNumber == 2) {image = left2;} else if (spriteNumber == 3) {image = left3;} break;
            case "right": if (spriteNumber == 1) {image = right1;} else if (spriteNumber == 2) {image = right2;} else if (spriteNumber == 3) {image = right3;} break;
        }
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, screenX, screenY, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
