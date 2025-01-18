package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword extends Entity {
    public OBJ_Sword(GamePanel gp) {
        super(gp);

        name = "Amethyst Sword";
        down1 = registerEntitySprite("/objects/amethyst_sword", gp.tileSize, gp.tileSize);
        attackValue = 1;
        description = "/n A majestic purple sword /n Attack: +" + attackValue;
    }
}
