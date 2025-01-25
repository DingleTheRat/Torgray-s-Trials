package object;

import entity.Entity;
import entity.EntityTags;
import entity.EntityTypes;
import main.GamePanel;
import main.States;

import java.awt.*;
import java.util.Random;

public class OBJ_Torgray_Soup extends Entity {
    GamePanel gamePanel;

    public OBJ_Torgray_Soup(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        name = "Torgray's Soup";
        value = 4;
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_CONSUMABLE);
        down1 = registerEntitySprite("/objects/torgray_soup", gamePanel.tileSize, gamePanel.tileSize);
        description = "/nTorgray's wisest soup. /nIt's warm and a bit hearty. /nHealing: +" + value;
    }
    public void use(Entity entity) {
        gamePanel.gameState = States.STATE_DIALOGUE;
        Random random = new Random();
        int i = random.nextInt(5) + 1;

        if (i == 1) {
            gamePanel.ui.currentDialogue = "Erm a last key is behind the pond. /n+4 health";
        }
        if (i == 2) {
            gamePanel.ui.currentDialogue = "Erm healing in the pond respawns mobs. /n+4 health";
        }
        if (i == 3) {
            gamePanel.ui.currentDialogue = "Erm the higher level, the more you /nheal when leveling up. /n+4 health";
        }
        if (i == 4) {
            gamePanel.ui.currentDialogue = "Erm I think you are left handed. /n+4 health";
        }
        if (i == 5) {
            gamePanel.ui.currentDialogue = "Erm after passing all 3 gates you get a /nreward. /n+4 health";
        }

        if (gamePanel.player.health + value > gamePanel.player.maxHealth) {
            gamePanel.player.health = gamePanel.player.maxHealth;
        } else {
            gamePanel.player.health += value;
        }
        gamePanel.player.generateParticles(this, gamePanel.player, value);
        gamePanel.playSound(2);
    }

    // Particles
    public Color getParticleColor() {return new Color(209, 25, 25);}
    public int getParticleSize() {return 6;} // 6 pixels
    public int getParticleSpeed() {return 1;}
    public int getParticleMaxHealth() {return 20;}
}
