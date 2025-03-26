package net.dinglezz.torgrays_trials.npc;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

import java.util.Random;

public class NPC_GateKeeper extends Entity {
    Game game;

    public NPC_GateKeeper(Game game) {
        super(game);
        this.game = game;
        type = EntityTypes.TYPE_NPC;
        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }

    public void getImage() {
        up1 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_up_1");
        up2 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_up_2");
        up3 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_up_3");

        down1 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_down_1");
        down2 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_down_2");
        down3 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_down_3");

        left1 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_left_1");
        left2 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_left_2");
        left3 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_left_3");

        right1 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_right_1");
        right2 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_right_2");
        right3 = registerEntitySprite("/drawable/npc/gatekeeper/gatekeeper_right_3");
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
    @Override
    public void speak(boolean facePlayer) {
        // make this character do specific stuff
        super.speak(true);
    }
}
