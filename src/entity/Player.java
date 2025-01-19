package entity;

import main.GamePanel;
import main.KeyHandler;
import main.States;
import object.OBJ_Shield_Iron;
import object.OBJ_Sword_Iron;
import object.OBJ_Torgray_Soup;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity{
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 25;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Solid Area
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getImage();
        getAttackImage();
        setItems();
    }

    public void getImage() {
        up1 = registerEntitySprite("/player/walking/ghost_up_1", gp.tileSize, gp.tileSize);
        up2 = registerEntitySprite("/player/walking/ghost_up_2", gp.tileSize, gp.tileSize);
        up3 = registerEntitySprite("/player/walking/ghost_up_3", gp.tileSize, gp.tileSize);

        down1 = registerEntitySprite("/player/walking/ghost_down_1", gp.tileSize, gp.tileSize);
        down2 = registerEntitySprite("/player/walking/ghost_down_2", gp.tileSize, gp.tileSize);
        down3 = registerEntitySprite("/player/walking/ghost_down_3", gp.tileSize, gp.tileSize);

        left1 = registerEntitySprite("/player/walking/ghost_left_1", gp.tileSize, gp.tileSize);
        left2 = registerEntitySprite("/player/walking/ghost_left_2", gp.tileSize, gp.tileSize);
        left3 = registerEntitySprite("/player/walking/ghost_left_3", gp.tileSize, gp.tileSize);

        right1 = registerEntitySprite("/player/walking/ghost_right_1", gp.tileSize, gp.tileSize);
        right2 = registerEntitySprite("/player/walking/ghost_right_2", gp.tileSize, gp.tileSize);
        right3 = registerEntitySprite("/player/walking/ghost_right_3", gp.tileSize, gp.tileSize);
    }
    public void getAttackImage() {
        if (currentWeapon.tags.contains(EntityTags.TAG_AMETHIST)) {
            attackUp = registerEntitySprite("/player/attack/torgray_amethist_attack_up", gp.tileSize, gp.tileSize * 2);
            attackDown = registerEntitySprite("/player/attack/torgray_amethist_attack_down", gp.tileSize, gp.tileSize * 2);
            attackLeft = registerEntitySprite("/player/attack/torgray_amethist_attack_left", gp.tileSize * 2, gp.tileSize);
            attackRight = registerEntitySprite("/player/attack/torgray_amethist_attack_right", gp.tileSize * 2, gp.tileSize);
        } else if (currentWeapon.tags.contains(EntityTags.TAG_IRON)) {
            attackUp = registerEntitySprite("/player/attack/torgray_iron_attack_up", gp.tileSize, gp.tileSize * 2);
            attackDown = registerEntitySprite("/player/attack/torgray_iron_attack_down", gp.tileSize, gp.tileSize * 2);
            attackLeft = registerEntitySprite("/player/attack/torgray_iron_attack_left", gp.tileSize * 2, gp.tileSize);
            attackRight = registerEntitySprite("/player/attack/torgray_iron_attack_right", gp.tileSize * 2, gp.tileSize);
        }
    }
    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // Player Status
        level = 1;
        maxHealth = 12;
        health = maxHealth;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coins = 0;
        currentWeapon = new OBJ_Sword_Iron(gp);
        currentShield = new OBJ_Shield_Iron(gp);
        attack = getAttack();
        defence = getDefence();
    }
    public void setItems() {
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Torgray_Soup(gp));
    }
    public int getAttack() {
        attackArea = currentWeapon.attackArea;
        return attack = strength * currentWeapon.attackValue;
    }
    public int getDefence() {
        return defence = dexterity * currentShield.defenceValue;
    }
    public void update() {
        if (attacking) {
            attack();
        } else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.spacePressed || keyH.interactKeyPressed) {
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

            if (!collisionOn && !keyH.spacePressed && !keyH.interactKeyPressed) {
                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            // Attacking
            if (keyH.spacePressed && !attackCanceled) {
                attacking = true;
                spriteCounter = 0;
            }

            // Inventory
            if (keyH.interactKeyPressed && !attackCanceled) {
                gp.gameState = States.STATE_CHARACTER;
            }

            attackCanceled = false;
            gp.keyH.spacePressed = false;
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
    public void pickUpObject(int i) {
        if (i != 999) {
            if (gp.obj[i].tags.contains(EntityTags.TAG_INTERACTABLE)) {
                for (int j = 0; j < inventory.size(); j++) {
                    if (inventory.get(j).name.equals("Key")) {
                        gp.obj[i] = null;
                        inventory.remove(j);
                        gp.ui.addMessage("-1 Key");
                        gp.playSE(3);
                        break;
                    }
                }
            } else {
                String text;
                if (inventory.size() != maxInventorySize) {
                    inventory.add(gp.obj[i]);
                    gp.playSE(1);
                    text = "+1 " + gp.obj[i].name;
                }
                else {
                    text = "Inventory Full";
                }
                gp.ui.addMessage(text);
                gp.obj[i] = null;
            }
        }
    }
    public void interactNPC(int i) {
        if (gp.keyH.interactKeyPressed) {
            if (i != 999) {
                attackCanceled = true;
                gp.gameState = States.STATE_DIALOGUE;
                gp.npc[i].speak();
            }
        }
    }
    public void contactMob(int i) {
        if (i != 999) {
            if (!invincible) {
                gp.playSE(7);

                int damage = gp.mob[i].attack - defence;
                if (damage < 0) {
                    damage = 0;
                }
                health -= damage;
                invincible = true;
            }
        }
    }

    public void damageMob(int i) {
        if (i != 999) {
            if (!gp.mob[i].invincible) {
                gp.playSE(6);

                int damage = attack - gp.mob[i].defence;
                if (damage < 0) {
                    damage = 0;
                }
                gp.mob[i].health -= damage;

                gp.mob[i].invincible = true;
                gp.mob[i].damageReaction();

                if (gp.mob[i].health <= 0) {
                    gp.mob[i].dying = true;
                    gp.ui.addMessage("Killed " + gp.mob[i].name);
                    gp.ui.addMessage("+" + gp.mob[i].exp + " exp");
                    exp += gp.mob[i].exp;
                    checkLevelUp();
                }
            }
        }
    }
    public void checkLevelUp() {
        if (exp >= nextLevelExp) {
            level++;
            exp = 0;
            nextLevelExp = nextLevelExp * 2;
            health += level * 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defence = getDefence();
            gp.ui.addMessage("Level Up!");

            if (health >= maxHealth) {
                health = maxHealth;
            }
        }
    }
    public void selectItem() {
        int itemIndex = gp.ui.getItemIndex();

        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.type == EntityTypes.TYPE_SWORD) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getAttackImage();
            }
            if (selectedItem.type == EntityTypes.TYPE_SHIELD) {
                currentShield = selectedItem;
                defence = getDefence();
            }
            if (selectedItem.type == EntityTypes.TYPE_CONSUMABLE) {
                selectedItem.use(this);
                inventory.remove(itemIndex);
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
