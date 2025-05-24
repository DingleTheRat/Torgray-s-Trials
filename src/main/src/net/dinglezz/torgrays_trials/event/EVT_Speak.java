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
        if (Main.game.inputHandler.interactKeyPressed) {
            // Prepare for dialogue
            Main.game.gameState = States.GameStates.DIALOGUE;
            Main.game.player.attackCanceled = true;

            // Set the entity depending on the provided type
            System.out.println(Main.game.npc.get(tilePoint.map()).get((int) getParameter("index")));
            Entity entity = switch ((String) getParameter("type")) {
                case "npc" -> Main.game.npc.get(tilePoint.map()).get((int) getParameter("index"));
                case "object" -> Main.game.object.get(tilePoint.map()).get((int) getParameter("index"));
                case "monster" -> Main.game.monster.get(tilePoint.map()).get((int) getParameter("index"));
                default -> throw new IllegalStateException("Unexpected value: " + getParameter("type"));
            };

            // Force the entity to speak against its will >:)
            entity.speak(false);
        }
    }
}
