package object;

import entity.Entity;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Boots extends Entity {
    public OBJ_Boots(GamePanel gp) {
        super(gp);

        name = "Boots";
        type = EntityTypes.TYPE_OBJECT;
        down1 = registerEntitySprite("/objects/boots", gp.tileSize, gp.tileSize);
    }
}
