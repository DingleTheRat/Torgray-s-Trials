package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Coin extends Entity {
    GamePanel gp;

    public OBJ_Coin(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Coin";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_PICKUPONLY);
        down1 = registerEntitySprite("/objects/coin", gp.tileSize, gp.tileSize);
    }

    public void use(Entity entity) {
        gp.playSE(1);
        gp.ui.addMessage("+1 Coin");
        gp.player.coins++;
    }
}
