package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Gate extends Entity {
    GamePanel gamePanel;

    public OBJ_Gate(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        name = "Gate";
        down1 = registerEntitySprite("/objects/gate", gamePanel.tileSize, gamePanel.tileSize);
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_INTERACTABLE);
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
    public void use(Entity entity, int j) {
        for (int i = 0; i < gamePanel.player.inventory.size(); i++) {
            if (gamePanel.player.inventory.get(i).name.equals("Key")) {
                gamePanel.obj.put(j, null);
                gamePanel.player.inventory.remove(i);
                gamePanel.ui.addMessage("-1 Key");
                gamePanel.playSound(3);
                break;
            }
        }
    }
}
