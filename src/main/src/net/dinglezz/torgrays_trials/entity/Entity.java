package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.main.Game;
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
    public  Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    public HashMap<Integer, String> dialogues = new HashMap<>();

    // States
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNumber = 1;
    int dialogueIndex = 0;
    public boolean collisionOn = false;
    public boolean invincible = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;

    // Counters
    public int spriteCounter = 0;
    public int spriteSpeed = 10;
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;

    // Attributes
    public String name;
    public EntityTypes type;
    public ArrayList<EntityTags> tags = new ArrayList<>();
    public int speed;
    public int maxHealth;
    public int health;
    public int level;
    public  int strength;
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

    // Item Attributes
    public int value;
    public int attackValue;
    public int defenceValue;
    public String description = "";
    public int lightRadius;
    public boolean stackable = false;
    public int amount = 1;
    public int price = 0;

    public Entity(Game game) {
        this.game = game;
        down1 = registerEntitySprite("/drawable/disabled");
        down2 = registerEntitySprite("/drawable/disabled");
        down3 = registerEntitySprite("/drawable/disabled");
        up1 = registerEntitySprite("/drawable/disabled");
        up2 = registerEntitySprite("/drawable/disabled");
        up3 = registerEntitySprite("/drawable/disabled");
        left1 = registerEntitySprite("/drawable/disabled");
        left2 = registerEntitySprite("/drawable/disabled");
        left3 = registerEntitySprite("/drawable/disabled");
        right1 = registerEntitySprite("/drawable/disabled");
        right2 = registerEntitySprite("/drawable/disabled");
        right3 = registerEntitySprite("/drawable/disabled");

        attackUp = registerEntitySprite("/drawable/disabled");
        attackDown = registerEntitySprite("/drawable/disabled");
        attackLeft = registerEntitySprite("/drawable/disabled");
        right3 = registerEntitySprite("/drawable/disabled");

        image = registerEntitySprite("/drawable/disabled");
        image2 = registerEntitySprite("/drawable/disabled");
        image3 = registerEntitySprite("/drawable/disabled");
    }

    public int getLeftX() {return worldX + solidArea.x;}
    public int getRightX() {return worldX + solidArea.x + solidArea.width;}
    public int getTopY() {return worldY + solidArea.y;}
    public int getBottomY() {return worldY + solidArea.y + solidArea.height;}
    public int getCol() {return (worldX + solidArea.x) / game.tileSize;}
    public int getRow() {return (worldY + solidArea.y) / game.tileSize;}

    public void setAction() {}
    public void damageReaction() {}
    public void speak(boolean facePlayer) {
        if (dialogues.get(dialogueIndex) == null) {
            dialogueIndex = 0;
        }
        game.ui.currentDialogue = dialogues.get(dialogueIndex);
        dialogueIndex++;

        if (facePlayer) {
            switch (game.player.direction) {
                case "up": direction = "down"; break;
                case "down": direction = "up"; break;
                case "left": direction = "right"; break;
                case "right": direction = "left"; break;
            }
        }
    }
    public void interact() {}
    public boolean use(Entity entity) {return false;}
    public void checkDrop() {}
    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < game.obj.size(); i++) {
            if (game.obj.get(game.currentMap).get(i) == null) {
                game.obj.get(game.currentMap).put(i, droppedItem);
                game.obj.get(game.currentMap).get(i).worldX = worldX;
                game.obj.get(game.currentMap).get(i).worldY = worldY;
                break;
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

    public void update() {
        setAction();
        collisionOn = false;
        game.collisionChecker.checkTile(this);
        game.collisionChecker.checkObject(this, false);
        int npcContact = game.collisionChecker.checkEntity(this, game.npc);
        game.collisionChecker.checkEntity(this, game.monster);
        boolean contactPlayer = game.collisionChecker.checkPlayer(this);

        if (this.type == EntityTypes.TYPE_MONSTER && contactPlayer) {
            damagePlayer(attack);
        }

        if (!collisionOn) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        spriteCounter ++;
        if (spriteCounter > spriteSpeed) {
            if (spriteNumber == 1) {
                spriteNumber = 2;
            } else if (spriteNumber == 2) {
                spriteNumber = 3;
            } else if (spriteNumber == 3) {
                spriteNumber = 1;
            }
            spriteCounter = 0;
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    public void damagePlayer(int attack) {
        if (!game.player.invincible) {
            game.playSound("Receive Damage");

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
        int screenX = worldX - game.player.worldX + game.player.screenX;
        int screenY = worldY - game.player.worldY + game.player.screenY;

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
            if (type == EntityTypes.TYPE_MONSTER && hpBarOn) {
                double oneScale = (double) game.tileSize / maxHealth;
                double hpBarValue = oneScale * health;

                graphics2D.setColor(Color.black);
                graphics2D.fillRect( screenX - 2, screenY - 17, game.tileSize + 4, 14);
                graphics2D.setColor(Color.white);
                graphics2D.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;
                if (hpBarCounter > 100) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(graphics2D, 0.4f);
            }
            if (dying) {dyingAnimation(graphics2D, 5);}

            graphics2D.drawImage(image, screenX, screenY, null);
            changeAlpha(graphics2D, 1f);
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
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            try {
                image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            } catch (IllegalArgumentException e) {
                System.out.println("Warning: \"" + imagePath + "\" is not a valid path.");
                image = ImageIO.read(getClass().getResourceAsStream("/drawable/disabled.png"));
            }
            image = uTool.scaleImage(image, game.tileSize, game.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public BufferedImage registerEntitySprite(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            try {
                image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            } catch (IllegalArgumentException e) {
                System.out.println("Warning: \"" + imagePath + "\" is not a valid path.");
                image = ImageIO.read(getClass().getResourceAsStream("/drawable/disabled.png"));
            }
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public int getDetected(Entity user, HashMap<Integer, Entity> target, String targetName) {
        int index = 999;

        // Check surrounding objects
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch (user.direction) {
            case "up": nextWorldY = user.getTopY() - 10; break;
            case "down": nextWorldY = user.getBottomY() + 10; break;
            case "left": nextWorldX = user.getLeftX() - 10; break;
            case "right": nextWorldY = user.getRightX() + 10; break;
        }
        int col = nextWorldX / game.tileSize;
        int row = nextWorldY / game.tileSize;

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
}
