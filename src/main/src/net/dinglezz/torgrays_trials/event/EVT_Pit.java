package net.dinglezz.torgrays_trials.event;

import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONObject;

public class EVT_Pit extends Event {
    public EVT_Pit(TilePoint tilePoint, JSONObject parameters) {
        super(tilePoint, parameters);
    }

    @Override
    public void onHit() {
        // Torgray being annoyed dialogue
        Main.game.gameState = States.GameStates.DIALOGUE;
        Main.game.ui.currentDialogue = "Dang it, I feel into a pit!";

        // Take some health >:)
        Main.game.player.health -= 1;
    }

    @Override
    public void whileHit() {}
}
