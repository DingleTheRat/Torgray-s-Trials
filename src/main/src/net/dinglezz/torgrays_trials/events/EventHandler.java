package net.dinglezz.torgrays_trials.events;

import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;

import java.util.Objects;

public class EventHandler {
    Game game;
    EventRect[][][] eventRect;

    int previousEventX, previousEventY;
    boolean canTouchEvent;

    public EventHandler(Game game) {
        this.game = game;

        eventRect = new EventRect[game.maxMaps][game.maxWorldCol][game.maxWorldRow];
        for (int map : game.tileManager.mapNumbers.values()) {
            int col = 0;
            int row = 0;
            while (col < game.maxWorldRow && row < game.maxWorldRow) {
                eventRect[map][col][row] = new EventRect();
                eventRect[map][col][row].x = 23;
                eventRect[map][col][row].y = 23;
                eventRect[map][col][row].width = 2;
                eventRect[map][col][row].height = 2;
                eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
                eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

                col++;
                if (col == game.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    public void checkEvent() {
        // Check if player is more then one tile away from last event
        int xDistance = Math.abs(game.player.worldX - previousEventX);
        int yDistance = Math.abs(game.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > game.tileSize) {canTouchEvent = true;}

        if (canTouchEvent) {
            // Main Island Map
            if (hit("Main Island", 27, 16, "right")) {damagePit(States.STATE_DIALOGUE);}
            if (hit("Main Island", 23, 19, "any")) {damagePit(States.STATE_DIALOGUE);}
            if (hit("Main Island", 22, 40, "any")) {damagePit(States.STATE_DIALOGUE);}
            if (hit("Main Island", 14, 26, "any")) {damagePit(States.STATE_DIALOGUE);}
            if (hit("Main Island", 14, 26, "any")) {damagePit(States.STATE_DIALOGUE);}
            if (hit("Main Island", 36, 9, "any")) {damagePit(States.STATE_DIALOGUE);}
            if (hit("Main Island", 37, 34, "any")) {damagePit(States.STATE_DIALOGUE);}
            if (hit("Main Island", 35, 38, "any")) {damagePit(States.STATE_DIALOGUE);}
            if (hit("Main Island", 9, 30, "any")) {damagePit(States.STATE_DIALOGUE);}
            if (hit("Main Island", 23, 12, "up")) {healingPond(States.STATE_DIALOGUE);}
        }
    }
    public boolean hit(String map, int col, int row, String direction) {
        boolean hit = false;

        if (Objects.equals(map, game.currentMap)) {
            game.player.solidArea.x = game.player.worldX + game.player.solidArea.x;
            game.player.solidArea.y = game.player.worldY + game.player.solidArea.y;
            eventRect[game.tileManager.mapNumbers.get(map)][col][row].x = col * game.tileSize + eventRect[game.tileManager.mapNumbers.get(map)][col][row].x;
            eventRect[game.tileManager.mapNumbers.get(map)][col][row].y = row * game.tileSize + eventRect[game.tileManager.mapNumbers.get(map)][col][row].y;

            if (game.player.solidArea.intersects(eventRect[game.tileManager.mapNumbers.get(map)][col][row]) && !eventRect[game.tileManager.mapNumbers.get(map)][col][row].eventDone) {
                if (game.player.direction.contentEquals(direction) || direction.contentEquals("any")) {
                    hit = true;
                    previousEventX = game.player.worldX;
                    previousEventY = game.player.worldY;
                }
            }
            game.player.solidArea.x = game.player.solidAreaDefaultX;
            game.player.solidArea.y = game.player.solidAreaDefaultY;
            eventRect[game.tileManager.mapNumbers.get(map)][col][row].x = eventRect[game.tileManager.mapNumbers.get(map)][col][row].eventRectDefaultX;
            eventRect[game.tileManager.mapNumbers.get(map)][col][row].y = eventRect[game.tileManager.mapNumbers.get(map)][col][row].eventRectDefaultY;
        }

        return hit;
    }
    public void damagePit(States gameState) {
        game.gameState = gameState;
        game.ui.currentDialogue = "Dang it, I feel into a pit!";
        game.player.health -= 1;
        canTouchEvent = false;
    }
    public void healingPond(States gameState) {
        if (game.keyHandler.interactKeyPressed) {
            game.gameState = gameState;
            game.player.attackCanceled = true;
            game.ui.currentDialogue = "*Drinks water* /nYay, the pond of healing healed me!";
            game.player.health = game.player.maxHealth;
            game.assetSetter.setMonster();
        }
    }
}
