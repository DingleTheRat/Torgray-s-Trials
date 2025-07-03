package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.entity.item.*;
import net.dinglezz.torgrays_trials.entity.item.light.Lantern;
import net.dinglezz.torgrays_trials.entity.item.soup.Coiner_Soup;
import net.dinglezz.torgrays_trials.effects.Effect;
import net.dinglezz.torgrays_trials.entity.monster.Monster;
import net.dinglezz.torgrays_trials.event.EventHandler;
import net.dinglezz.torgrays_trials.main.*;
import net.dinglezz.torgrays_trials.entity.item.shield.Shield_Iron;
import net.dinglezz.torgrays_trials.entity.item.weapon.Sword_Iron;
import net.dinglezz.torgrays_trials.tile.MapHandler;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.Serializable;

public class Player extends Mob implements Serializable {
    // Attributes
    public int exp;
    public int level;

    public final int screenX;
    public final int screenY;
    private boolean attackCanceled = false;
    private boolean inventoryCanceled = false;
    private boolean attackCheckCanceled = false;

    public Player() {
        super("Player", null);

        screenX = Main.game.screenWidth / 2 - (Main.game.tileSize / 2);
        screenY = Main.game.screenHeight / 2 - (Main.game.tileSize / 2);

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
        up1 = registerEntitySprite("entity/player/walking/torgray_up_1");
        up2 = registerEntitySprite("entity/player/walking/torgray_up_2");
        up3 = registerEntitySprite("entity/player/walking/torgray_up_3");

        // Down
        down1 = registerEntitySprite("entity/player/walking/torgray_down_1");
        down2 = registerEntitySprite("entity/player/walking/torgray_down_2");
        down3 = registerEntitySprite("entity/player/walking/torgray_down_3");

        // Left
        left1 = registerEntitySprite("entity/player/walking/torgray_left_1");
        left2 = registerEntitySprite("entity/player/walking/torgray_left_2");
        left3 = registerEntitySprite("entity/player/walking/torgray_left_3");

        // Right
        right1 = registerEntitySprite("entity/player/walking/torgray_right_1");
        right2 = registerEntitySprite("entity/player/walking/torgray_right_2");
        right3 = registerEntitySprite("entity/player/walking/torgray_right_3");
    }
    public void getAttackImage() {
        // It takes the weapon name and replaces spaces for "_" to get the path for the sprite
        String modifiedName = currentWeapon.name.toLowerCase().replace(" ", "_");
        attackUp = registerEntitySprite("entity/player/attack/" + modifiedName + "/torgray_" + modifiedName + "_attack_up", Main.game.tileSize, Main.game.tileSize * 2);
        attackDown = registerEntitySprite("entity/player/attack/" + modifiedName + "/torgray_" + modifiedName + "_attack_down", Main.game.tileSize, Main.game.tileSize * 2);
        attackLeft = registerEntitySprite("entity/player/attack/" + modifiedName + "/torgray_" + modifiedName + "_attack_left", Main.game.tileSize * 2, Main.game.tileSize);
        attackRight = registerEntitySprite("entity/player/attack/" + modifiedName + "/torgray_" + modifiedName + "_attack_right", Main.game.tileSize * 2, Main.game.tileSize);
    }
    public void setDefaultValues() {
        defaultSpeed = 4;
        speed = defaultSpeed;

        // Player Stats
        level = 1;
        maxHealth = 12;
        heal(maxHealth);
        dying = false;
        exp = 0;
        coins = 2;
        currentWeapon = new Sword_Iron(null);
        currentShield = new Shield_Iron(null);
        currentLight = new Lantern(null);
    }
    public void setDefaultPosition() {
        JSONObject file = MapHandler.mapFiles.get(Main.game.currentMap);
        try {
            worldX = Main.game.tileSize * file.getJSONObject("spawn point").getInt("col");
            worldY = Main.game.tileSize * file.getJSONObject("spawn point").getInt("row");
        } catch (JSONException jsonException) {
            file = MapHandler.mapFiles.get("Disabled");
            worldX = Main.game.tileSize * file.getJSONObject("spawn point").getInt("col");
            worldY = Main.game.tileSize * file.getJSONObject("spawn point").getInt("row");
        }
        tilePoint = new TilePoint(Main.game.currentMap, worldX / Main.game.tileSize, worldY / Main.game.tileSize);
        direction = "down";
    }
    public void setItems() {
        clearInventory();
        inventory.add(new Coins(null, 2));
        giveItem(currentWeapon);
        giveItem(currentShield);
        giveItem(currentLight);
        giveItem(new Coiner_Soup(null));
    }

