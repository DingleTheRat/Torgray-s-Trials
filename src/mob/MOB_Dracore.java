package mob;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MOB_Dracore extends Entity {
    GamePanel gp;

    public MOB_Dracore(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Dracore";
        type = 2;
        speed = 1;
        maxHealth = 5;
        health = maxHealth;
        attack = 3;
        defence = 0;
        exp = 2;
        
        getImage();
    }
    public void getImage() {
        up1 = registerEntitySprite("/mob/dracore_1", gp.tileSize, gp.tileSize);
        up2 = registerEntitySprite("/mob/dracore_2", gp.tileSize, gp.tileSize);
        up3 = registerEntitySprite("/mob/dracore_3", gp.tileSize, gp.tileSize);
        
        down1 = registerEntitySprite("/mob/dracore_1", gp.tileSize, gp.tileSize);
        down2 = registerEntitySprite("/mob/dracore_2", gp.tileSize, gp.tileSize);
        down3 = registerEntitySprite("/mob/dracore_3", gp.tileSize, gp.tileSize);
        
        left1 = registerEntitySprite("/mob/dracore_1", gp.tileSize, gp.tileSize);
        left2 = registerEntitySprite("/mob/dracore_2", gp.tileSize, gp.tileSize);
        left3 = registerEntitySprite("/mob/dracore_3", gp.tileSize, gp.tileSize);
        
        right1 = registerEntitySprite("/mob/dracore_1", gp.tileSize, gp.tileSize);
        right2 = registerEntitySprite("/mob/dracore_2", gp.tileSize, gp.tileSize);
        right3 = registerEntitySprite("/mob/dracore_3", gp.tileSize, gp.tileSize);
    }
    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // Pick a number from 1 to 100

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }
    public void damageReaction() {
        actionLockCounter = 0;

        switch (gp.player.direction) {
            case "up": direction = "down"; break;
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
        }
    }
}
