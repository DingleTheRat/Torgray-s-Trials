package net.dinglezz.torgrays_trials.entity.item;

import net.dinglezz.torgrays_trials.entity.mob.Mob;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class World_Map extends Item {
    public World_Map(TilePoint tilePoint) {
        super("World Map", tilePoint);
        icon = registerEntitySprite("entity/item/map");
        currentImage = icon;
        tags.add(ItemTags.TAG_CONSUMABLE);
        description = "A handy map of the world, \nwithout that annoying \ndarkness.";
        price = 8;
    }
    @Override
    public boolean use(Mob mob) {
        Main.game.ui.uiState = States.UIStates.MAP;
        Main.game.inputHandler.upPressed = false;
        Main.game.inputHandler.downPressed = false;
        Main.game.inputHandler.leftPressed = false;
        Main.game.inputHandler.rightPressed = false;
        return false;
    }
}
