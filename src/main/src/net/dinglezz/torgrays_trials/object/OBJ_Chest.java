package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;

public class OBJ_Chest extends Entity {
    Game game;
    Entity loot;
    Entity loot2;
    boolean opened = false;

    public OBJ_Chest(Game game, Entity loot) {
        super(game);
        this.game = game;
        this.loot = loot;

        setDefaultValues();
    }
    public OBJ_Chest(Game game, Entity loot, Entity loot2) {
        super(game);
        this.game = game;
        this.loot = loot;
        this.loot2 = loot2;

        setDefaultValues();
    }

    public void setDefaultValues() {
        name = "Chest";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_OBSTACLE);
        image = registerEntitySprite("/objects/chest_closed");
        image2 = registerEntitySprite("/objects/chest_open");
        down1 = image;
        collision = true;

        // Solid Area
        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void interact() {
        game.gameState = States.STATE_DIALOGUE;
        game.player.attackCanceled = true;
        if (!opened) {
            game.playSound("Unlock");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Woah, this chest is so shinny! /n+1 " + loot.name);

            if (loot2 != null) {
                stringBuilder.append("/n+1 " + loot2.name);
            }

            if (loot2 != null) {
                if (!game.player.canObtainItem(loot) || !game.player.canObtainItem(loot2)) {
                    stringBuilder.append("/n... But I can't carry all this!");
                } else {
                    down1 = image2;
                    opened = true;
                }
            } else {
                if (!game.player.canObtainItem(loot)) {
                    stringBuilder.append("/n... But I can't carry all this!");
                } else {
                    down1 = image2;
                    opened = true;
                }
            }
            game.ui.currentDialogue = stringBuilder.toString();
        } else {
            game.ui.currentDialogue = "There's nothing in here anymore :(";
        }
    }
}
