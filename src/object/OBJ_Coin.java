package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Coin extends Entity {
    GamePanel gp;

    public OBJ_Coin(GamePanel gamePanel) {
        super(gamePanel);
        this.gp = gamePanel;

        name = "Coin";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_PICKUPONLY);
        down1 = registerEntitySprite("/objects/coin", gamePanel.tileSize, gamePanel.tileSize);
    }

    public boolean use(Entity entity) {
        gp.playSound(1);
        gp.ui.addMessage("+1 Coin");
        gp.player.coins++;
        return true;
    }
}
