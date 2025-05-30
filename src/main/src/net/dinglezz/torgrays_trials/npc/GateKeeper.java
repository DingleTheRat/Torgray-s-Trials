package net.dinglezz.torgrays_trials.npc;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

import java.util.Random;

public class GateKeeper extends Entity {
    Game game;

    public GateKeeper(Game game) {
        super(game);
        this.game = game;
        type = EntityTypes.TYPE_NPC;
        direction = "down";
        speed = 1;

        // Solid Area
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        getImage();
        setDialogue();
    }

    public void getImage() {
        up1 = registerEntitySprite("/npc/gatekeeper/gatekeeper_up_1");
        up2 = registerEntitySprite("/npc/gatekeeper/gatekeeper_up_2");
        up3 = registerEntitySprite("/npc/gatekeeper/gatekeeper_up_3");

        down1 = registerEntitySprite("/npc/gatekeeper/gatekeeper_down_1");
        down2 = registerEntitySprite("/npc/gatekeeper/gatekeeper_down_2");
        down3 = registerEntitySprite("/npc/gatekeeper/gatekeeper_down_3");

        left1 = registerEntitySprite("/npc/gatekeeper/gatekeeper_left_1");
        left2 = registerEntitySprite("/npc/gatekeeper/gatekeeper_left_2");
        left3 = registerEntitySprite("/npc/gatekeeper/gatekeeper_left_3");

        right1 = registerEntitySprite("/npc/gatekeeper/gatekeeper_right_1");
        right2 = registerEntitySprite("/npc/gatekeeper/gatekeeper_right_2");
        right3 = registerEntitySprite("/npc/gatekeeper/gatekeeper_right_3");
    }
    public void setDialogue() {
        dialogues.addFirst("Hey there partner!");
        dialogues.add(1, "This place is quite dark isn't it?");
        dialogues.add(2, "I heard that it's some sort of curse that \nwas placed upon this village");
        dialogues.add(3, "Who did it though?");
    }

    @Override
    public void setAction() {
        if (onPath) {
            int goalCol = (game.player.worldX + game.player.solidArea.x) / game.tileSize;
            int goalRow = (game.player.worldY + game.player.solidArea.y) / game.tileSize;

            searchPath(goalCol, goalRow, false);
        } else {
            actionLockCounter++;
            if (actionLockCounter == 120) {
                int random = new Random().nextInt(100);

                if (random <= 25) {
                    direction = "up";
                } else if (random <= 50) {
                    direction = "down";
                } else if ( random <= 75) {
                    direction = "left";
                } else {
                    direction = "right";
                }
                actionLockCounter = 0;
            }
        }
    }
    @Override
    public void speak(boolean facePlayer) {
        super.speak(true);

        // Pathfinding
        if (game.pathFinding) {
            onPath = true;
        }
    }
}
