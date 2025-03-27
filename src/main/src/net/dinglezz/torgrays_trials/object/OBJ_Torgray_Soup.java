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
        down1 = registerEntitySprite("/drawable/objects/torgray_soup");
        description = "/nTorgray's wisest soup. /nIt's warm and a bit hearty. /nHealing: +" + value;
        stackable = true;
        price = 1;
    }
    public boolean use(Entity entity) {
        game.gameState = States.STATE_DIALOGUE;
        int random = new Random().nextInt(6) + 1;

        game.ui.currentDialogue = switch (random) {
            case 1 -> "Erm a last key is behind the pond. /n+4 health";
            case 2 -> "Erm healing in the pond respawns /nmobs. /n+4 health";
            case 3 -> "Erm the higher level, the more you /nheal when leveling up. /n+4 health";
            case 4 -> "Erm I think you are left handed. /n+4 health";
            case 5 -> "Erm after passing all 3 gates you get /na reward. /n+4 health";
            case 6 -> "Erm the pond is a good place to /nheal. /n+4 health";
            default -> "Erm something went wrong. /n+4 health";
        };

        if (game.player.health + value > game.player.maxHealth) {
            game.player.health = game.player.maxHealth;
        } else {
            game.player.health += value;
        }
        game.player.generateParticles(this, game.player);
        game.playSound("Power Up");
        return true;
    }

    // Particles
    public Color getParticleColor() {return new Color(209, 25, 25);}
    public int getParticleSize() {return 6;} // 6 pixels
    public int getParticleSpeed() {return 1;}
    public int getParticleMaxHealth() {return 20;}
}
