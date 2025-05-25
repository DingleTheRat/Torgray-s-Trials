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

    /**
     * Checks the player's interaction with events based on their current position
     * and triggers the appropriate event methods.
     * <p>
     * This method calculates the distance between the player's current position
     * and the last recorded event position to determine if the player can trigger
     * a new event. If the player is within the proximity of an event, it calls
     * event-specific methods:
     * <p>
     * - {@code onHit()} is called when the player enters the event for the first time.
     * - {@code whileHit()} is repeatedly called while the player stays in the event.
     * - {@code onLeave()} is called when the player exits the event.
     * <p>
     * The method iteratively checks all events for potential interaction.
     */
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
                event.wasInEvent = true;
            } else {
                // If the player is not in the event anymore, call the onLeave method
                if (event.wasInEvent) {
                    event.onLeave();
                    event.wasInEvent = false;
                }
            }
        }
    }

    /**
     * Checks if the player has collided with a specific event rectangle
     * corresponding to the provided tile point and triggers the event if necessary.
     *
     * The method calculates the player's position and the event rectangle's position
     * and checks if they intersect. If a collision is detected and the event has not
     * already been triggered, it updates event states accordingly.
     *
     * @param tilePoint the tile point representing the location and map of the event
     *                  to check collision with
     * @return {@code true} if a collision with the event rectangle occurs, {@code false} otherwise
     */
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
