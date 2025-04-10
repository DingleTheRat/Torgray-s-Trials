package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;

import java.util.ArrayList;
import java.util.Objects;

public class OBJ_Chest extends Entity {
    Game game;
    ArrayList<Entity> loot;
    boolean opened = false;

    public OBJ_Chest(Game game, ArrayList<Entity> loot) {
        super(game);
        this.game = game;
        this.loot = loot;

        setDefaultValues();
    }

    public void setDefaultValues() {
        name = "Chest";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_OBSTACLE);
        image = registerEntitySprite("/objects/chest_closed");
        image2 = registerEntitySprite("/objects/chest_opened");
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
        if (!opened) {
            game.playSound("Unlock");
            game.gameState = States.STATE_DIALOGUE;
            game.player.attackCanceled = true;

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Woah, this chest is shiny!");

            for (Entity reward : loot) {
                if (Objects.equals(reward.name, "Coins")) {
                    game.player.coins += reward.amount;
                    stringBuilder.append("/n+").append(reward.amount).append(" ").append(reward.name);
                } else if (game.player.canObtainItem(reward)) {
                    stringBuilder.append("/n+1 ").append(reward.name);
                } else {
                    stringBuilder.append("I can't carry all this loot :(");
                }
            }
            game.ui.currentDialogue = stringBuilder.toString();
            opened = true;
            down1 = image2;
        }
    }
}
