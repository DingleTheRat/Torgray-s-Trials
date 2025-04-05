package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(Game game) {
        super(game);
        name = "Heart";
        type = EntityTypes.TYPE_OBJECT;

        image = registerEntitySprite("/objects/heart/heart");
        image2 = registerEntitySprite("/objects/heart/half_heart");
        image3 = registerEntitySprite("/objects/heart/lost_heart");
    }
}
