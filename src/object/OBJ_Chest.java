package object;

import entity.Entity;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Chest extends Entity {
    public OBJ_Chest(GamePanel gamePanel) {
        super(gamePanel);

        name = "Chest";
        type = EntityTypes.TYPE_OBJECT;
        down1 = registerEntitySprite("/objects/chest", gamePanel.tileSize, gamePanel.tileSize);
    }
}
