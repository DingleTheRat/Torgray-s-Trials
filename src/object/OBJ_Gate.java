package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Gate extends Entity {
    public OBJ_Gate(GamePanel gp) {
        super(gp);

        name = "Gate";
        down1 = registerEntitySprite("/objects/gate", gp.tileSize, gp.tileSize);
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_INTERACTABLE);
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
