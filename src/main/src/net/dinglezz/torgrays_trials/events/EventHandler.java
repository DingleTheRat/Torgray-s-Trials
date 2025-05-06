package net.dinglezz.torgrays_trials.events;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.MapHandler;

import java.util.Objects;

public class EventHandler {
    static EventRect[][][] eventRect = new EventRect[Main.game.maxMaps][Main.game.maxWorldCol][Main.game.maxWorldRow];;
    static int previousEventX, previousEventY;
    static boolean canTouchEvent;
    static public int nextCol, nextRow;
    static public String nextMap, nextDirection;

    public static void setup() {
        for (int map : MapHandler.mapNumbers.values()) {
            int col = 0;
            int row = 0;
            while (col < Main.game.maxWorldRow && row < Main.game.maxWorldRow) {
                eventRect[map][col][row] = new EventRect();
                eventRect[map][col][row].x = 23;
                eventRect[map][col][row].y = 23;
                eventRect[map][col][row].width = 2;
                eventRect[map][col][row].height = 2;
                eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
                eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

                col++;
                if (col == Main.game.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    public static void checkEvent() {
        // Check if player is more then one tile away from the last event
        int xDistance = Math.abs(Main.game.player.worldX - previousEventX);
        int yDistance = Math.abs(Main.game.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > Main.game.tileSize) {canTouchEvent = true;}

        if (canTouchEvent) {
            // Main Island Map
                // Pits
                if (hit("Main Island", 23, 19)) {damagePit();}
                if (hit("Main Island", 22, 40)) {damagePit();}
                if (hit("Main Island", 14, 26)) {damagePit();}
                if (hit("Main Island", 14, 26)) {damagePit();}
                if (hit("Main Island", 36, 9)) {damagePit();}
                if (hit("Main Island", 37, 34)) {damagePit();}
                if (hit("Main Island", 35, 38)) {damagePit();}
                if (hit("Main Island", 9, 30)) {damagePit();}

                // Others
                if (hit("Main Island", 23, 12, "up")) {healingPond();}
                if (hit("Main Island", 10, 39, "up")) {teleport("Coiner's Shop", "up");}

            // Coiner's Shop Map
                if (hit("Coiner's Shop", 12, 13, "down")) {teleport("Main Island", 10, 39, "down");}
                if (hit("Coiner's Shop", 12, 11, "up")) {speak(Main.game.npc.get(Main.game.currentMap).get(0));}
        }
    }
    public static boolean hit(String map, int col, int row) {
        boolean hit = false;

        if (Objects.equals(map, Main.game.currentMap)) {
            Main.game.player.solidArea.x = Main.game.player.worldX + Main.game.player.solidArea.x;
            Main.game.player.solidArea.y = Main.game.player.worldY + Main.game.player.solidArea.y;
            eventRect[MapHandler.mapNumbers.get(map)][col][row].x = col * Main.game.tileSize + eventRect[MapHandler.mapNumbers.get(map)][col][row].x;
            eventRect[MapHandler.mapNumbers.get(map)][col][row].y = row * Main.game.tileSize + eventRect[MapHandler.mapNumbers.get(map)][col][row].y;

            if (Main.game.player.solidArea.intersects(eventRect[MapHandler.mapNumbers.get(map)][col][row]) && !eventRect[MapHandler.mapNumbers.get(map)][col][row].eventDone) {
                hit = true;
                previousEventX = Main.game.player.worldX;
                previousEventY = Main.game.player.worldY;
            }
            Main.game.player.solidArea.x = Main.game.player.solidAreaDefaultX;
            Main.game.player.solidArea.y = Main.game.player.solidAreaDefaultY;
            eventRect[MapHandler.mapNumbers.get(map)][col][row].x = eventRect[MapHandler.mapNumbers.get(map)][col][row].eventRectDefaultX;
            eventRect[MapHandler.mapNumbers.get(map)][col][row].y = eventRect[MapHandler.mapNumbers.get(map)][col][row].eventRectDefaultY;
        }

        return hit;
    }
    public static boolean hit(String map, int col, int row, String direction) {
        boolean hit = false;

        if (Objects.equals(map, Main.game.currentMap)) {
            Main.game.player.solidArea.x = Main.game.player.worldX + Main.game.player.solidArea.x;
            Main.game.player.solidArea.y = Main.game.player.worldY + Main.game.player.solidArea.y;
            eventRect[MapHandler.mapNumbers.get(map)][col][row].x = col * Main.game.tileSize + eventRect[MapHandler.mapNumbers.get(map)][col][row].x;
            eventRect[MapHandler.mapNumbers.get(map)][col][row].y = row * Main.game.tileSize + eventRect[MapHandler.mapNumbers.get(map)][col][row].y;

            if (Main.game.player.solidArea.intersects(eventRect[MapHandler.mapNumbers.get(map)][col][row]) && !eventRect[MapHandler.mapNumbers.get(map)][col][row].eventDone) {
                if (Main.game.player.direction.contentEquals(direction) || direction.contentEquals("any")) {
                    hit = true;
                    previousEventX = Main.game.player.worldX;
                    previousEventY = Main.game.player.worldY;
                }
            }
            Main.game.player.solidArea.x = Main.game.player.solidAreaDefaultX;
            Main.game.player.solidArea.y = Main.game.player.solidAreaDefaultY;
            eventRect[MapHandler.mapNumbers.get(map)][col][row].x = eventRect[MapHandler.mapNumbers.get(map)][col][row].eventRectDefaultX;
            eventRect[MapHandler.mapNumbers.get(map)][col][row].y = eventRect[MapHandler.mapNumbers.get(map)][col][row].eventRectDefaultY;
        }

        return hit;
    }
    public static void damagePit() {
        Main.game.gameState = States.GameStates.STATE_DIALOGUE;
        Main.game.ui.currentDialogue = "Dang it, I feel into a pit!";
        Main.game.player.health -= 1;
        canTouchEvent = false;
    }
    public static void healingPond() {
        if (Main.game.inputHandler.interactKeyPressed) {
            Main.game.gameState = States.GameStates.STATE_DIALOGUE;
            Main.game.player.attackCanceled = true;
            Main.game.ui.currentDialogue = "*Drinks water* \nHuh, nothing happened :/";
//            Main.game.player.health = Main.game.player.maxHealth;
//            Main.game.assetSetter.seteMonsters();
        }
    }
    public static void teleport(String map, String direction) {
        canTouchEvent = false;
        Sound.playSFX("Teleport");

        nextMap = map;
        nextCol = Integer.MIN_VALUE;
        nextRow = Integer.MIN_VALUE;
        nextDirection = direction;
        Main.game.ui.actionMethod = "transitionTeleport";
        Main.game.ui.transitionOpenSpeed = 0.02f;
        Main.game.ui.transitionCloseSpeed = 0.02f;
        Main.game.ui.transitioning = true;
    }
    public static void teleport(String map, int col, int row, String direction) {
        canTouchEvent = false;
        Sound.playSFX("Teleport");

        nextMap = map;
        nextCol = Main.game.tileSize * col;
        nextRow = Main.game.tileSize * row;
        nextDirection = direction;
        Main.game.ui.actionMethod = "transitionTeleport";
        Main.game.ui.transitionOpenSpeed = 0.02f;
        Main.game.ui.transitionCloseSpeed = 0.02f;
        Main.game.ui.transitioning = true;
    }
    public static void speak(Entity entity) {
        if (Main.game.inputHandler.interactKeyPressed) {
            Main.game.gameState = States.GameStates.STATE_DIALOGUE;
            Main.game.player.attackCanceled = true;
            entity.speak(false);
        }
    }
}
