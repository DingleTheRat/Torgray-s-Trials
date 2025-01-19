package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Shield_Iron extends Entity {
    public OBJ_Shield_Iron(GamePanel gp) {
        super(gp);

        name = "Iron Shield";
        type = EntityTypes.TYPE_SHIELD;
        tags.add(EntityTags.TAG_IRON);
        down1 = registerEntitySprite("/objects/iron_shield", gp.tileSize, gp.tileSize);
        defenceValue = 1;
        description = "/n A shiny iron shield /n Defence: +" + defenceValue;
    }
}
