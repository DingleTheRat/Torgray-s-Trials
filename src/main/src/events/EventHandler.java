package events;

import main.GamePanel;
import main.States;

public class EventHandler {
    GamePanel gamePanel;
    EventRect[][] eventRect;

    int previousEventX, previousEventY;
    boolean canTouchEvent;

    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        eventRect = new EventRect[gamePanel.maxWorldCol][gamePanel.maxWorldRow];
        int col = 0;
        int row = 0;
        while (col < gamePanel.maxWorldRow && row < gamePanel.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == gamePanel.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent() {
        // Check if player is more then one tile away from last event
        int xDistance = Math.abs(gamePanel.player.worldX - previousEventX);
        int yDistance = Math.abs(gamePanel.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > gamePanel.tileSize) {canTouchEvent = true;}

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

        gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
        gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;
        eventRect[col][row].x = col * gamePanel.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gamePanel.tileSize + eventRect[col][row].y;

        if (gamePanel.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (gamePanel.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;
                previousEventX = gamePanel.player.worldX;
                previousEventY = gamePanel.player.worldY;
            }
        }
        gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
        gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }
    public void damagePit(int col, int row, States gameState) {
        gamePanel.gameState = gameState;
        gamePanel.ui.currentDialogue = "Dang it, I feel into a pit!";
        gamePanel.player.health -= 1;
        canTouchEvent = false;
    }
    public void healingPond(int col, int row, States gameState) {
        if (gamePanel.keyHandler.interactKeyPressed) {
            gamePanel.gameState = gameState;
            gamePanel.player.attackCanceled = true;
            gamePanel.ui.currentDialogue = "*Drinks water* /nYay, the pond of healing healed me!";
            gamePanel.player.health = gamePanel.player.maxHealth;
            gamePanel.assetSetter.setMonster();
        }
    }
}
