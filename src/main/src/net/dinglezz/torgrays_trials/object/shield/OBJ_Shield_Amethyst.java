package net.dinglezz.torgrays_trials.object.shield;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Shield_Amethyst extends Entity {
    public OBJ_Shield_Amethyst(Game game) {
        super(game);

        name = "Amethyst Shield";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_SHIELD);
        down1 = registerEntitySprite("/object/amethyst_shield");
        defenceValue = 2;
        description = "A majestic purple shield \nDefence: +" + defenceValue;
        price = 6;
    }
}
