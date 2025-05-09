package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.entity.LootTableHandler;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.main.States;

import java.util.ArrayList;

public class OBJ_Chest extends Entity {
    Game game;
    String lootTable;
    boolean opened = false;

    public OBJ_Chest(Game game, String lootTable) {
        super(game);
        this.game = game;
        this.lootTable = lootTable;

        setDefaultValues();
    }

    public void setDefaultValues() {
        name = "Chest";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_OBSTACLE);
        image = registerEntitySprite("/objects/chest/chest_closed");
        image2 = registerEntitySprite("/objects/chest/chest_opened");
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
            Sound.playSFX("Unlock");
            game.gameState = States.GameStates.STATE_DIALOGUE;
            game.player.attackCanceled = true;

            StringBuilder stringBuilder = new StringBuilder();

            if (lootTable.isEmpty()) {
                stringBuilder.append("This chest is empty :(");
            } else {
                ArrayList<Entity> loot = LootTableHandler.generateLoot(LootTableHandler.lootTables.get(lootTable));
                if (loot.isEmpty()) {
                    stringBuilder.append("This chest is empty :(");
                } else {
                    stringBuilder.append("Woah, this chest is shiny!");
                }

                for (Entity reward : loot) {
                    if (game.player.canObtainItem(reward)) {
                        stringBuilder.append("\n+").append(reward.amount).append(" ").append(reward.name);
                    } else {
                        stringBuilder.append("\nI can't carry all this loot :(");
                    }
                }
            }

            game.ui.currentDialogue = stringBuilder.toString();
            down1 = image2;
            opened = true;
        }
    }
}
