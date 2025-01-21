package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;

public class OBJ_Gate extends Entity {
    GamePanel gp;

    public OBJ_Gate(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Gate";
        down1 = registerEntitySprite("/objects/gate", gp.tileSize, gp.tileSize);
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
        for (int i = 0; i < gp.player.inventory.size(); i++) {
            if (gp.player.inventory.get(i).name.equals("Key")) {
                gp.obj.put(j, null);
                gp.player.inventory.remove(i);
                gp.ui.addMessage("-1 Key");
                gp.playSE(3);
                break;
            }
        }
    }
}
