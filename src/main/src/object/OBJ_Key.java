package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Key extends Entity {
    GamePanel gamePanel;

    public OBJ_Key(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        name = "Key";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_CONSUMABLE);
        down1 = registerEntitySprite("/drawable/objects/key", gamePanel.tileSize, gamePanel.tileSize);
        description = "/n Probably opens a gate";
        stackable = true;
    }
    public boolean use(Entity entity) {

        int objIndex = getDetected(entity, gamePanel.obj, "Gate");
        if (objIndex != 999) {
            gamePanel.ui.addMessage("-1 Key");
            gamePanel.playSound(3);
            gamePanel.obj.put(objIndex, null);
            return true;
        } else {
            return false;
        }
    }
}
