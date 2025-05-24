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
        if (Main.game.inputHandler.interactKeyPressed) {
            Main.game.gameState = States.GameStates.DIALOGUE;
            Main.game.player.attackCanceled = true;
            Main.game.ui.currentDialogue = "*Drinks water* \nHuh, nothing happened :/";
            // Disabled Stuff
            // Main.game.player.health = Main.game.player.maxHealth;
            // Main.game.assetSetter.setMonsters();
        }
    }
}
