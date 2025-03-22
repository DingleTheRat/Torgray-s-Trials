package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;

import java.awt.*;
import java.util.Random;

public class OBJ_Torgray_Soup extends Entity {
    Game game;

    public OBJ_Torgray_Soup(Game game) {
        super(game);
        this.game = game;

        name = "Torgray's Soup";
        value = 4;
        type = EntityTypes.TYPE_OBJECT;
        tags.add(EntityTags.TAG_CONSUMABLE);
        down1 = registerEntitySprite("/drawable/objects/torgray_soup", game.tileSize, game.tileSize);
        description = "/nTorgray's wisest soup. /nIt's warm and a bit hearty. /nHealing: +" + value;
        stackable = true;
    }
    public boolean use(Entity entity) {
        game.gameState = States.STATE_DIALOGUE;
        Random random = new Random();
        int i = random.nextInt(5) + 1;

        if (i == 1) {
            game.ui.currentDialogue = "Erm a last key is behind the pond. /n+4 health";
        }
        if (i == 2) {
            game.ui.currentDialogue = "Erm healing in the pond respawns /nmobs. /n+4 health";
        }
        if (i == 3) {
            game.ui.currentDialogue = "Erm the higher level, the more you /nheal when leveling up. /n+4 health";
        }
        if (i == 4) {
            game.ui.currentDialogue = "Erm I think you are left handed. /n+4 health";
        }
        if (i == 5) {
            game.ui.currentDialogue = "Erm after passing all 3 gates you get /na reward. /n+4 health";
        }

        if (game.player.health + value > game.player.maxHealth) {
            game.player.health = game.player.maxHealth;
        } else {
            game.player.health += value;
        }
        game.player.generateParticles(this, game.player, value);
        game.playSound(2);
        return true;
    }

    // Particles
    public Color getParticleColor() {return new Color(209, 25, 25);}
    public int getParticleSize() {return 6;} // 6 pixels
    public int getParticleSpeed() {return 1;}
    public int getParticleMaxHealth() {return 20;}
}
