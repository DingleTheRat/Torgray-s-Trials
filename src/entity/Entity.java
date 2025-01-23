package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Entity {
    GamePanel gp;
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    public BufferedImage attackUp, attackDown, attackLeft, attackRight;
    public BufferedImage image, image2, image3;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public  Rectangle attackArea = new Rectangle(0, 0, 0, 0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    HashMap<Integer, String> dialogues = new HashMap<>();

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

    // Item Attributes
    public int value;
    public int attackValue;
    public int defenceValue;
    public String description = "";
    public int lightRadius;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void setAction() {}
    public void damageReaction() {}
    public void speak() {
        if (dialogues.get(dialogueIndex) == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues.get(dialogueIndex);
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up": direction = "down"; break;
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
        }
    }
    public void use(Entity entity) {}
    public void use(Entity entity, int j) {}
    public void checkDrop() {}
    public void dropItem(Entity droppedItem) {
        for (int i = 0; i < gp.obj.size(); i++) {
            if (gp.obj.get(i) == null) {
                gp.obj.put(i, droppedItem);
                gp.obj.get(i).worldX = worldX;
                gp.obj.get(i).worldY = worldY;
                break;
            }
        }
    }
    public Color getParticleColor() {return null;}
    public int getParticleSize() {return 0;}
    public int getParticleSpeed() {return 0;}
    public int getParticleMaxHealth() {return 0;}
    public void generateParticles(Entity generator, Entity target, int amount) {
        Color color = generator.getParticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxHealth = generator.getParticleMaxHealth();

        Particle p1 = new Particle(gp, target, color, size, speed, maxHealth, -2, -1);
        Particle p2 = new Particle(gp, target, color, size, speed, maxHealth, 2, -1);
        Particle p3 = new Particle(gp, target, color, size, speed, maxHealth, -2, 1);
        Particle p4 = new Particle(gp, target, color, size, speed, maxHealth, 2, 1);
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
    }

    public void update() {
        setAction();
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        int npcContact = gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.mob);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if (this.type == EntityTypes.TYPE_MOB && contactPlayer) {
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

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    public void damagePlayer(int Attack) {
        if (!gp.player.invincible) {
            gp.playSE(7);

            int damage = attack - gp.player.defence;
            if (damage <= 0) {
                damage = 1;
            }
            gp.player.health -= damage;
            gp.player.invincible = true;
        }
    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch (direction) {
                case "up": if (spriteNumber == 1) {image = up1;} else if (spriteNumber == 2) {image = up2;} else if (spriteNumber == 3) {image = up3;} break;
                case "down": if (spriteNumber == 1) {image = down1;} else if (spriteNumber == 2) {image = down2;} else if (spriteNumber == 3) {image = down3;} break;
                case "left": if (spriteNumber == 1) {image = left1;} else if (spriteNumber == 2) {image = left2;} else if (spriteNumber == 3) {image = left3;} break;
                case "right": if (spriteNumber == 1) {image = right1;} else if (spriteNumber == 2) {image = right2;} else if (spriteNumber == 3) {image = right3;} break;
            }

            // Mob Health Bar
            if (type == EntityTypes.TYPE_MOB && hpBarOn) {
                double oneScale = (double)gp.tileSize / maxHealth;
                double hpBarValue = oneScale * health;

                g2.setColor(Color.black);
                g2.fillRect( screenX - 2, screenY - 17, gp.tileSize + 4, 14);
                g2.setColor(Color.white);
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;
                if (hpBarCounter > 100) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if (invincible) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }
            if (dying) {dyingAnimation(g2, 5);}

            g2.drawImage(image, screenX, screenY, null);
            changeAlpha(g2, 1f);
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
    public BufferedImage registerEntitySprite(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
