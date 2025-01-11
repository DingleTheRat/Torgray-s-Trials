package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity {
    public OBJ_Heart(GamePanel gp) {
        super(gp);
        name = "Heart";

        image = registerEntitySprite("/objects/heart");
        image2 = registerEntitySprite("/objects/half_heart");
        image3 = registerEntitySprite("/objects/lost_heart");
    }
}
