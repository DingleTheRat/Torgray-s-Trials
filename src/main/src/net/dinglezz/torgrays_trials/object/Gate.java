package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class Gate extends Entity {
    Game game;

    public Gate(Game game) {
        super(game);
        this.game = game;

        name = "Gate";
        down1 = registerEntitySprite("/object/gate");
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_OBSTACLE);
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
