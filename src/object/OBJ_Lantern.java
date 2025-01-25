package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Lantern extends Entity {
    GamePanel gamePanel;

    public OBJ_Lantern(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        name = "Lantern";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_LIGHT);
        lightRadius = 150;
        down1 = registerEntitySprite("/objects/lantern", gamePanel.tileSize, gamePanel.tileSize);
        description = "/nSHINE BRIGHTTTTTT /nTONIGHTTTTTT /lyr /nLight Radius: " + lightRadius;
    }
}
