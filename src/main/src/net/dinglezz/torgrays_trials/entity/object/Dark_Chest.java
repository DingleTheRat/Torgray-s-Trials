package net.dinglezz.torgrays_trials.entity.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.entity.LootTableHandler;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.io.Serializable;
import java.util.ArrayList;

public class Dark_Chest extends Entity implements Serializable {
    String lootTable;
    boolean opened = false;

    public Dark_Chest(TilePoint tilePoint, String lootTable) {
        super("Dark Chest", tilePoint);
        this.lootTable = lootTable;

        setDefaultValues();
    }

    public void setDefaultValues() {
        name = "Dark Chest";
        image = registerEntitySprite("entity/object/dark_chest/closed");
        image2 = registerEntitySprite("entity/object/dark_chest/opened");
        currentImage = image;
        collision = true;
    }

    @Override
    public void onInteract() {
        if (!opened) {
            Sound.playSFX("Unlock");
            Main.game.ui.uiState = States.UIStates.DIALOGUE;
            Main.game.player.cancelInventory();

            StringBuilder stringBuilder = new StringBuilder();

            if (lootTable.isEmpty()) {
                stringBuilder.append("This chest is empty :(");
            } else {
                ArrayList<Item> loot = LootTableHandler.generateLoot(LootTableHandler.lootTables.get(lootTable));
                if (loot.isEmpty()) {
                    stringBuilder.append("This chest is empty :(");
                } else {
                    stringBuilder.append("Hmmm what's in this dark old chest?");
                }

                for (Item reward : loot) {
                    if (Main.game.player.giveItem(reward)) {
                        stringBuilder.append("\n+").append(reward.amount).append(" ").append(reward.name);
                    } else {
                        stringBuilder.append("\nI can't carry all this loot :(");
                    }
                }
            }

            Main.game.ui.setCurrentDialogue(stringBuilder.toString());
            currentImage = image2;
            opened = true;
        }
    }

    @Override
    public void onPlayerHit() {}
}
