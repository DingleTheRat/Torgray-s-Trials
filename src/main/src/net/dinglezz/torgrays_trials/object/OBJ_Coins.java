package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.Sound;

public class OBJ_Coins extends Entity {
    Game game;

    public OBJ_Coins(Game game, int amount) {
        super(game);
        this.game = game;
        this.amount = amount;

        name = "Coins";
        maxStack = 2;
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_PICKUP_ONLY);
        tags.add(EntityTags.TAG_NON_SELLABLE);
        down1 = registerEntitySprite("/objects/coin");
        description = "Precious pieces of gold";
    }

    @Override
    public boolean use(Entity entity) {
        game.player.coins += amount;
        Sound.playSFX("Coin");

        // Grammar check
        if (amount == 1) {
            game.ui.addMiniNotification("+" + amount + " Coin");
        } else {
            game.ui.addMiniNotification("+" + amount + " Coins");
        }
        return true;
    }
}
