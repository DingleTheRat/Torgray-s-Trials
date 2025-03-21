package object;

import entity.Entity;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Boots extends Entity {
    public OBJ_Boots(GamePanel gamePanel) {
        super(gamePanel);

        name = "Boots";
        type = EntityTypes.TYPE_OBJECT;
        down1 = registerEntitySprite("/drawable/objects/boots", gamePanel.tileSize, gamePanel.tileSize);
    }
}
