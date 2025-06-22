package net.dinglezz.torgrays_trials.event;

import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONObject;

public class EVT_Healing_Pond extends Event {
    public EVT_Healing_Pond(TilePoint tilePoint, JSONObject parameters) {
        super(tilePoint, parameters);
    }

    @Override
    public void onHit() {}

    @Override
    public void whileHit() {
        Main.game.ui.uiState = States.UIStates.INTERACT;

        if (Main.game.inputHandler.interactKeyPressed) {
            Main.game.ui.uiState = States.UIStates.DIALOGUE;
            Main.game.player.cancelAttack();
            Main.game.ui.setCurrentDialogue("*Drinks water* \nHuh, nothing happened :/");
            // Disabled Stuff
            // Main.game.player.health = Main.game.player.maxHealth;
            // Main.game.assetSetter.setMonsters();
        }
    }

    @Override
    public void onLeave() {
        Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
    }
}
