package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.entity.LootTableHandler;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.main.States;

import java.util.ArrayList;

public class OBJ_Dark_Chest extends Entity {
    Game game;
    String lootTable;
    boolean opened = false;

    public OBJ_Dark_Chest(Game game, String lootTable) {
        super(game);
        this.game = game;
        this.lootTable = lootTable;

        setDefaultValues();
    }

    public void setDefaultValues() {
        name = "Dark Chest";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_OBSTACLE);
        image = registerEntitySprite("/object/dark_chest/dark_chest_closed");
        image2 = registerEntitySprite("/object/dark_chest/dark_chest_opened");
        down1 = image;
        collision = true;
    }

    @Override
    public void interact() {
        if (!opened) {
            Sound.playSFX("Unlock");
            game.ui.uiState = States.UIStates.DIALOGUE;
            game.player.attackCanceled = true;

            StringBuilder stringBuilder = new StringBuilder();

            if (lootTable.isEmpty()) {
                stringBuilder.append("This chest is empty :(");
            } else {
                ArrayList<Entity> loot = LootTableHandler.generateLoot(LootTableHandler.lootTables.get(lootTable));
                if (loot.isEmpty()) {
                    stringBuilder.append("This chest is empty :(");
                } else {
                    stringBuilder.append("Hmmm what's in this dark old chest?");
                }

                for (Entity reward : loot) {
                    if (game.player.canObtainItem(reward)) {
                        stringBuilder.append("\n+").append(reward.amount).append(" ").append(reward.name);
                    } else {
                        stringBuilder.append("\nI can't carry all this loot :(");
                    }
                }
            }

            game.ui.setCurrentDialogue(stringBuilder.toString());
            down1 = image2;
            opened = true;
        }
    }
}
