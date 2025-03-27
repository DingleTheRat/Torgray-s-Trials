package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Coin extends Entity {
    Game game;
    int value;

    public OBJ_Coin(Game game, int value) {
        super(game);
        this.game = game;
        this.value = value;

        name = "Coin";
        stackable = true;
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_PICKUPONLY);
        down1 = registerEntitySprite("/drawable/objects/coin", game.tileSize, game.tileSize);
        description = "/nA precious piece of gold";
    }

    @Override
    public void pickup(Entity entity, int i) {
        coins += value;
        game.ui.addMessage("+1 Coin");
//        if (game.player.canObtainItem(game.obj.get(game.currentMap).get(i))) {
//            game.playSound("Coin");
//            if (value == 1) {
//                game.ui.addMessage("+" + value + " Coin");
//            } else {
//                game.ui.addMessage("+" + value + " Coins");
//            }
//        }
    }
}
