package main;

import entity.Entity;

public class OBJ_Sword_Normal extends Entity {
    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);

        name = "Normal Sword";
        down1 = registerEntitySprite("/tiles/disabled", gp.tileSize, gp.tileSize);
        attackValue = 1;
    }
}
