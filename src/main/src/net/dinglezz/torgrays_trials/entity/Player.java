package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.event.EventHandler;
import net.dinglezz.torgrays_trials.main.*;
import net.dinglezz.torgrays_trials.object.OBJ_Coins;
import net.dinglezz.torgrays_trials.object.OBJ_Lantern;
import net.dinglezz.torgrays_trials.object.shield.OBJ_Shield_Iron;
import net.dinglezz.torgrays_trials.object.weapon.OBJ_Sword_Iron;
import net.dinglezz.torgrays_trials.tile.MapHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Player extends Entity{
    InputHandler inputHandler;

    public final int screenX;
    public final int screenY;
    int standCounter = 0;
    public boolean attackCanceled = false;

    public Player(Game game, InputHandler inputHandler) {
        super(game);
        this.inputHandler = inputHandler;

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
        // Up
        up1 = registerEntitySprite("/player/walking/torgray_up_1");
        up2 = registerEntitySprite("/player/walking/torgray_up_2");
        up3 = registerEntitySprite("/player/walking/torgray_up_3");

        // Down
        down1 = registerEntitySprite("/player/walking/torgray_down_1");
        down2 = registerEntitySprite("/player/walking/torgray_down_2");
        down3 = registerEntitySprite("/player/walking/torgray_down_3");

        // Left
        left1 = registerEntitySprite("/player/walking/torgray_left_1");
        left2 = registerEntitySprite("/player/walking/torgray_left_2");
        left3 = registerEntitySprite("/player/walking/torgray_left_3");

        // Right
        right1 = registerEntitySprite("/player/walking/torgray_right_1");
        right2 = registerEntitySprite("/player/walking/torgray_right_2");
        right3 = registerEntitySprite("/player/walking/torgray_right_3");
    }
    public void getAttackImage() {
        // It takes the weapon name and replaces spaces for "_" to get the path for the sprite
        String modifiedName = currentWeapon.name.toLowerCase().replace(" ", "_");
        attackUp = registerEntitySprite("/player/attack/" + modifiedName + "/torgray_" + modifiedName + "_attack_up", game.tileSize, game.tileSize * 2);
        attackDown = registerEntitySprite("/player/attack/" + modifiedName + "/torgray_" + modifiedName + "_attack_down", game.tileSize, game.tileSize * 2);
        attackLeft = registerEntitySprite("/player/attack/" + modifiedName + "/torgray_" + modifiedName + "_attack_left", game.tileSize * 2, game.tileSize);
        attackRight = registerEntitySprite("/player/attack/" + modifiedName + "/torgray_" + modifiedName + "_attack_right", game.tileSize * 2, game.tileSize);
    }
    public void setDefaultValues() {
        type = EntityTypes.TYPE_PLAYER;
        defaultSpeed = 4;
        speed = defaultSpeed;

        // Player Stats
        level = 1;
        maxHealth = 12;
        health = maxHealth;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        coins = 2;
        currentWeapon = new OBJ_Sword_Iron(game);
        currentShield = new OBJ_Shield_Iron(game);
        currentLight = new OBJ_Lantern(game);
        attack = getAttack();
        defence = getDefence();
    }
    public void setDefaultPosition() {
        JSONObject file = MapHandler.mapFiles.get(game.currentMap);
        try {
            worldX = game.tileSize * file.getJSONObject("spawn point").getInt("col");
            worldY = game.tileSize * file.getJSONObject("spawn point").getInt("row");
        } catch (JSONException jsonException) {
            file = MapHandler.mapFiles.get("Disabled");
            worldX = game.tileSize * file.getJSONObject("spawn point").getInt("col");
            worldY = game.tileSize * file.getJSONObject("spawn point").getInt("row");
        }

        direction = "down";
    }
    public void restoreHealth() {
        health = maxHealth;
        invincible = false;
    }
    public void setItems() {
        inventory.clear();
        inventory.add(new OBJ_Coins(game, 2));
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
        } else if (inputHandler.upPressed || inputHandler.downPressed || inputHandler.leftPressed || inputHandler.rightPressed || inputHandler.interactKeyPressed) {
            if (inputHandler.upPressed && inputHandler.leftPressed) direction = "up left";
            else if (inputHandler.upPressed && inputHandler.rightPressed) direction = "up right";
            else if (inputHandler.downPressed && inputHandler.leftPressed) direction = "down left";
            else if (inputHandler.downPressed && inputHandler.rightPressed) direction = "down right";
            else if (inputHandler.upPressed) direction = "up";
            else if (inputHandler.downPressed) direction = "down";
            else if (inputHandler.leftPressed) direction = "left";
            else if (inputHandler.rightPressed) direction = "right";

            // Check tile collision
            collisionOn = false;
            CollisionChecker.checkTile(this);

            // Check OBJ collision
            int objectIndex = CollisionChecker.checkObject(this, true);
            pickUpObject(objectIndex);

            // Check NPC collision
            int npcIndex = CollisionChecker.checkEntity(this, game.npc);
            interactNPC(npcIndex);

            // Check Event
            EventHandler.checkEvent();

            // Check Mob Collision
            int monsterIndex = CollisionChecker.checkEntity(this, game.monster);
            contactMonster(monsterIndex);

            if (!collisionOn && !inputHandler.spacePressed && !inputHandler.interactKeyPressed) {
                switch (direction) {
                    case "up left" -> {worldX -= (speed - 1); worldY -= (speed - 1);}
                    case "up right" -> {worldX += (speed - 1); worldY -= (speed - 1);}
                    case "down left" -> {worldX -= (speed - 1); worldY += (speed - 1);}
                    case "down right" -> {worldX += (speed - 1); worldY += (speed - 1);}
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            // Inventory
            if (inputHandler.interactKeyPressed && !attackCanceled) {
                game.ui.uiState = States.UIStates.CHARACTER;
            }

            game.inputHandler.interactKeyPressed = false;

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
            invincibilityCounter++;
            if (invincibilityCounter > 60) {
                invincible = false;
                invincibilityCounter = 0;
            }
        }
        if (health > maxHealth) {
            health = maxHealth;
        }
        if (health <= 0) {
            game.gameState = States.GameStates.GAME_END;
            Sound.playSFX("Game Over");
            game.ui.commandNumber = -1;
            Sound.stopMusic();
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
                case "up" -> worldY -= attackArea.height;
                case "down" -> worldY += attackArea.height;
                case "left", "down left", "up left" -> worldX -= attackArea.width;
                case "right", "up right", "down right" -> worldX += attackArea.width;
            }
            // Attack Area = Solid Area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check collision with the updates
            int monsterIndex = CollisionChecker.checkEntity(this, game.monster);
            damageMonster(monsterIndex, currentWeapon.knockBackPower);

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
            if (game.object.get(game.currentMap).get(i).tags.contains(EntityTags.TAG_OBSTACLE)) {
                if (inputHandler.interactKeyPressed) {
                    game.object.get(game.currentMap).get(i).interact();
                }
            }
            else if (game.object.get(game.currentMap).get(i).tags.contains(EntityTags.TAG_PICKUP_ONLY)) {
                game.object.get(game.currentMap).get(i).use(this);
                game.object.get(game.currentMap).put(i, null);
            }
            else if (canObtainItem(game.object.get(game.currentMap).get(i))) {
                Sound.playSFX("Coin");
                String text = "+1 " + game.object.get(game.currentMap).get(i).name;
                game.ui.addMiniNotification(text);
                game.object.get(game.currentMap).put(i, null);
            }
        }
    }
    public void interactNPC(int i) {
        if (game.inputHandler.interactKeyPressed) {
            if (i != 999) {
                attackCanceled = true;
                game.ui.uiState = States.UIStates.DIALOGUE;
                game.npc.get(game.currentMap).get(i).speak(false);
            }
        }
    }
    public void contactMonster(int i) {
        if (i != 999) {
            if (!invincible && !game.monster.get(game.currentMap).get(i).dying) {
                Sound.playSFX("Receive Damage");

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

    public void damageMonster(int i, int knockBackPower) {
        if (i != 999) {
            if (!game.monster.get(game.currentMap).get(i).invincible) {
                Sound.playSFX("Hit Monster");

                // KnockBack :D
                if (knockBackPower > 0) {
                    knockBack(game.monster.get(game.currentMap).get(i), knockBackPower);
                }

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
                    game.ui.addMiniNotification("Killed " + game.monster.get(game.currentMap).get(i).name);
                    game.ui.addMiniNotification("+" + game.monster.get(game.currentMap).get(i).exp + " exp");
                    exp += game.monster.get(game.currentMap).get(i).exp;
                    checkLevelUp();
                }
            }
        }
    }
    public void knockBack(Entity entity, int knockBackPower) {
        entity.direction = direction;
        entity.speed += knockBackPower;
        entity.knockBack = true;
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
            game.ui.addMiniNotification("Level Up!");

            if (health >= maxHealth) {
                health = maxHealth;
            }
        }
    }
    public void selectItem() {
        int itemIndex = game.ui.getItemIndex(game.ui.playerSlotCol, game.ui.playerSlotRow);

        if (itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);

            if (selectedItem.tags.contains(EntityTags.TAG_WEAPON)) {
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
                game.environmentManager.lightUpdated = true;
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

        // Check if maxStack
        if (!(item.amount >= item.maxStack) && !Objects.equals(item.name, "Coins")) {
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
        } else if (Objects.equals(item.name, "Coins")) {
            coins += item.amount;
            canObtain = true;
        } else { // Not maxStack, so check vacancy
            if (inventory.size() != maxInventorySize) {
                inventory.add(item);
                canObtain = true;
            }
        }
        return canObtain;
     }

    public void draw(Graphics2D graphics2D) {
        BufferedImage image = null;
        int temporaryScreenX = screenX;
        int temporaryScreenY = screenY;

        switch (direction) {
            case "up" -> {
                if (!attacking) {
                    if (spriteNumber == 1) {image = up1;}
                    else if (spriteNumber == 2) {image = up2;}
                    else if (spriteNumber == 3) {image = up3;}
                } if (attacking) {
                    temporaryScreenY = screenY - game.tileSize;
                    if (spriteNumber == 1) {image = attackUp;}
                    if (spriteNumber == 2) {image = attackUp;}
                    if (spriteNumber == 3) {image = attackUp;}
                }
            }
            case "down" -> {
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
            }
            case "left", "up left", "down left" -> {
                if (!attacking) {
                    if (spriteNumber == 1) {image = left1;}
                    else if (spriteNumber == 2) {image = left2;}
                    else if (spriteNumber == 3) {image = left3;}
                }
                if (attacking) {
                    temporaryScreenX = screenX - game.tileSize;
                    if (spriteNumber == 1) {image = attackLeft;}
                    if (spriteNumber == 2) {image = attackLeft;}
                    if (spriteNumber == 3) {image = attackLeft;}
                }
            }
            case "right", "up right", "down right" -> {
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
            }
        }
        if (invincible) {
            graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        graphics2D.drawImage(image, temporaryScreenX, temporaryScreenY, null);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        if (game.debugHitBoxes) {
            graphics2D.setColor(new Color(0.7f, 0, 0, 0.3f));
            graphics2D.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }

    // Particles
    @Override public Color getParticleColor() {return new Color(178, 29, 29);}
    @Override public int getParticleSize() {return 6;} // 6 pixels
    @Override public int getParticleSpeed() {return 1;}
    @Override public int getParticleMaxHealth() {return 20;}
}
