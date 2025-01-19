package entity;

import main.GamePanel;

import java.util.Random;

public class NPC_GateKeeper extends Entity{
    public NPC_GateKeeper(GamePanel gp) {
        super(gp);
        type = EntityTypes.TYPE_NPC;
        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }

    public void getImage() {
        up1 = registerEntitySprite("/npc/gatekeeper_up_1", gp.tileSize, gp.tileSize);
        up2 = registerEntitySprite("/npc/gatekeeper_up_2", gp.tileSize, gp.tileSize);
        up3 = registerEntitySprite("/npc/gatekeeper_up_3", gp.tileSize, gp.tileSize);

        down1 = registerEntitySprite("/npc/gatekeeper_down_1", gp.tileSize, gp.tileSize);
        down2 = registerEntitySprite("/npc/gatekeeper_down_2", gp.tileSize, gp.tileSize);
        down3 = registerEntitySprite("/npc/gatekeeper_down_3", gp.tileSize, gp.tileSize);

        left1 = registerEntitySprite("/npc/gatekeeper_left_1", gp.tileSize, gp.tileSize);
        left2 = registerEntitySprite("/npc/gatekeeper_left_2", gp.tileSize, gp.tileSize);
        left3 = registerEntitySprite("/npc/gatekeeper_left_3", gp.tileSize, gp.tileSize);

        right1 = registerEntitySprite("/npc/gatekeeper_right_1", gp.tileSize, gp.tileSize);
        right2 = registerEntitySprite("/npc/gatekeeper_right_2", gp.tileSize, gp.tileSize);
        right3 = registerEntitySprite("/npc/gatekeeper_right_3", gp.tileSize, gp.tileSize);
    }
    public void setDialogue() {
        dialogues[0] = "Hey there partner!";
        dialogues[1] = "Let me tell you a very special secret";
        dialogues[2] = "This game is for a science fair";
        dialogues[3] = "The person who made this game, /ndingle,is actually super screwed for /nthe science fair";
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
