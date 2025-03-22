package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Shield_Iron extends Entity {
    public OBJ_Shield_Iron(Game game) {
        super(game);

        name = "Iron Shield";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_IRON);
        tags.add(EntityTags.TAG_SHIELD);
<<<<<<<< HEAD:src/main/src/object/OBJ_Shield_Iron.java
        down1 = registerEntitySprite("/drawable/objects/iron_shield", gamePanel.tileSize, gamePanel.tileSize);
========
        down1 = registerEntitySprite("/objects/iron_shield", game.tileSize, game.tileSize);
>>>>>>>> origin/main:src/main/src/net/dinglezz/torgrays_trials/object/OBJ_Shield_Iron.java
        defenceValue = 1;
        description = "/n A shiny iron shield /n Defence: +" + defenceValue;
    }
}
