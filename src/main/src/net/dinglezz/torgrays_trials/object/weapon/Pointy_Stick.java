package net.dinglezz.torgrays_trials.object.weapon;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class Pointy_Stick extends Entity {
    public Pointy_Stick(Game game) {
        super(game);

        name = "Stick";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_WEAPON);
        down1 = registerEntitySprite("/object/pointy_stick");
        attackValue = 1;
        knockBackPower = 3;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "Knockback > Damage \nAttack: +" + attackValue + "\nKnockback: +" + knockBackPower;
        price = 5;
    }
}
