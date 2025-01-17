package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword extends Entity {
    public OBJ_Sword(GamePanel gp) {
        super(gp);

        name = "Sword";
        down1 = registerEntitySprite("/tiles/disabled", gp.tileSize, gp.tileSize);
        attackValue = 1;
    }
}
