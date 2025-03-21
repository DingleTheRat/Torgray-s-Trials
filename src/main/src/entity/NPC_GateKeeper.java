package entity;

import main.GamePanel;

import java.util.Random;

public class NPC_GateKeeper extends Entity{
    public NPC_GateKeeper(GamePanel gamePanel) {
        super(gamePanel);
        type = EntityTypes.TYPE_NPC;
        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }

    public void getImage() {
        up1 = registerEntitySprite("/drawable/npc/gatekeeper_up_1", gamePanel.tileSize, gamePanel.tileSize);
        up2 = registerEntitySprite("/drawable/npc/gatekeeper_up_2", gamePanel.tileSize, gamePanel.tileSize);
        up3 = registerEntitySprite("/drawable/npc/gatekeeper_up_3", gamePanel.tileSize, gamePanel.tileSize);

        down1 = registerEntitySprite("/drawable/npc/gatekeeper_down_1", gamePanel.tileSize, gamePanel.tileSize);
        down2 = registerEntitySprite("/drawable/npc/gatekeeper_down_2", gamePanel.tileSize, gamePanel.tileSize);
        down3 = registerEntitySprite("/drawable/npc/gatekeeper_down_3", gamePanel.tileSize, gamePanel.tileSize);

        left1 = registerEntitySprite("/drawable/npc/gatekeeper_left_1", gamePanel.tileSize, gamePanel.tileSize);
        left2 = registerEntitySprite("/drawable/npc/gatekeeper_left_2", gamePanel.tileSize, gamePanel.tileSize);
        left3 = registerEntitySprite("/drawable/npc/gatekeeper_left_3", gamePanel.tileSize, gamePanel.tileSize);

        right1 = registerEntitySprite("/drawable/npc/gatekeeper_right_1", gamePanel.tileSize, gamePanel.tileSize);
        right2 = registerEntitySprite("/drawable/npc/gatekeeper_right_2", gamePanel.tileSize, gamePanel.tileSize);
        right3 = registerEntitySprite("/drawable/npc/gatekeeper_right_3", gamePanel.tileSize, gamePanel.tileSize);
    }
    public void setDialogue() {
        dialogues.put(0, "Hey there partner!");
        dialogues.put(1, "Let me tell you a very special secret");
        dialogues.put(2, "This game is for a science fair");
        dialogues.put(3, "The person who made this game, /ndingle,is actually super screwed for /nthe science fair");
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
