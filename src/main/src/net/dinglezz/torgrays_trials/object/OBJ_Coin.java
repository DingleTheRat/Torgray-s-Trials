package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Coin extends Entity {
    Game gp;

    public OBJ_Coin(Game game) {
        super(game);
        this.gp = game;

        name = "Coin";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_PICKUPONLY);
        down1 = registerEntitySprite("/drawable/objects/coin", game.tileSize, game.tileSize);
    }

    public boolean use(Entity entity) {
        gp.playSound("Coin");
        gp.ui.addMessage("+1 Coin");
        gp.player.coins++;
        return true;
    }
}
