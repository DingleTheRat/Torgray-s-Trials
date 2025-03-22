package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Sword_Iron extends Entity {
    public OBJ_Sword_Iron(Game game) {
        super(game);

        name = "Iron Sword";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_IRON);
        tags.add(EntityTags.TAG_SWORD);
<<<<<<<< HEAD:src/main/src/object/OBJ_Sword_Iron.java
        down1 = registerEntitySprite("/drawable/objects/iron_sword", gamePanel.tileSize, gamePanel.tileSize);
========
        down1 = registerEntitySprite("/objects/iron_sword", game.tileSize, game.tileSize);
>>>>>>>> origin/main:src/main/src/net/dinglezz/torgrays_trials/object/OBJ_Sword_Iron.java
        attackValue = 1;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "/n A shiny iron shield /n Attack: +" + attackValue;
    }
}
