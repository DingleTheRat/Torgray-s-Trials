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

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getImage();
        getAttackImage();
    }

    public void getImage() {
        up1 = registerEntitySprite("/player/ghost_up_1", gp.tileSize, gp.tileSize);
        up2 = registerEntitySprite("/player/ghost_up_2", gp.tileSize, gp.tileSize);
        up3 = registerEntitySprite("/player/ghost_up_3", gp.tileSize, gp.tileSize);

        down1 = registerEntitySprite("/player/ghost_down_1", gp.tileSize, gp.tileSize);
        down2 = registerEntitySprite("/player/ghost_down_2", gp.tileSize, gp.tileSize);
        down3 = registerEntitySprite("/player/ghost_down_3", gp.tileSize, gp.tileSize);

        left1 = registerEntitySprite("/player/ghost_left_1", gp.tileSize, gp.tileSize);
        left2 = registerEntitySprite("/player/ghost_left_2", gp.tileSize, gp.tileSize);
        left3 = registerEntitySprite("/player/ghost_left_3", gp.tileSize, gp.tileSize);

        right1 = registerEntitySprite("/player/ghost_right_1", gp.tileSize, gp.tileSize);
        right2 = registerEntitySprite("/player/ghost_right_2", gp.tileSize, gp.tileSize);
        right3 = registerEntitySprite("/player/ghost_right_3", gp.tileSize, gp.tileSize);
    }
    public void getAttackImage() {
        attackUp = registerEntitySprite("/player/torgray_attack_up", gp.tileSize, gp.tileSize * 2);
        attackDown = registerEntitySprite("/player/torgray_attack_down", gp.tileSize, gp.tileSize * 2);
        attackLeft = registerEntitySprite("/player/torgray_attack_left", gp.tileSize * 2, gp.tileSize);
        attackRight = registerEntitySprite("/player/torgray_attack_right", gp.tileSize * 2, gp.tileSize);
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
        if (attacking) {
            attack();
        } else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.interactKeyPressed) {
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

            if (!collisionOn && !keyH.interactKeyPressed) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            gp.keyH.interactKeyPressed = false;

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

            if (spriteCounter > 20) {
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
    public void attack() {
        spriteCounter ++;

        if (spriteCounter <= 5) {
            spriteNumber = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNumber = 2;

            // Save current worldX, worldY and solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Ajust player's worldX and worldY for attack area
            switch (direction) {
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += attackArea.height; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += attackArea.width; break;
            }
            // Attack Area = Solid Area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check collision with the updates
            int mobIndex = gp.cChecker.checkEntity(this, gp.mob);
            damageMob(mobIndex);

            // Restore original data
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > 25) {
            spriteNumber = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }
    public void pickUpObject(int i) {if (i != 999) {}}
    public void interactNPC(int i) {
        if (gp.keyH.interactKeyPressed) {
            if (i != 999) {
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            } else {
                attacking = true;
            }
        }
    }
    public void contactMob(int i) {
        if (i != 999) {
            if (!invincible) {
                gp.playSE(7);
                health -= 2;
                invincible = true;
            }
        }
    }

    public void damageMob(int i) {
        if (i != 999) {
            if (!gp.mob[i].invincible) {
                gp.playSE(6);
                gp.mob[i].health -= 1;
                gp.mob[i].invincible = true;
                gp.mob[i].damageReaction();

                if (gp.mob[i].health <= 0) {
                    gp.mob[i].dying = true;
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {
            case "up":
                if (!attacking) {
                    if (spriteNumber == 1) {image = up1;}
                    else if (spriteNumber == 2) {image = up2;}
                    else if (spriteNumber == 3) {image = up3;}
                } if (attacking) {
                    tempScreenY = screenY - gp.tileSize;
                    if (spriteNumber == 1) {image = attackUp;}
                    if (spriteNumber == 2) {image = attackUp;}
                    if (spriteNumber == 3) {image = attackUp;}
                }
            break;
            case "down":
                if (!attacking) {
                    if (spriteNumber == 1) {image = down1;}
                    else if (spriteNumber == 2) {image = down2;}
                    else if (spriteNumber == 3) {image = down3;}
                }
                if (attacking) {
                    if (spriteNumber == 1) {image = attackDown;}
                    if (spriteNumber == 2) {image = attackDown;}
                    if (spriteNumber == 3) {image = attackDown;}
                }
            break;
            case "left":
                if (!attacking) {
                    if (spriteNumber == 1) {image = left1;}
                    else if (spriteNumber == 2) {image = left2;}
                    else if (spriteNumber == 3) {image = left3;}
                }
                if (attacking) {
                    tempScreenX = screenX - gp.tileSize;
                    if (spriteNumber == 1) {image = attackLeft;}
                    if (spriteNumber == 2) {image = attackLeft;}
                    if (spriteNumber == 3) {image = attackLeft;}
                }
            break;
            case "right":
                if (!attacking) {
                    if (spriteNumber == 1) {image = right1;}
                    else if (spriteNumber == 2) {image = right2;}
                    else if (spriteNumber == 3) {image = right3;}
                }
                if (attacking) {
                    if (spriteNumber == 1) {image = attackRight;}
                    if (spriteNumber == 2) {image = attackRight;}
                    if (spriteNumber == 3) {image = attackRight;}
                }
            break;
        }
        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
