package net.dinglezz.torgrays_trials.event;

import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EventHandler {
    public static final HashMap<TilePoint, EventRectangle> eventRectangles = new HashMap<>();
    public static final ArrayList<Event> events = new ArrayList<>();
    private static int previousEventX, previousEventY;
    private static boolean canTouchEvent;

    /// Checks if the player is in an event and calls the onHit/wileHit method
    public static void checkEvent() {
        // Check if player is more then one tile away from the last event
        int xDistance = Math.abs(Main.game.player.worldX - previousEventX);
        int yDistance = Math.abs(Main.game.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > Main.game.tileSize) canTouchEvent = true;

        // Loop through all the events and see if the player is in an event
        for (Event event : events) {
            if (hit(event.tilePoint)) {
                // If it's the first time touching the event, call the onHit method
                if (canTouchEvent) {
                    event.onHit();
                    canTouchEvent = false;
                }
                // Call the whileHit method while the player is in the event
                event.whileHit();
            }
        }
    }

    /// Checks if the player is in a tile with an event rectangle
    public static boolean hit(TilePoint tilePoint) {
        boolean hit = false;

        if (Objects.equals(tilePoint.map(), Main.game.currentMap)) {
            Main.game.player.solidArea.x = Main.game.player.worldX + Main.game.player.solidArea.x;
            Main.game.player.solidArea.y = Main.game.player.worldY + Main.game.player.solidArea.y;
            eventRectangles.get(tilePoint).x = tilePoint.col() * Main.game.tileSize + eventRectangles.get(tilePoint).x;
            eventRectangles.get(tilePoint).y = tilePoint.row() * Main.game.tileSize + eventRectangles.get(tilePoint).y;

            if (Main.game.player.solidArea.intersects(eventRectangles.get(tilePoint)) && !eventRectangles.get(tilePoint).eventDone) {
                hit = true;
                previousEventX = Main.game.player.worldX;
                previousEventY = Main.game.player.worldY;
            }
            Main.game.player.solidArea.x = Main.game.player.solidAreaDefaultX;
            Main.game.player.solidArea.y = Main.game.player.solidAreaDefaultY;
            eventRectangles.get(tilePoint).x = eventRectangles.get(tilePoint).eventRectangleDefaultX;
            eventRectangles.get(tilePoint).y = eventRectangles.get(tilePoint).eventRectangleDefaultY;
        }

        return hit;
    }
}
