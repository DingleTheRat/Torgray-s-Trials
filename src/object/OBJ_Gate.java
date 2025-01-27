package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;
import main.States;

public class OBJ_Gate extends Entity {
    GamePanel gamePanel;

    public OBJ_Gate(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        name = "Gate";
        down1 = registerEntitySprite("/objects/gate", gamePanel.tileSize, gamePanel.tileSize);
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_OBSTACLE);
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
