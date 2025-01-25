package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Sword_Amethyst extends Entity {
    public OBJ_Sword_Amethyst(GamePanel gamePanel) {
        super(gamePanel);

        name = "Amethyst Sword";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_AMETHIST);
        tags.add(EntityTags.TAG_SWORD);
        down1 = registerEntitySprite("/objects/amethyst_sword", gamePanel.tileSize, gamePanel.tileSize);
        attackValue = 2;
        attackArea.width = 36;
        attackArea.height = 36;
        description = "/n A majestic purple sword /n Attack: +" + attackValue;
    }
}
