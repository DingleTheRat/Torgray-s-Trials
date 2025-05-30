package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.Sound;

public class OBJ_Key extends Entity {
    Game game;

    public OBJ_Key(Game game) {
        super(game);
        this.game = game;

        name = "Key";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_CONSUMABLE);
        down1 = registerEntitySprite("/object/key");
        description = "Probably opens a gate";
        maxStack = 4;
        price = 6;
    }
    public boolean use(Entity entity) {

        int objIndex = getDetected(entity, game.object.get(game.currentMap), "Gate");
        if (objIndex != 999) {
            game.ui.addMiniNotification("-1 Key");
            Sound.playSFX("Unlock");
            game.object.get(game.currentMap).set(objIndex, null);
            return true;
        } else {
            return false;
        }
    }
}
