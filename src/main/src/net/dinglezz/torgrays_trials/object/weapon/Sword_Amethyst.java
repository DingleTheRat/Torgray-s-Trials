package net.dinglezz.torgrays_trials.object.weapon;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class Sword_Amethyst extends Entity {
    public Sword_Amethyst(Game game) {
        super(game);

        name = "Amethyst Sword";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_WEAPON);
        down1 = registerEntitySprite("/object/amethyst_sword");
        attackValue = 2;
        knockBackPower = 2;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "A majestic purple sword \nAttack: +" + attackValue + "\nKnockback: +" + knockBackPower;
        price = 6;
    }
}
