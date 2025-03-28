package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Sword_Amethyst extends Entity {
    public OBJ_Sword_Amethyst(Game game) {
        super(game);

        name = "Amethyst Sword";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_WEAPON);
        down1 = registerEntitySprite("/drawable/objects/amethyst_sword", game.tileSize, game.tileSize);
        attackValue = 2;
        knockBackPower = 2;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "/n A majestic purple sword /n Attack: +" + attackValue + "/n Knockback: +" + knockBackPower;
        price = 6;
    }
}
