package object;

import entity.Entity;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Key extends Entity {
    public OBJ_Key(GamePanel gp) {
        super(gp);

        name = "Key";
        type = EntityTypes.TYPE_OBJECT;
        down1 = registerEntitySprite("/objects/key", gp.tileSize, gp.tileSize);
        description = "/n Probably opens a gate";
    }
}
