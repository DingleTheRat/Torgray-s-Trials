package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Lantern extends Entity {
    Game game;

    public OBJ_Lantern(Game game) {
        super(game);
        this.game = game;

        name = "Lantern";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_LIGHT);
        tags.add(EntityTags.TAG_NON_SELLABLE);
        lightRadius = 150;
        down1 = registerEntitySprite("/drawable/objects/lantern", game.tileSize, game.tileSize);
        description = "/nSHINE BRIGHTTTTTT /nTONIGHTTTTTT /lyr /nLight Radius: " + lightRadius;
    }
}
