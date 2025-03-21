package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;
import main.States;

public class OBJ_Chest extends Entity {
    GamePanel gamePanel;
    Entity loot;
    Entity loot2;
    boolean opened = false;

    public OBJ_Chest(GamePanel gamePanel, Entity loot) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        this.loot = loot;

        setDefaultValues();
    }
    public OBJ_Chest(GamePanel gamePanel, Entity loot, Entity loot2) {
        super(gamePanel);
        this.gamePanel = gamePanel;
        this.loot = loot;
        this.loot2 = loot2;

        setDefaultValues();
    }

    public void setDefaultValues() {
        name = "Chest";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_OBSTACLE);
        image = registerEntitySprite("/drawable/objects/chest_closed", gamePanel.tileSize, gamePanel.tileSize);
        image2 = registerEntitySprite("/drawable/objects/chest_open", gamePanel.tileSize, gamePanel.tileSize);
        down1 = image;
        collision = true;

        // Solid Area
        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    @Override
    public void interact() {
        gamePanel.gameState = States.STATE_DIALOGUE;
        gamePanel.player.attackCanceled = true;
        if (!opened) {
            gamePanel.playSound(3);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Woah, this chest is so shinny! /n+1 " + loot.name);

            if (loot2 != null) {
                stringBuilder.append("/n+1 " + loot2.name);
            }

            if (loot2 != null) {
                if (!gamePanel.player.canObtainItem(loot) || !gamePanel.player.canObtainItem(loot2)) {
                    stringBuilder.append("/n... But I can't carry all this!");
                } else {
                    down1 = image2;
                    opened = true;
                }
            } else {
                if (!gamePanel.player.canObtainItem(loot)) {
                    stringBuilder.append("/n... But I can't carry all this!");
                } else {
                    down1 = image2;
                    opened = true;
                }
            }
            gamePanel.ui.currentDialogue = stringBuilder.toString();
        } else {
            gamePanel.ui.currentDialogue = "There's nothing in here anymore :(";
        }
    }
}
