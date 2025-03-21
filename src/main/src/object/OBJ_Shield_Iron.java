package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Shield_Iron extends Entity {
    public OBJ_Shield_Iron(GamePanel gamePanel) {
        super(gamePanel);

        name = "Iron Shield";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_IRON);
        tags.add(EntityTags.TAG_SHIELD);
        down1 = registerEntitySprite("/drawable/objects/iron_shield", gamePanel.tileSize, gamePanel.tileSize);
        defenceValue = 1;
        description = "/n A shiny iron shield /n Defence: +" + defenceValue;
    }
}
