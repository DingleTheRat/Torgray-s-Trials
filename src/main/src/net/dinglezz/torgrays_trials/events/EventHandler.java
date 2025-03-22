package net.dinglezz.torgrays_trials.events;

import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;

public class EventHandler {
    Game game;
    EventRect[][] eventRect;

    int previousEventX, previousEventY;
    boolean canTouchEvent;

    public EventHandler(Game game) {
        this.game = game;

        eventRect = new EventRect[game.maxWorldCol][game.maxWorldRow];
        int col = 0;
        int row = 0;
        while (col < game.maxWorldRow && row < game.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == game.maxWorldCol) {
                col = 0;
                row++;
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
            if (hit(27, 16, "right")) {damagePit(27, 16, States.STATE_DIALOGUE);}
            if (hit(23, 19, "any")) {damagePit(23, 19, States.STATE_DIALOGUE);}
            if (hit(22, 40, "any")) {damagePit(22, 40, States.STATE_DIALOGUE);}
            if (hit(14, 26, "any")) {damagePit(14, 26, States.STATE_DIALOGUE);}
            if (hit(14, 26, "any")) {damagePit(9, 30, States.STATE_DIALOGUE);}
            if (hit(36, 9, "any")) {damagePit(36, 9, States.STATE_DIALOGUE);}
            if (hit(37, 34, "any")) {damagePit(37, 34, States.STATE_DIALOGUE);}
            if (hit(35, 38, "any")) {damagePit(35, 38, States.STATE_DIALOGUE);}
            if (hit(9, 30, "any")) {damagePit(9, 30, States.STATE_DIALOGUE);}
            if (hit(23, 12, "up")) {healingPond(23, 12, States.STATE_DIALOGUE);}
        }
    }
    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false;

        game.player.solidArea.x = game.player.worldX + game.player.solidArea.x;
        game.player.solidArea.y = game.player.worldY + game.player.solidArea.y;
        eventRect[col][row].x = col * game.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * game.tileSize + eventRect[col][row].y;

        if (game.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (game.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;
                previousEventX = game.player.worldX;
                previousEventY = game.player.worldY;
            }
        }
        game.player.solidArea.x = game.player.solidAreaDefaultX;
        game.player.solidArea.y = game.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }
    public void damagePit(int col, int row, States gameState) {
        game.gameState = gameState;
        game.ui.currentDialogue = "Dang it, I feel into a pit!";
        game.player.health -= 1;
        canTouchEvent = false;
    }
    public void healingPond(int col, int row, States gameState) {
        if (game.keyHandler.interactKeyPressed) {
            game.gameState = gameState;
            game.player.attackCanceled = true;
            game.ui.currentDialogue = "*Drinks water* /nYay, the pond of healing healed me!";
            game.player.health = game.player.maxHealth;
            game.assetSetter.setMonster();
        }
    }
}
