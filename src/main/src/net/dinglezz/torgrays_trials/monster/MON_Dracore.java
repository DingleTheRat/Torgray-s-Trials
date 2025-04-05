package net.dinglezz.torgrays_trials.monster;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.object.OBJ_Coin;
import net.dinglezz.torgrays_trials.object.OBJ_Torgray_Soup;

import java.awt.*;
import java.util.Random;

public class MON_Dracore extends Entity {
    Game game;

    public MON_Dracore(Game game) {
        super(game);
        this.game = game;

        name = "Dracore";
        type = EntityTypes.TYPE_MONSTER;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxHealth = 5;
        health = maxHealth;
        attack = 4;
        defence = 0;
        exp = 2;
        
        getImage();
    }
    public void getImage() {
        up1 = registerEntitySprite("/monster/dracore/dracore_1");
        up2 = registerEntitySprite("/monster/dracore/dracore_2");
        up3 = registerEntitySprite("/monster/dracore/dracore_3");
        
        down1 = registerEntitySprite("/monster/dracore/dracore_1");
        down2 = registerEntitySprite("/monster/dracore/dracore_2");
        down3 = registerEntitySprite("/monster/dracore/dracore_3");
        
        left1 = registerEntitySprite("/monster/dracore/dracore_1");
        left2 = registerEntitySprite("/monster/dracore/dracore_2");
        left3 = registerEntitySprite("/monster/dracore/dracore_3");
        
        right1 = registerEntitySprite("/monster/dracore/dracore_1");
        right2 = registerEntitySprite("/monster/dracore/dracore_2");
        right3 = registerEntitySprite("/monster/dracore/dracore_3");
    }
    public void setAction() {
        actionLockCounter++;
        if (actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1; // Pick a number from 1 to 100

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }
            actionLockCounter = 0;
        }
    }
    public void damageReaction() {
        actionLockCounter = 0;

        switch (game.player.direction) {
            case "up": direction = "down"; break;
            case "down": direction = "up"; break;
            case "left": direction = "right"; break;
            case "right": direction = "left"; break;
        }
    }
    public void checkDrop() {
        int random = new Random().nextInt(2) + 1;

        switch (random) {
            // No zero since there is supposed to be a chance for no drop
            case 1: dropItem(new OBJ_Torgray_Soup(game)); break;
            case 2:
                int amount = new Random().nextInt(100) + 1;
                if (amount <= 15) {
                    dropItem(new OBJ_Coin(game, 3));
                } else if (amount <= 35) {
                    dropItem(new OBJ_Coin(game, 2));
                } else {
                    dropItem(new OBJ_Coin(game, 1));
                }
            break;
        }
    }


    // Particles
    public Color getParticleColor() {return new Color(63, 6, 5);}
    public int getParticleSize() {return 6;} // 6 pixels
    public int getParticleSpeed() {return 1;}
    public int getParticleMaxHealth() {return 20;}
}
