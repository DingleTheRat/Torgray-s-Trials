package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Gate extends Entity {
    public OBJ_Gate(GamePanel gp) {
        super(gp);

        name = "Gate";
        down1 = registerEntitySprite("/objects/gate", gp.tileSize, gp.tileSize);
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
