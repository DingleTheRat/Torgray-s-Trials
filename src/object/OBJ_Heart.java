package object;

import entity.Entity;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(GamePanel gp) {
        super(gp);
        name = "Heart";
        type = EntityTypes.TYPE_OBJECT;

        image = registerEntitySprite("/objects/heart", gp.tileSize, gp.tileSize);
        image2 = registerEntitySprite("/objects/half_heart", gp.tileSize, gp.tileSize);
        image3 = registerEntitySprite("/objects/lost_heart", gp.tileSize, gp.tileSize);
    }
}
