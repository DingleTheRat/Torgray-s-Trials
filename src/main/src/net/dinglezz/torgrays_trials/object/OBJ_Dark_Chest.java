package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.LootTable;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.main.States;

import java.util.ArrayList;
import java.util.Objects;

public class OBJ_Dark_Chest extends Entity {
    Game game;
    ArrayList<LootTable> lootTable;
    boolean opened = false;

    public OBJ_Dark_Chest(Game game, ArrayList<LootTable> lootTable) {
        super(game);
        this.game = game;
        this.lootTable = lootTable;

        setDefaultValues();
    }

    public void setDefaultValues() {
        name = "Dark Chest";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_OBSTACLE);
        image = registerEntitySprite("/objects/dark_chest/dark_chest_closed");
        image2 = registerEntitySprite("/objects/dark_chest/dark_chest_opened");
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
                ArrayList<Entity> loot = LootTable.chooseMultipleLoot(lootTable);
                if (loot.isEmpty()) {
                    stringBuilder.append("This chest is empty :(");
                } else {
                    stringBuilder.append("Hmmmm, what's in this old dark chest?");
                }

                for (Entity reward : loot) {
                    if (reward == LootTable.RANDOM_COIN) {
                        reward = LootTable.chooseSingleLoot(LootTable.LOOT_TABLE_DARK_CHEST_COINS);
                        game.player.coins += reward.amount;
                        if (reward.amount == 1) {
                            stringBuilder.append("\n+").append(reward.amount).append(" Coin");
                        } else {
                            stringBuilder.append("\n+").append(reward.amount).append(" Coins");
                        }
                    } else if (game.player.canObtainItem(reward)) {
                        stringBuilder.append("\n+1 ").append(reward.name);
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
