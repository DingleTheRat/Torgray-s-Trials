package net.dinglezz.torgrays_trials.entity.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Gate extends Entity {
    public Gate(TilePoint tilePoint) {
        super("Gate", tilePoint);
        image = registerEntitySprite("entity/object/gate");
        currentImage = image;
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void onPlayerHit() {}
}
