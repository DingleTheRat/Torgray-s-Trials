package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Coin extends Entity {
    Game game;

    public OBJ_Coin(Game game, int amount) {
        super(game);
        this.game = game;
        this.amount = amount;

        name = "Coins";
        stackable = true;
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_PICKUP_ONLY);
        tags.add(EntityTags.TAG_NON_SELLABLE);
        down1 = registerEntitySprite("/drawable/objects/coin");
        description = "/nPrecious pieces of gold";
    }

    @Override
    public boolean use(Entity entity) {
        game.player.coins += amount;
        game.playSound("Coin");

        // Grammar check
        if (amount == 1) {
            game.ui.addMessage("+" + amount + " Coin");
        } else {
            game.ui.addMessage("+" + amount + " Coins");
        }
        return true;
    }
}
