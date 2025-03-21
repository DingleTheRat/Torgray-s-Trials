package object;

import entity.Entity;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(GamePanel gamePanel) {
        super(gamePanel);
        name = "Heart";
        type = EntityTypes.TYPE_OBJECT;

        image = registerEntitySprite("/drawable/objects/heart", gamePanel.tileSize, gamePanel.tileSize);
        image2 = registerEntitySprite("/drawable/objects/half_heart", gamePanel.tileSize, gamePanel.tileSize);
        image3 = registerEntitySprite("/drawable/objects/lost_heart", gamePanel.tileSize, gamePanel.tileSize);
    }
}
