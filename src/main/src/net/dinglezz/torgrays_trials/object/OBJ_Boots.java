package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Boots extends Entity {
    public OBJ_Boots(Game game) {
        super(game);

        name = "Boots";
        type = EntityTypes.TYPE_OBJECT;
        down1 = registerEntitySprite("/drawable/objects/boots", game.tileSize, game.tileSize);
    }
}
