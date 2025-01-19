package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;
import main.States;

import java.util.Random;

public class OBJ_Lantern extends Entity {
    GamePanel gp;

    public OBJ_Lantern(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Lantern";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_LIGHT);
        lightRadius = 150;
        down1 = registerEntitySprite("/objects/lantern", gp.tileSize, gp.tileSize);
        description = "/nSHINE BRIGHTTTTTT /nTONIGHTTTTTT /lyr /nLight Radius: " + lightRadius;
    }
}
