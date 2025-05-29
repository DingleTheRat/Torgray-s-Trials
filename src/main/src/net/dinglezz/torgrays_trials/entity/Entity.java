package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.main.CollisionChecker;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Entity {
    Game game;
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public BufferedImage attackUp, attackDown, attackLeft, attackRight;
    public BufferedImage image, image2, image3;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    public HashMap<Integer, String> dialogues = new HashMap<>();

    // States
    public float worldX, worldY;
    public String direction = "down";
    public int spriteNumber = 1;
    int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean healthBarOn = false;
    public boolean onPath = false;
    public boolean knockBack = false;


    // Counters
    public int spriteCounter = 0;
    public int spriteSpeed = 10;
    public int actionLockCounter = 0;
    public int invincibilityCounter = 0;
    int dyingCounter = 0;
    int healthBarCounter = 0;
    int knockBackCounter = 0;

    // Attributes
    public String name;
    public EntityTypes type;
    public ArrayList<EntityTags> tags = new ArrayList<>();
    public int defaultSpeed;
    public int speed;
    public int maxHealth;
    public int health;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defence;
    public int exp;
    public int nextLevelExp;
    public int coins;
    public Entity currentWeapon;
    public Entity currentShield;
    public Entity currentLight;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 25;
    public boolean moveOffScreen = false;

    // Item Attributes
    public int value;
    public int attackValue;
    public int knockBackPower = 0;
    public int defenceValue;
    public String description = "Nothing Here :(";
    public int lightRadius;
    public int maxStack = 1;
    public int amount = 1;
    public int price = 0;

    public Entity(Game game) {
        this.game = game;
        down1 = registerEntitySprite("/disabled");
        down2 = registerEntitySprite("/disabled");
        down3 = registerEntitySprite("/disabled");
        up1 = registerEntitySprite("/disabled");
        up2 = registerEntitySprite("/disabled");
        up3 = registerEntitySprite("/disabled");
        left1 = registerEntitySprite("/disabled");
        left2 = registerEntitySprite("/disabled");
        left3 = registerEntitySprite("/disabled");
        right1 = registerEntitySprite("/disabled");
        right2 = registerEntitySprite("/disabled");
        right3 = registerEntitySprite("/disabled");

        attackUp = registerEntitySprite("/disabled");
        attackDown = registerEntitySprite("/disabled");
        attackLeft = registerEntitySprite("/disabled");

        image = registerEntitySprite("/disabled");
        image2 = registerEntitySprite("/disabled");
        image3 = registerEntitySprite("/disabled");
    }

    public float getLeftX() {return worldX + solidArea.x;}
    public float getRightX() {return worldX + solidArea.x + solidArea.width;}
    public float getTopY() {return worldY + solidArea.y;}
    public float getBottomY() {return worldY + solidArea.y + solidArea.height;}
    public float getCol() {return (worldX + solidArea.x) / game.tileSize;}
    public float getRow() {return (worldY + solidArea.y) / game.tileSize;}

    public void setAction() {}
    public void damageReaction() {}
    public void speak(boolean facePlayer) {
        if (dialogues.get(dialogueIndex) == null) dialogueIndex = 0;
        game.ui.setCurrentDialogue(dialogues.get(dialogueIndex));
        dialogueIndex++;

        if (facePlayer) {
            switch (game.player.direction) {
                case "up" -> direction = "down";
                case "down" -> direction = "up";
                case "left" -> direction = "right";
                case "right" -> direction = "left";
            }
        }
    }
    public void interact() {}
    public boolean use(Entity entity) {return false;}
    public void checkDrop() {}
    public void dropItem(Entity droppedItem) {
        if (game.object.get(game.currentMap) != null) {
            for (int i = 0; i < game.object.get(game.currentMap).size(); i++) {
                if (game.object.get(game.currentMap).get(i) == null) {
                    game.object.get(game.currentMap).put(i, droppedItem);
                    droppedItem.worldX = worldX;
                    droppedItem.worldY = worldY;
                    break;
                }
            }
        }
    }
    public Color getParticleColor() {return null;}
    public int getParticleSize() {return 0;}
    public int getParticleSpeed() {return 0;}
    public int getParticleMaxHealth() {return 0;}
    public void generateParticles(Entity generator, Entity target) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxHealth = generator.getParticleMaxHealth();

        Particle p1 = new Particle(game, target, color, size, speed, maxHealth, -2, -1);
        Particle p2 = new Particle(game, target, color, size, speed, maxHealth, 2, -1);
        Particle p3 = new Particle(game, target, color, size, speed, maxHealth, -2, 1);
        Particle p4 = new Particle(game, target, color, size, speed, maxHealth, 2, 1);
        game.particleList.add(p1);
        game.particleList.add(p2);
        game.particleList.add(p3);
        game.particleList.add(p4);
    }
    public void checkCollision() {
        collisionOn = false;

        // Check tile collision
        CollisionChecker.checkTile(this);

        // Check object collision
        CollisionChecker.checkObject(this, false);

        // Check player collision
        boolean contactPlayer = CollisionChecker.checkPlayer(this);

        // Check NPC and monster collisions
        if (game.npc.get(game.currentMap) != null) {
            if (game.npc.get(game.currentMap) != null) {
                CollisionChecker.checkEntity(this, game.npc);
            }
            if (game.monster.get(game.currentMap) != null) {
                CollisionChecker.checkEntity(this, game.monster);
            }
        }

        // Handle monster-specific behavior
        if (this.type == EntityTypes.TYPE_MONSTER && contactPlayer) {
            damagePlayer(attack);
        }
    }

    public void update() {
        if (worldX + game.tileSize > game.player.worldX - game.player.screenX &&
                worldX - game.tileSize < game.player.worldX + game.player.screenX &&
                worldY + game.tileSize > game.player.worldY - game.player.screenY &&
                worldY - game.tileSize < game.player.worldY + game.player.screenY || moveOffScreen) {
            if (knockBack) {
                checkCollision();
                
                if (collisionOn || knockBackCounter == 10) {
                    knockBackCounter = 0;
                    knockBack = false;
                    speed = defaultSpeed;
                } else {
                    switch (game.player.direction) {
                        case "up" -> worldY -= speed * game.deltaTime * 60;
                        case "down" -> worldY += speed * game.deltaTime * 60;
                        case "left" -> worldX -= speed * game.deltaTime * 60;
                        case "right" -> worldX += speed * game.deltaTime * 60;
                    }
                    knockBackCounter++;
                }
            } else {
                setAction();
                checkCollision();
                
                // If collisionOn is false, move the entity
                if (!collisionOn) {
                    switch (direction) {
                        case "up" -> worldY -= speed * game.deltaTime * 60;
                        case "down" -> worldY += speed * game.deltaTime * 60;
                        case "left" -> worldX -= speed * game.deltaTime * 60;
                        case "right" -> worldX += speed * game.deltaTime * 60;
                    }
                }
            }
            
            spriteCounter++;
            if (spriteCounter > spriteSpeed) {
                if (spriteNumber == 1) spriteNumber = 2;
                else if (spriteNumber == 2) spriteNumber = 3;
                else if (spriteNumber == 3) spriteNumber = 1;
                spriteCounter = 0;
            }
            
            if (invincible) {
                invincibilityCounter++;
                if (invincibilityCounter > 40) {
                    invincible = false;
                    invincibilityCounter = 0;
                }
            }
        }
    }
    public void damagePlayer(int attack) {
        if (!game.player.invincible) {
            Sound.playSFX("Receive Damage");

            int damage = attack - game.player.defence;
            if (damage <= 0) {
                damage = 1;
            }
            game.player.health -= damage;
            game.player.invincible = true;
        }
    }
    public void draw(Graphics2D graphics2D) {
        BufferedImage image = null;
        float screenX = worldX - game.player.worldX + game.player.screenX;
        float screenY = worldY - game.player.worldY + game.player.screenY;

        if (worldX + game.tileSize > game.player.worldX - game.player.screenX &&
                worldX - game.tileSize < game.player.worldX + game.player.screenX &&
                worldY + game.tileSize > game.player.worldY - game.player.screenY &&
                worldY - game.tileSize < game.player.worldY + game.player.screenY) {
            switch (direction) {
                case "up": if (spriteNumber == 1) {image = up1;} else if (spriteNumber == 2) {image = up2;} else if (spriteNumber == 3) {image = up3;} break;
                case "down": if (spriteNumber == 1) {image = down1;} else if (spriteNumber == 2) {image = down2;} else if (spriteNumber == 3) {image = down3;} break;
                case "left": if (spriteNumber == 1) {image = left1;} else if (spriteNumber == 2) {image = left2;} else if (spriteNumber == 3) {image = left3;} break;
                case "right": if (spriteNumber == 1) {image = right1;} else if (spriteNumber == 2) {image = right2;} else if (spriteNumber == 3) {image = right3;} break;
            }

            // Mob Health Bar
            if (type == EntityTypes.TYPE_MONSTER && healthBarOn) {
                double oneScale = (double) game.tileSize / maxHealth;
                double hpBarValue = oneScale * health;

                graphics2D.setColor(Color.black);
                graphics2D.fillRect(Math.round(screenX - 2), Math.round(screenY - 17), game.tileSize + 4, 14);
                graphics2D.setColor(Color.white);
                graphics2D.fillRect(Math.round(screenX), Math.round(screenY - 15), (int)hpBarValue, 10);

                healthBarCounter++;
                if (healthBarCounter > 100) {
                    healthBarCounter = 0;
                    healthBarOn = false;
                }
            }

            if (invincible) {
                healthBarOn = true;
                healthBarCounter = 0;
                changeAlpha(graphics2D, 0.4f);
            }
            if (dying) dyingAnimation(graphics2D, 5);

            graphics2D.drawImage(image, Math.round(screenX), Math.round(screenY), null);
            changeAlpha(graphics2D, 1f);

            if (game.debugHitBoxes) {
                graphics2D.setColor(new Color(0.7f, 0, 0, 0.3f));
                graphics2D.fillRect(Math.round(screenX + solidArea.x), Math.round(screenY + solidArea.y),
                        solidArea.width, solidArea.height);
            }
        }
    }
    public void dyingAnimation(Graphics2D g2, int i) {
        dyingCounter++;

        if (dyingCounter <= i) {changeAlpha(g2, 0f);}
        if (dyingCounter > i && dyingCounter <= i * 2) {changeAlpha(g2, 1f);}
        if (dyingCounter > i * 2 && dyingCounter <= i * 3) {changeAlpha(g2, 0f);}
        if (dyingCounter > i * 3 && dyingCounter <= i * 4) {changeAlpha(g2, 1f);}
        if (dyingCounter > i * 4 && dyingCounter <= i * 5) {changeAlpha(g2, 0f);}
        if (dyingCounter > i * 5 && dyingCounter <= i * 6) {changeAlpha(g2, 1f);}
        if (dyingCounter > i * 6 && dyingCounter <= i * 7) {changeAlpha(g2, 0f);}
        if (dyingCounter > i * 7 && dyingCounter <= i * 8) {changeAlpha(g2, 1f);}
        if (dyingCounter > i * 8) {alive = false;}
    }
    public void changeAlpha(Graphics2D g2, float alphaValue) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }
    public BufferedImage registerEntitySprite(String imagePath) {
        BufferedImage image = null;
        try {
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/drawable" + imagePath + ".png"));
            } catch (IllegalArgumentException exception) {
                System.err.println("Warning: \"" + imagePath + "\" is not a valid path.");
                image = ImageIO.read(getClass().getResourceAsStream("/drawable/disabled.png"));
            }
            image = UtilityTool.scaleImage(image, game.tileSize, game.tileSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }
    public BufferedImage registerEntitySprite(String imagePath, int width, int height) {
        BufferedImage image;
        try {
            try {
                image = ImageIO.read(getClass().getResourceAsStream("/drawable" + imagePath + ".png"));
            } catch (IllegalArgumentException exception) {
                System.err.println("Warning: \"" + imagePath + "\" is not a valid path.");
                image = ImageIO.read(getClass().getResourceAsStream("/drawable/disabled.png"));
            }
            image = UtilityTool.scaleImage(image, width, height);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return image;
    }

    public int getDetected(Entity user, HashMap<Integer, Entity> target, String targetName) {
        int index = 999;

        // Check surrounding objects
        float nextWorldX = user.getLeftX();
        float nextWorldY = user.getTopY();

        switch (user.direction) {
            case "up" -> nextWorldY = user.getTopY() - 10;
            case "down" -> nextWorldY = user.getBottomY() + 10;
            case "left" -> nextWorldX = user.getLeftX() - 10;
            case "right" -> nextWorldY = user.getRightX() + 10;
        }
        float col = nextWorldX / game.tileSize;
        float row = nextWorldY / game.tileSize;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                if (target.get(i).getCol() == col
                        && target.get(i).getRow() == row
                        && target.get(i).name.equals(targetName)) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }
    public void searchPath(int goalCol, int goalRow, boolean endSearch) {
        float startCol = (worldX + solidArea.x) / game.tileSize;
        float startRow = (worldY + solidArea.y) / game.tileSize;
        game.pathFinder.setNodes(Math.round(startCol), Math.round(startRow), goalCol, goalRow);

        if (game.pathFinder.search()) {
            // Next worldX & worldY
            int nextX = game.pathFinder.pathList.getFirst().col * game.tileSize;
            int nextY = game.pathFinder.pathList.getFirst().row * game.tileSize;

            // Entity's solidArea position
            float enLeftX = worldX + solidArea.x;
            float enRightX = worldX + solidArea.x + solidArea.width;
            float enTopY = worldY + solidArea.y;
            float enBottomY = worldY + solidArea.y + solidArea.height;

            if (enTopY > nextY && enLeftX >= nextX && enRightX < nextX + game.tileSize) {
                direction = "up";
            }
            else if (enTopY < nextY && enLeftX >= nextX && enRightX < nextX + game.tileSize) {
                direction = "down";
            }
            else if (enTopY >= nextY && enBottomY < nextY + game.tileSize) {
                // Left or r=Right
                if (enLeftX > nextX) {
                    direction = "left";
                }
                if (enLeftX < nextX) {
                    direction = "right";
                }
            }
            else if (enTopY > nextY && enLeftX > nextX) {
                // Up or Left
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            }
            else if (enTopY > nextY && enLeftX < nextX) {
                // Up or Right
                direction = "up";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }
            else if (enTopY < nextY && enLeftX > nextX) {
                // Down or Left
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "left";
                }
            }
            else if (enTopY < nextY && enLeftX < nextX) {
                // Down or Right
                direction = "down";
                checkCollision();
                if (collisionOn) {
                    direction = "right";
                }
            }

            if (endSearch) {
                int nextCol = game.pathFinder.pathList.getFirst().col;
                int nextRow = game.pathFinder.pathList.getFirst().row;
                if (nextCol == goalCol && nextRow == goalRow) {
                    System.out.println("OVER");
                    onPath = false;
                }
            }
        }
    }
}
