package net.dinglezz.torgrays_trials.event;

import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONObject;

import java.awt.*;

public class EVT_Teleport extends Event {
    public static String nextMap;
    public static int nextCol;
    public static int nextRow;
    public static String nextDirection;

    public EVT_Teleport(TilePoint tilePoint, JSONObject parameters) {
        super(tilePoint, parameters);
    }

    @Override
    public void onHit() {
        // Play the sound (important)
        Sound.playSFX("Teleport");

        // Set all the necessary values
        nextMap = (String) getParameter("map");
        nextDirection = getParameter("direction").toString();
        if (hasParameter("col") && hasParameter("row")) {
            // Then set the col and row to the provided values
            nextCol = (int) getParameter("col");
            nextRow = (int) getParameter("row");
        } else {
            // Otherwise, set the col and row to the lowest point so it's set to the spawn point
            nextCol = Integer.MIN_VALUE;
            nextRow = Integer.MIN_VALUE;
        }

        // Set the transition settings
        Main.game.ui.transitioning = true;
        Main.game.ui.setTransitionSettings(Color.BLACK, 0.02f, 0.02f);
        Main.game.ui.actionMethod = "transitionTeleport";
    }

    @Override
    public void whileHit() {}
}
