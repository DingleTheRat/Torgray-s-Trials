package net.dinglezz.torgrays_trials.event;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONObject;

public class EVT_Speak extends Event {
    public EVT_Speak(TilePoint tilePoint, JSONObject parameters) {
        super(tilePoint, parameters);
    }

    @Override
    public void onHit() {}

    @Override
    public void whileHit() {
        Main.game.ui.uiState = States.UIStates.INTERACT;

        if (Main.game.inputHandler.interactKeyPressed) {
            // Prepare for dialogue
            Main.game.ui.uiState = States.UIStates.DIALOGUE;
            Main.game.player.attackCanceled = true;

            // Set the entity depending on the provided type
            Entity entity = switch (getParameter("type", String.class)) {
                case "npc" -> Main.game.npc.get(tilePoint.map()).get(getParameter("index", Integer.class));
                case "object" -> Main.game.object.get(tilePoint.map()).get(getParameter("index", Integer.class));
                case "monster" -> Main.game.monster.get(tilePoint.map()).get(getParameter("index", Integer.class));
                default -> throw new IllegalStateException("Unexpected value: " + getParameter("type", String.class));
            };

            // Force the entity to speak against its will >:)
            entity.speak(false);
        }
    }

    @Override
    public void onLeave() {
        Main.game.ui.uiState = States.UIStates.JUST_DEFAULT;
    }
}
