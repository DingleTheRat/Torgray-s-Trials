package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Shield_Amethyst extends Entity {
    public OBJ_Shield_Amethyst(GamePanel gp) {
        super(gp);

        name = "Amethyst Shield";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_AMETHIST);
        tags.add(EntityTags.TAG_SHIELD);
        down1 = registerEntitySprite("/objects/amethyst_shield", gp.tileSize, gp.tileSize);
        defenceValue = 2;
        description = "/n A majestic purple shield /n Defence: +" + defenceValue;
    }
}
