package net.dinglezz.torgrays_trials.entity.object;

import net.dinglezz.torgrays_trials.entity.item.Item;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Heart extends Item {
    public Heart(TilePoint tilePoint) {
        super("Heart", tilePoint);

        image = registerEntitySprite("entity/object/heart/heart");
        image2 = registerEntitySprite("entity/object/heart/half_heart");
        image3 = registerEntitySprite("entity/object/heart/lost_heart");
    }
}