    @Override
    public void checkCollision() {
        // Check tile collision
        colliding = false;
        CollisionChecker.checkTile(this);

        // Check Entity collision and call the necessary methods
        // Objects
        Entity collidingObject = CollisionChecker.checkEntity(this, Main.game.objects.get(Main.game.currentMap));
        if (collidingObject != null && Main.game.inputHandler.interactKeyPressed) collidingObject.onInteract();
        if (collidingObject != null) collidingObject.onPlayerHit();

        // Items
        Item collidingItem = CollisionChecker.checkEntity(this, Main.game.items.get(Main.game.currentMap));
        if (collidingItem != null && Main.game.inputHandler.interactKeyPressed) collidingItem.onInteract();
        if (collidingItem != null) collidingItem.onPlayerHit();

        // NPCs
        Mob collidingNPC = CollisionChecker.checkEntity(this, Main.game.npcs.get(Main.game.currentMap));
        if (collidingNPC != null) collidingNPC.onPlayerHit();
        if (collidingNPC != null && Main.game.inputHandler.interactKeyPressed) collidingNPC.onInteract();

        // Monsters
        Monster collidingMonster = CollisionChecker.checkEntity(this, Main.game.monsters.get(Main.game.currentMap));
        if (collidingMonster != null) collidingMonster.onPlayerHit();
        if (collidingMonster != null && Main.game.inputHandler.interactKeyPressed) collidingMonster.onInteract();

        contactMonster(collidingMonster);

        // Check if the player is on an event
        EventHandler.checkEvents();
    }

