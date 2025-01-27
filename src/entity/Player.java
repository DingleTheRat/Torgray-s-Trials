package entity;

import main.GamePanel;
import main.KeyHandler;
import main.States;
import object.OBJ_Lantern;
import object.OBJ_Shield_Iron;
import object.OBJ_Sword_Iron;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity{
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    public boolean lightUpdated = false;


    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 25;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        super(gamePanel);
        this.keyHandler = keyHandler;

        screenX = this.gamePanel.screenWidth / 2 - (this.gamePanel.tileSize / 2);
        screenY = this.gamePanel.screenHeight / 2 - (this.gamePanel.tileSize / 2);

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
        up1 = registerEntitySprite("/player/walking/ghost_up_1", gamePanel.tileSize, gamePanel.tileSize);
        up2 = registerEntitySprite("/player/walking/ghost_up_2", gamePanel.tileSize, gamePanel.tileSize);
        up3 = registerEntitySprite("/player/walking/ghost_up_3", gamePanel.tileSize, gamePanel.tileSize);

        down1 = registerEntitySprite("/player/walking/ghost_down_1", gamePanel.tileSize, gamePanel.tileSize);
        down2 = registerEntitySprite("/player/walking/ghost_down_2", gamePanel.tileSize, gamePanel.tileSize);
        down3 = registerEntitySprite("/player/walking/ghost_down_3", gamePanel.tileSize, gamePanel.tileSize);

        left1 = registerEntitySprite("/player/walking/ghost_left_1", gamePanel.tileSize, gamePanel.tileSize);
        left2 = registerEntitySprite("/player/walking/ghost_left_2", gamePanel.tileSize, gamePanel.tileSize);
        left3 = registerEntitySprite("/player/walking/ghost_left_3", gamePanel.tileSize, gamePanel.tileSize);

        right1 = registerEntitySprite("/player/walking/ghost_right_1", gamePanel.tileSize, gamePanel.tileSize);
        right2 = registerEntitySprite("/player/walking/ghost_right_2", gamePanel.tileSize, gamePanel.tileSize);
        right3 = registerEntitySprite("/player/walking/ghost_right_3", gamePanel.tileSize, gamePanel.tileSize);
    }
    public void getAttackImage() {
        if (currentWeapon.tags.contains(EntityTags.TAG_AMETHIST)) {
            attackUp = registerEntitySprite("/player/attack/torgray_amethist_attack_up", gamePanel.tileSize, gamePanel.tileSize * 2);
            attackDown = registerEntitySprite("/player/attack/torgray_amethist_attack_down", gamePanel.tileSize, gamePanel.tileSize * 2);
            attackLeft = registerEntitySprite("/player/attack/torgray_amethist_attack_left", gamePanel.tileSize * 2, gamePanel.tileSize);
            attackRight = registerEntitySprite("/player/attack/torgray_amethist_attack_right", gamePanel.tileSize * 2, gamePanel.tileSize);
        } else if (currentWeapon.tags.contains(EntityTags.TAG_IRON)) {
            attackUp = registerEntitySprite("/player/attack/torgray_iron_attack_up", gamePanel.tileSize, gamePanel.tileSize * 2);
            attackDown = registerEntitySprite("/player/attack/torgray_iron_attack_down", gamePanel.tileSize, gamePanel.tileSize * 2);
            attackLeft = registerEntitySprite("/player/attack/torgray_iron_attack_left", gamePanel.tileSize * 2, gamePanel.tileSize);
            attackRight = registerEntitySprite("/player/attack/torgray_iron_attack_right", gamePanel.tileSize * 2, gamePanel.tileSize);
        }
    }
    public void setDefaultValues() {
        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 21;
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
        currentWeapon = new OBJ_Sword_Iron(gamePanel);
        currentShield = new OBJ_Shield_Iron(gamePanel);
        currentLight = new OBJ_Lantern(gamePanel);
        attack = getAttack();
        defence = getDefence();
    }
    public void setDefaultPosition() {
        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 21;
        direction = "down";
    }
    public void restoreHealth() {
        health = maxHealth;
        invincible = false;
    }
    public void setItems() {
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(currentLight);
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
            attack();
        } else if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed || keyHandler.spacePressed || keyHandler.interactKeyPressed) {
            if (keyHandler.upPressed && keyHandler.leftPressed) {
                direction = "up left";
            } else if (keyHandler.upPressed && keyHandler.rightPressed) {
                direction = "up right";
            } else if (keyHandler.downPressed && keyHandler.leftPressed) {
                direction = "down left";
            } else if (keyHandler.downPressed && keyHandler.rightPressed) {
                direction = "down right";
            } else if (keyHandler.upPressed) {
                direction = "up";
            } else if (keyHandler.downPressed) {
                direction = "down";
            } else if (keyHandler.leftPressed) {
                direction = "left";
            } else if (keyHandler.rightPressed) {
                direction = "right";
            }

            // Check tile collision
            collisionOn = false;
            gamePanel.cChecker.checkTile(this);

            // Check OBJ collision
            int objIndex = gamePanel.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check NPC collision
            int npcIndex = gamePanel.cChecker.checkEntity(this, gamePanel.npc);
            interactNPC(npcIndex);

            // Check Event
            gamePanel.eventHandler.checkEvent();

            // Check Mob Collision
            int mobIndex = gamePanel.cChecker.checkEntity(this, gamePanel.mob);
            contactMob(mobIndex);

            if (!collisionOn && !keyHandler.spacePressed && !keyHandler.interactKeyPressed) {
                switch (direction) {
                    case "up left": worldX -= (speed - 1); worldY -= (speed - 1); break;
                    case "up right": worldX += (speed - 1); worldY -= (speed - 1); break;
                    case "down left": worldX -= (speed - 1); worldY += (speed - 1); break;
                    case "down right": worldX += (speed - 1); worldY += (speed - 1); break;
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            // Attacking
            if (keyHandler.spacePressed && !attackCanceled) {
                attacking = true;
                spriteCounter = 0;
            }

            // Inventory
            if (keyHandler.interactKeyPressed && !attackCanceled) {
                gamePanel.gameState = States.STATE_CHARACTER;
            }

            attackCanceled = false;
            gamePanel.keyHandler.spacePressed = false;
            gamePanel.keyHandler.interactKeyPressed = false;

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
        if (health > maxHealth) {
            health = maxHealth;
        }
        if (health <= 0) {
            gamePanel.gameState = States.STATE_GAME_OVER;
            gamePanel.playSound(9);
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
                case "left", "down left", "up left": worldX -= attackArea.width; break;
                case "right", "up right", "down right": worldX += attackArea.width; break;
            }
            // Attack Area = Solid Area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check collision with the updates
            int mobIndex = gamePanel.cChecker.checkEntity(this, gamePanel.mob);
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
            if (gamePanel.obj.get(i).tags.contains(EntityTags.TAG_OBSTACLE)) {
                if (keyHandler.interactKeyPressed) {
                    gamePanel.obj.get(i).interact();
                }
            }
            else if (gamePanel.obj.get(i).tags.contains(EntityTags.TAG_PICKUPONLY)) {
                gamePanel.obj.get(i).use(this);
                gamePanel.obj.put(i, null);
            } else {
                String text;

                if (canObtainItem(gamePanel.obj.get(i))) {
                    gamePanel.playSound(1);
                    text = "+1 " + gamePanel.obj.get(i).name;
                }
                else {
                    text = "Inventory Full";
                }
                gamePanel.ui.addMessage(text);
                gamePanel.obj.put(i, null);
            }
        }
    }
    public void interactNPC(int i) {
        if (gamePanel.keyHandler.interactKeyPressed) {
            if (i != 999) {
                attackCanceled = true;
                gamePanel.gameState = States.STATE_DIALOGUE;
                gamePanel.npc.get(i).speak();
            }
        }
    }
    public void contactMob(int i) {
        if (i != 999) {
            if (!invincible && !gamePanel.mob.get(i).dying) {
                gamePanel.playSound(7);

                int damage = gamePanel.mob.get(i).attack - defence;
                if (damage < 0) {
                    damage = 0;
                }
                health -= damage;
                generateParticles(gamePanel.player, gamePanel.player, damage);
                invincible = true;
            }
        }
    }

    public void damageMob(int i) {
        if (i != 999) {
            if (!gamePanel.mob.get(i).invincible) {
                gamePanel.playSound(6);

                int damage = attack - gamePanel.mob.get(i).defence;
                if (damage < 0) {
                    damage = 0;
                }
                gamePanel.mob.get(i).health -= damage;

                gamePanel.mob.get(i).invincible = true;
                gamePanel.mob.get(i).damageReaction();

                generateParticles(gamePanel.mob.get(i), gamePanel.mob.get(i), damage);

                if (gamePanel.mob.get(i).health <= 0) {
                    gamePanel.mob.get(i).dying = true;
                    gamePanel.ui.addMessage("Killed " + gamePanel.mob.get(i).name);
                    gamePanel.ui.addMessage("+" + gamePanel.mob.get(i).exp + " exp");
                    exp += gamePanel.mob.get(i).exp;
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
            strength++;
            dexterity++;
            attack = getAttack();
            defence = getDefence();
            gamePanel.ui.addMessage("Level Up!");

            if (health >= maxHealth) {
                health = maxHealth;
            }
        }
    }
    public void selectItem() {
        int itemIndex = gamePanel.ui.getItemIndex();

        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.tags.contains(EntityTags.TAG_SWORD)) {
                currentWeapon = selectedItem;
                attack = getAttack();
                getAttackImage();
            }
            if (selectedItem.tags.contains(EntityTags.TAG_SHIELD)) {
                currentShield = selectedItem;
                defence = getDefence();
            }
            if (selectedItem.tags.contains(EntityTags.TAG_LIGHT)) {
                if (currentLight == selectedItem) {
                    currentLight = null;
                } else {
                    currentLight = selectedItem;
                }
                lightUpdated = true;
            }
            if (selectedItem.tags.contains(EntityTags.TAG_CONSUMABLE)) {
                if (selectedItem.use(this)) {
                    if (selectedItem.amount > 1) {
                        selectedItem.amount--;
                    } else {
                        inventory.remove(itemIndex);
                    }
                }
            }
        }
    }
    public int searchInInventory(String itemName) {
        int itemIndex = 999;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).name.equals(itemName)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }
    public boolean canObtainItem(Entity item) {
        boolean canObtain = false;

        // Check if stackable
        if (item.stackable) {
            int index = searchInInventory(item.name);

            if (index != 999) {
                inventory.get(index).amount++;
                canObtain = true;
            }
            else { // New item, so check vacancy
                if (inventory.size() != maxInventorySize) {
                    inventory.add(item);
                    canObtain = true;
                }
            }
        } else { // Not stackable, so check vacancy
            if (inventory.size() != maxInventorySize) {
                inventory.add(item);
                canObtain = true;
            }
        }
        return canObtain;
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
                    tempScreenY = screenY - gamePanel.tileSize;
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
            case "left", "up left", "down left":
                if (!attacking) {
                    if (spriteNumber == 1) {image = left1;}
                    else if (spriteNumber == 2) {image = left2;}
                    else if (spriteNumber == 3) {image = left3;}
                }
                if (attacking) {
                    tempScreenX = screenX - gamePanel.tileSize;
                    if (spriteNumber == 1) {image = attackLeft;}
                    if (spriteNumber == 2) {image = attackLeft;}
                    if (spriteNumber == 3) {image = attackLeft;}
                }
            break;
            case "right", "up right", "down right":
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

    // Particles
    public Color getParticleColor() {return new Color(178, 29, 29);}
    public int getParticleSize() {return 6;} // 6 pixels
    public int getParticleSpeed() {return 1;}
    public int getParticleMaxHealth() {return 20;}
}
