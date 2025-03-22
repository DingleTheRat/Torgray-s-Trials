package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(Game game) {
        super(game);
        name = "Heart";
        type = EntityTypes.TYPE_OBJECT;

        image = registerEntitySprite("/drawable/objects/heart/heart", game.tileSize, game.tileSize);
        image2 = registerEntitySprite("/drawable/objects/heart/half_heart", game.tileSize, game.tileSize);
        image3 = registerEntitySprite("/drawable/objects/heart/lost_heart", game.tileSize, game.tileSize);
    }
}
