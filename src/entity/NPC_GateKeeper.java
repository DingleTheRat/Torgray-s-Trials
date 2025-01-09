package entity;

import main.GamePanel;

import java.util.Random;

public class NPC_GateKeeper extends Entity{
    public NPC_GateKeeper(GamePanel gp) {
        super(gp);
        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }

    public void getImage() {
        up1 = registerEntitySprite("/npc/oldman_up_1");
        up2 = registerEntitySprite("/npc/oldman_up_2");

        down1 = registerEntitySprite("/npc/oldman_down_1");
        down2 = registerEntitySprite("/npc/oldman_down_2");

        left1 = registerEntitySprite("/npc/oldman_left_1");
        left2 = registerEntitySprite("/npc/oldman_left_2");

        right1 = registerEntitySprite("/npc/oldman_right_1");
        right2 = registerEntitySprite("/npc/oldman_right_2");
    }
    public void setDialogue() {
        dialogues[0] = "Hey there partner!";
        dialogues[1] = "Let me tell you a very special secret";
        dialogues[2] = "This game is actually for a science fair";
        dialogues[3] = "The person who made this game, dingle, /n is actually super screwed for the science /n fair";
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
    public void speak() {
        // make this character do specific stuff

        super.speak();
    }
}
