package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {
    public OBJ_Key(GamePanel gp) {
        super(gp);

        name = "Key";
        down1 = registerEntitySprite("/objects/key", gp.tileSize, gp.tileSize);
        description = "/n Probably opens a gate";
    }
}
