package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class Heart extends Entity {
    public Heart(Game game) {
        super(game);
        name = "Heart";
        type = EntityTypes.TYPE_OBJECT;

        image = registerEntitySprite("/object/heart/heart");
        image2 = registerEntitySprite("/object/heart/half_heart");
        image3 = registerEntitySprite("/object/heart/lost_heart");
    }
}
