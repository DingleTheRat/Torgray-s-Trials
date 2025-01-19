package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;
import main.States;

import java.util.Random;

public class OBJ_Torgray_Soup extends Entity {
    GamePanel gp;
    int value = 4;

    public OBJ_Torgray_Soup(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Torgray's Soup";
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_CONSUMABLE);
        down1 = registerEntitySprite("/objects/torgray_soup", gp.tileSize, gp.tileSize);
        description = "/nTorgray's wisest soup. /nIt's warm and a bit hearty. /nHealing: +" + value;
    }
    public void use(Entity entity) {
        gp.gameState = States.STATE_DIALOGUE;
        Random random = new Random();
        int i = random.nextInt(6) + 1; // Pick a number from 1 to 100

        if (i == 1) {
            gp.ui.currentDialogue = "Erm a last key is behind the pond. /n+4 health";
        }
        if (i == 2) {
            gp.ui.currentDialogue = "Erm healing in the pond respawns mobs. /n+4 health";
        }
        if (i == 3) {
            gp.ui.currentDialogue = "Erm the higher level, the more you /nheal when leveling up. /n+4 health";
        }
        if (i == 4) {
            gp.ui.currentDialogue = "Erm I think you are left handed. /n+4 health";
        }
        if (i == 5) {
            gp.ui.currentDialogue = "Erm Torgray made two of us. /n+4 health";
        }
        if (i == 5) {
            gp.ui.currentDialogue = "Erm If you are still without a light it's /nvery close to spawn. /n+4 health";
        }

        if (gp.player.health + value > gp.player.maxHealth) {
            gp.player.health = gp.player.maxHealth;
        } else {
            gp.player.health += value;
        }
        gp.playSE(2);
    }
}
