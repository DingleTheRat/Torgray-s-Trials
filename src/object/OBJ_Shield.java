package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield extends Entity {
    public OBJ_Shield(GamePanel gp) {
        super(gp);

        name = "Amethyst Shield";
        down1 = registerEntitySprite("/objects/amethyst_shield", gp.tileSize, gp.tileSize);
        defenceValue = 1;
        description = "/n A majestic purple shield /n Defence: +" + defenceValue;
    }
}
