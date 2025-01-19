package events;

import main.GamePanel;
import main.States;

public class EventHandler {
    GamePanel gp;
    EventRect[][] eventRect;

    int previousEventX, previousEventY;
    boolean canTouchEvent;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
        int col = 0;
        int row = 0;
        while (col < gp.maxWorldRow && row < gp.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent() {
        // Check if player is more then one tile away from last event
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > gp.tileSize) {canTouchEvent = true;}

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
            if (hit(23, 12, "up")) {healingPond(23, 12, States.STATE_DIALOGUE);} else {gp.ui.interactable = false;}
        }
    }
    public boolean hit(int col, int row, String reqDirection) {
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;

        if (gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;
                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }
    public void damagePit(int col, int row, States gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialogue = "Dang it, I feel into a pit!";
        gp.player.health -= 1;
        canTouchEvent = false;
    }
    public void healingPond(int col, int row, States gameState) {
        if (gp.keyH.interactKeyPressed) {
            gp.ui.interactable = false;
            gp.gameState = gameState;
            gp.player.attackCanceled = true;
            gp.ui.currentDialogue = "*Drinks water* /n Yay, the pond of healing healed me!";
            gp.player.health = gp.player.maxHealth;
            gp.assetS.setMonster();
        } else {
            gp.ui.interactable = true;
        }
    }
}