    @Override
    public void update() {
        if (attacking) attack();
        else if (Main.game.inputHandler.upPressed || Main.game.inputHandler.downPressed || Main.game.inputHandler.leftPressed || Main.game.inputHandler.rightPressed) {
            // Set direction based on input
            if (Main.game.inputHandler.upPressed && Main.game.inputHandler.leftPressed) direction = "up left";
            else if (Main.game.inputHandler.upPressed && Main.game.inputHandler.rightPressed) direction = "up right";
            else if (Main.game.inputHandler.downPressed && Main.game.inputHandler.leftPressed) direction = "down left";
            else if (Main.game.inputHandler.downPressed && Main.game.inputHandler.rightPressed) direction = "down right";
            else if (Main.game.inputHandler.upPressed) direction = "up";
            else if (Main.game.inputHandler.downPressed) direction = "down";
            else if (Main.game.inputHandler.leftPressed) direction = "left";
            else direction = "right";

            // Check collision
            checkCollision();

            // If no collision, move the player
            if (!colliding) {
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

            // Animate the player sprite
            spriteCounter ++;
            if (spriteCounter > 10) {
                spriteNumber = switch (spriteNumber) {
                    case 1 -> 2;
                    case 2 -> 3;
                    case 3 -> 1;
                    default -> throw new IllegalStateException("Unexpected value: " + spriteNumber);
                };
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
        if (getHealth() <= 0) {
            Sound.stopMusic();
            Main.game.gameState = States.GameStates.GAME_END;
            Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
            Sound.playSFX("Game Over");
            Main.game.ui.commandNumber = -1;
        }

        // Attacking
        if (Main.game.inputHandler.spacePressed && !attackCheckCanceled) {
            checkCollision();

            if (!attacking && !attackCanceled) {
                Sound.playSFX("Swing");
                Main.game.player.attacking = true;
                Main.game.player.spriteCounter = 0;
                attackArea = currentWeapon.attackArea;
            }

            // Reset Values
            Main.game.inputHandler.spacePressed = false;
            attackCanceled = false;
        }
        attackCheckCanceled = false;

        // Inventory
        if (Main.game.inputHandler.interactKeyPressed) {
            checkCollision();

            if (!inventoryCanceled && Main.game.ui.uiState == States.UIStates.JUST_DEFAULT) Main.game.ui.uiState = States.UIStates.CHARACTER;

            // Reset Values
            inventoryCanceled = false;
            Main.game.inputHandler.interactKeyPressed = false;
        }

        // Update effects
        effects.stream().toList().forEach(Effect::update);
    }
    public void attack() {
        spriteCounter ++;

        if (spriteCounter <= 5) {
            spriteNumber = 1;
        } else if (spriteCounter <= 25) {
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
            Monster collidingMonster = CollisionChecker.checkEntity(this, Main.game.monsters.get(Main.game.currentMap));
            damageMonster(collidingMonster, currentWeapon.knockBackPower);

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

    public void cancelAttack() {attackCanceled = true;}
    public void cancelInventory() {inventoryCanceled = true;}
    public void cancelAttackCheck() {attackCheckCanceled = true;}

    public void contactMonster(Monster monster) {
        if (monster != null && !monster.dying) {
            damage(monster.attack);
        }
    }

    public void damageMonster(Monster monster, int knockBackPower) {
        if (monster != null && !monster.dying && !monster.invincible) {
            // KnockBack :D
            if (knockBackPower > 0) knockBack(monster, knockBackPower);

            // Attack time!
            monster.damage(currentWeapon.attackValue);

            // If the monsters is dead, then set it to dying
            if (monster.getHealth() <= 0) {
                monster.dying = true;

                // Add a notification
                Main.game.ui.addMiniNotification("Killed " + monster.name);
            }
        }
    }
    public void knockBack(Mob mob, int knockBackPower) {
        mob.direction = direction;
        mob.speed += knockBackPower;
        mob.knockBack = true;
    }
    public void selectItem() {
        int itemIndex = Main.game.ui.getItemIndex(Main.game.ui.playerSlotCol, Main.game.ui.playerSlotRow);

        if (itemIndex < getInventory().size()) {
            Item selectedItem = getInventory().get(itemIndex);

            if (selectedItem.tags.contains(ItemTags.TAG_WEAPON)) {
                currentWeapon = selectedItem;
                getAttackImage();
            }
            if (selectedItem.tags.contains(ItemTags.TAG_SHIELD)) {
                currentShield = selectedItem;
            }
            if (selectedItem.tags.contains(ItemTags.TAG_LIGHT)) {
                if (currentLight == selectedItem) {
                    currentLight = null;
                } else {
                    currentLight = selectedItem;
                }
                Main.game.environmentManager.lightUpdated = true;
            }
            if (selectedItem.tags.contains(ItemTags.TAG_CONSUMABLE)) {
                if (selectedItem.use(this)) {
                    removeItem(selectedItem);
                }
            }
        }
    }

     @Override
    public void draw(Graphics2D graphics2D) {
        int temporaryScreenX = screenX;
        int temporaryScreenY = screenY;

        switch (direction) {
            case "up" -> {
                if (!attacking) {
                    if (spriteNumber == 1) currentImage = up1;
                    else if (spriteNumber == 2) currentImage = up2;
                    else if (spriteNumber == 3) currentImage = up3;
                } if (attacking) {
                    temporaryScreenY = screenY - Main.game.tileSize;
                    if (spriteNumber == 1) currentImage = attackUp;
                    if (spriteNumber == 2) currentImage = attackUp;
                    if (spriteNumber == 3) currentImage = attackUp;
                }
            }
            case "down" -> {
                if (!attacking) {
                    if (spriteNumber == 1) currentImage = down1;
                    else if (spriteNumber == 2) currentImage = down2;
                    else if (spriteNumber == 3) currentImage = down3;
                }
                if (attacking) {
                    if (spriteNumber == 1) currentImage = attackDown;
                    if (spriteNumber == 2) currentImage = attackDown;
                    if (spriteNumber == 3) currentImage = attackDown;
                }
            }
            case "left", "up left", "down left" -> {
                if (!attacking) {
                    if (spriteNumber == 1) currentImage = left1;
                    else if (spriteNumber == 2) currentImage = left2;
                    else if (spriteNumber == 3) currentImage = left3;
                }
                if (attacking) {
                    temporaryScreenX = screenX - Main.game.tileSize;
                    if (spriteNumber == 1) currentImage = attackLeft;
                    if (spriteNumber == 2) currentImage = attackLeft;
                    if (spriteNumber == 3) currentImage = attackLeft;
                }
            }
            case "right", "up right", "down right" -> {
                if (!attacking) {
                    if (spriteNumber == 1) currentImage = right1;
                    else if (spriteNumber == 2) currentImage = right2;
                    else if (spriteNumber == 3) currentImage = right3;
                }
                if (attacking) {
                    if (spriteNumber == 1) currentImage = attackRight;
                    if (spriteNumber == 2) currentImage = attackRight;
                    if (spriteNumber == 3) currentImage = attackRight;
                }
            }
        }
        if (invincible) changeAlpha(graphics2D, 0.4f);
        graphics2D.drawImage(currentImage.getImage(), temporaryScreenX, temporaryScreenY, null);
        changeAlpha(graphics2D, 1f);

         if (Main.game.debugHitBoxes) {
             // Solid area
             graphics2D.setColor(new Color(0.7f, 0, 0, 0.3f));
             graphics2D.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);

             // Attack area
             graphics2D.fillRect(screenX + attackArea.x, screenY + attackArea.y, attackArea.width, attackArea.height);
         }
    }

    // Particles
    @Override public Color getParticleColor() {return new Color(178, 29, 29);}
    @Override public int getParticleSize() {return 6;} // 6 pixels
    @Override public int getParticleSpeed() {return 1;}
    @Override public int getParticleMaxHealth() {return 20;}
}
