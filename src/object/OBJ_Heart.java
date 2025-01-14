package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(GamePanel gp) {
        super(gp);
        name = "Heart";

        image = registerEntitySprite("/objects/heart", gp.tileSize, gp.tileSize);
        image2 = registerEntitySprite("/objects/half_heart", gp.tileSize, gp.tileSize);
        image3 = registerEntitySprite("/objects/lost_heart", gp.tileSize, gp.tileSize);
    }
}
