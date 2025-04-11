package net.dinglezz.torgrays_trials.object.shield;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Shield_Iron extends Entity {
    public OBJ_Shield_Iron(Game game) {
        super(game);

        name = "Iron Shield";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_SHIELD);
        down1 = registerEntitySprite("/objects/iron_shield");
        defenceValue = 1;
        description = "A shiny iron shield \nDefence: +" + defenceValue;
        price = 4;
    }
}
