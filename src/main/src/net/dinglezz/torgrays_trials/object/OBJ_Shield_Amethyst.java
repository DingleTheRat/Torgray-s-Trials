package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Shield_Amethyst extends Entity {
    public OBJ_Shield_Amethyst(Game game) {
        super(game);

        name = "Amethyst Shield";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_AMETHIST);
        tags.add(EntityTags.TAG_SHIELD);
<<<<<<<< HEAD:src/main/src/object/OBJ_Shield_Amethyst.java
        down1 = registerEntitySprite("/drawable/objects/amethyst_shield", gamePanel.tileSize, gamePanel.tileSize);
========
        down1 = registerEntitySprite("/objects/amethyst_shield", game.tileSize, game.tileSize);
>>>>>>>> origin/main:src/main/src/net/dinglezz/torgrays_trials/object/OBJ_Shield_Amethyst.java
        defenceValue = 2;
        description = "/n A majestic purple shield /n Defence: +" + defenceValue;
    }
}
