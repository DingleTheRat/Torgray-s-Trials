package mob;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MOB_Dracore extends Entity {
    public MOB_Dracore(GamePanel gp) {
        super(gp);

        name = "Dracore";
        type = 2;
        speed = 1;
        maxHealth = 4;
        health = maxHealth;
        
        getImage();
    }
    public void getImage() {
        up1 = registerEntitySprite("/mob/dracore_1");
        up2 = registerEntitySprite("/mob/dracore_2");
        up3 = registerEntitySprite("/mob/dracore_3");
        
        down1 = registerEntitySprite("/mob/dracore_1");
        down2 = registerEntitySprite("/mob/dracore_2");
        down3 = registerEntitySprite("/mob/dracore_3");
        
        left1 = registerEntitySprite("/mob/dracore_1");
        left2 = registerEntitySprite("/mob/dracore_2");
        left3 = registerEntitySprite("/mob/dracore_3");
        
        right1 = registerEntitySprite("/mob/dracore_1");
        right2 = registerEntitySprite("/mob/dracore_2");
        right3 = registerEntitySprite("/mob/dracore_3");
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
}
