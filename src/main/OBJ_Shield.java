package main;

import entity.Entity;

public class OBJ_Shield extends Entity {
    public OBJ_Shield(GamePanel gp) {
        super(gp);

        name = "Normal Shield";
        down1 = registerEntitySprite("/tiles/disabled", gp.tileSize, gp.tileSize);
        defenceValue = 1;
    }
}
