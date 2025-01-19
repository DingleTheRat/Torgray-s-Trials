package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Sword_Iron extends Entity {
    public OBJ_Sword_Iron(GamePanel gp) {
        super(gp);

        name = "Iron Sword";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_IRON);
        tags.add(EntityTags.TAG_SWORD);
        down1 = registerEntitySprite("/objects/iron_sword", gp.tileSize, gp.tileSize);
        attackValue = 1;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "/n A shiny iron shield /n Attack: +" + attackValue;
    }
}
