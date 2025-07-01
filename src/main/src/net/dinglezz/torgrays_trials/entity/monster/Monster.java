package net.dinglezz.torgrays_trials.entity.monster;

import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.io.Serializable;

public abstract class Monster extends Mob implements Serializable {
    public int attack;

    public Monster(String name, TilePoint tilePoint) {
        super(name, tilePoint);

        // Change the hit sound
        hitSound = "Hit Monster";
        attack = 1;
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;

        // Pathfinding
        if (Main.game.pathFinding) {
            onPath = true;
        } else {
            // If not, then change the direction
            switch (Main.game.player.direction) {
                case "up" -> direction = "down";
                case "down" -> direction = "up";
                case "left" -> direction = "right";
                case "right" -> direction = "left";
            }
        }
    }

    @Override
    public void checkCollision() {
        super.checkCollision();

        // If it's touching the player, then attack
        if (contactPlayer) {
            Main.game.player.damage(attack);
        }
    }
}
