package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;

public class OBJ_Key extends Entity {
    Game game;

    public OBJ_Key(Game game) {
        super(game);
        this.game = game;

        name = "Key";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_CONSUMABLE);
        down1 = registerEntitySprite("/objects/key");
        description = "Probably opens a gate";
        stackable = true;
        price = 6;
    }
    public boolean use(Entity entity) {

        int objIndex = getDetected(entity, game.object.get(game.currentMap), "Gate");
        if (objIndex != 999) {
            game.ui.addMessage("-1 Key");
            game.playSound("Unlock");
            game.object.get(game.currentMap).put(objIndex, null);
            return true;
        } else {
            return false;
        }
    }
}
