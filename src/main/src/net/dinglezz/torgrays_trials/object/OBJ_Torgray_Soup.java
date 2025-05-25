package net.dinglezz.torgrays_trials.object;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTags;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.Sound;
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
        down1 = registerEntitySprite("/object/torgray_soup");
        description = "Torgray's wisest soup. \nIt's warm and a bit hearty. \nHealing: +" + value;
        maxStack = 12;
        price = 2;
    }
    public boolean use(Entity entity) {
        game.gameState = States.GameStates.DIALOGUE;
        int random = new Random().nextInt(9) + 1;

        game.ui.currentDialogue = switch (random) {
            case 1 -> "Erm the last two keys have to be bought. \n+4 health";
            case 2 -> "Erm by pressing F3 you can enter debug \nmode. \n+4 health";
            case 3 -> "Erm when it's gloom, Dracores respawn, \nhave their health quadrupled, and do half a \nheart more damage. \n+4 health";
            case 4 -> "Erm it appears you are left handed. \nBut hey, that's just a theory! A GAME THEORY! \n+4 health";
            case 5 -> "Erm after passing all 4 gates you get \nsome cool goodies. \n+4 health";
            case 6 -> "Erm in dark gloom or light gloom, your light \nrange increases or decreases by 50px. \n+4 health";
            case 7 -> "Erm a heart is actually 2 health points, \nhalf a heart is 1. \n+4 health";
            case 8 -> "Erm when you go down from your spawn \npoint, in the area where the Dracores are, \nthere is a path to a hut. \n+4 health";
            case 9 -> "Erm leveling up increases your defence and \nattack values, but so do swords and shields. \n+4 health";
            default -> "Erm something went wrong. \n+4 health";
        };

        if (game.player.health + value > game.player.maxHealth) {
            game.player.health = game.player.maxHealth;
        } else {
            game.player.health += value;
        }
        game.player.generateParticles(this, game.player);
        Sound.playSFX("Power Up");
        return true;
    }

    // Particles
    public Color getParticleColor() {return new Color(209, 25, 25);}
    public int getParticleSize() {return 6;} // 6 pixels
    public int getParticleSpeed() {return 1;}
    public int getParticleMaxHealth() {return 20;}
}
