package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.KeyHandler;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.object.OBJ_Lantern;
import net.dinglezz.torgrays_trials.object.OBJ_Shield_Iron;
import net.dinglezz.torgrays_trials.object.OBJ_Sword_Iron;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;
    public boolean lightUpdated = false;

    public Player(Game game, KeyHandler keyHandler) {
        super(game);
        this.keyHandler = keyHandler;

        screenX = this.game.screenWidth / 2 - (this.game.tileSize / 2);
        screenY = this.game.screenHeight / 2 - (this.game.tileSize / 2);

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
        up1 = registerEntitySprite("/drawable/player/walking/torgray_up_1");
        up2 = registerEntitySprite("/drawable/player/walking/torgray_up_2");
        up3 = registerEntitySprite("/drawable/player/walking/torgray_up_3");

        down1 = registerEntitySprite("/drawable/player/walking/torgray_down_1");
        down2 = registerEntitySprite("/drawable/player/walking/torgray_down_2");
        down3 = registerEntitySprite("/drawable/player/walking/torgray_down_3");

        left1 = registerEntitySprite("/drawable/player/walking/torgray_left_1");
        left2 = registerEntitySprite("/drawable/player/walking/torgray_left_2");
        left3 = registerEntitySprite("/drawable/player/walking/torgray_left_3");

        right1 = registerEntitySprite("/drawable/player/walking/torgray_right_1");
        right2 = registerEntitySprite("/drawable/player/walking/torgray_right_2");
        right3 = registerEntitySprite("/drawable/player/walking/torgray_right_3");
    }
    public void getAttackImage() {
        if (currentWeapon.tags.contains(EntityTags.TAG_AMETHIST)) {
            attackUp = registerEntitySprite("/drawable/player/attack/torgray_amethist_attack_up", game.tileSize, game.tileSize * 2);
            attackDown = registerEntitySprite("/drawable/player/attack/torgray_amethist_attack_down", game.tileSize, game.tileSize * 2);
            attackLeft = registerEntitySprite("/drawable/player/attack/torgray_amethist_attack_left", game.tileSize * 2, game.tileSize);
            attackRight = registerEntitySprite("/drawable/player/attack/torgray_amethist_attack_right", game.tileSize * 2, game.tileSize);
        } else if (currentWeapon.tags.contains(EntityTags.TAG_IRON)) {
            attackUp = registerEntitySprite("/drawable/player/attack/torgray_iron_attack_up", game.tileSize, game.tileSize * 2);
            attackDown = registerEntitySprite("/drawable/player/attack/torgray_iron_attack_down", game.tileSize, game.tileSize * 2);
            attackLeft = registerEntitySprite("/drawable/player/attack/torgray_iron_attack_left", game.tileSize * 2, game.tileSize);
            attackRight = registerEntitySprite("/drawable/player/attack/torgray_iron_attack_right", game.tileSize * 2, game.tileSize);
        }
    }
    public void setDefaultValues() {
        worldX = game.tileSize * 23;
        worldY = game.tileSize * 21;
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
        currentWeapon = new OBJ_Sword_Iron(game);
        currentShield = new OBJ_Shield_Iron(game);
        currentLight = new OBJ_Lantern(game);
        attack = getAttack();
        defence = getDefence();
    }
    public void setDefaultPosition() {
        worldX = game.tileSize * 23;
        worldY = game.tileSize * 21;
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
            game.collisionChecker.checkTile(this);

            // Check OBJ collision
            int objIndex = game.collisionChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check NPC collision
            int npcIndex = game.collisionChecker.checkEntity(this, game.npc);
            interactNPC(npcIndex);

            // Check Event
            game.eventHandler.checkEvent();

            // Check Mob Collision
            int mobIndex = game.collisionChecker.checkEntity(this, game.monster);
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
                game.gameState = States.STATE_CHARACTER;
            }

            attackCanceled = false;
            game.keyHandler.spacePressed = false;
            game.keyHandler.interactKeyPressed = false;

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
            game.gameState = States.STATE_GAME_OVER;
            game.playSound("Game Over");
            game.ui.commandNumber = -1;
            game.stopMusic();
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
            int mobIndex = game.collisionChecker.checkEntity(this, game.monster);
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
            if (game.obj.get(game.currentMap).get(i).tags.contains(EntityTags.TAG_OBSTACLE)) {
                if (keyHandler.interactKeyPressed) {
                    game.obj.get(game.currentMap).get(i).interact();
                }
            }
            else if (game.obj.get(game.currentMap).get(i).tags.contains(EntityTags.TAG_PICKUPONLY)) {
                game.obj.get(game.currentMap).get(i).use(this);
                game.obj.get(game.currentMap).put(i, null);
            } else {
                String text;

                if (canObtainItem(game.obj.get(game.currentMap).get(i))) {
                    game.playSound("Coin");
                    text = "+1 " + game.obj.get(game.currentMap).get(i).name;
                }
                else {
                    text = "Inventory Full";
                }
                game.ui.addMessage(text);
                game.obj.get(game.currentMap).put(i, null);
            }
        }
    }
    public void interactNPC(int i) {
        if (game.keyHandler.interactKeyPressed) {
            if (i != 999) {
                attackCanceled = true;
                game.gameState = States.STATE_DIALOGUE;
                game.npc.get(game.currentMap).get(i).speak(false);
            }
        }
    }
    public void contactMob(int i) {
        if (i != 999) {
            if (!invincible && !game.monster.get(game.currentMap).get(i).dying) {
                game.playSound("Receive Damage");

                int damage = game.monster.get(game.currentMap).get(i).attack - defence;
                if (damage < 0) {
                    damage = 0;
                }
                health -= damage;
                generateParticles(game.player, game.player);
                invincible = true;
            }
        }
    }

    public void damageMob(int i) {
        if (i != 999) {
            if (!game.monster.get(game.currentMap).get(i).invincible) {
                game.playSound("Hit Mob");

                int damage = attack - game.monster.get(game.currentMap).get(i).defence;
                if (damage < 0) {
                    damage = 0;
                }
                game.monster.get(game.currentMap).get(i).health -= damage;

                game.monster.get(game.currentMap).get(i).invincible = true;
                game.monster.get(game.currentMap).get(i).damageReaction();

                generateParticles(game.monster.get(game.currentMap).get(i), game.monster.get(game.currentMap).get(i));

                if (game.monster.get(game.currentMap).get(i).health <= 0) {
                    game.monster.get(game.currentMap).get(i).dying = true;
                    game.ui.addMessage("Killed " + game.monster.get(game.currentMap).get(i).name);
                    game.ui.addMessage("+" + game.monster.get(game.currentMap).get(i).exp + " exp");
                    exp += game.monster.get(game.currentMap).get(i).exp;
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
            game.ui.addMessage("Level Up!");

            if (health >= maxHealth) {
                health = maxHealth;
            }
        }
    }
    public void selectItem() {
        int itemIndex = game.ui.getItemIndex();

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

    public void draw(Graphics2D graphics2D) {
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
                    tempScreenY = screenY - game.tileSize;
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
                    tempScreenX = screenX - game.tileSize;
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
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        graphics2D.drawImage(image, tempScreenX, tempScreenY, null);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    // Particles
    public Color getParticleColor() {return new Color(178, 29, 29);}
    public int getParticleSize() {return 6;} // 6 pixels
    public int getParticleSpeed() {return 1;}
    public int getParticleMaxHealth() {return 20;}
}
