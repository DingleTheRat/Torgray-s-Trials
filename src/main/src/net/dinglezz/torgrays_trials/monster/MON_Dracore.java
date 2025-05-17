package net.dinglezz.torgrays_trials.monster;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.entity.LootTableHandler;
import net.dinglezz.torgrays_trials.main.States;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MON_Dracore extends Entity {
    Game game;
    String lootTable;

    public MON_Dracore(Game game, String lootTable) {
        super(game);
        this.game = game;
        this.lootTable = lootTable;

        name = "Dracore";
        type = EntityTypes.TYPE_MONSTER;
        defaultSpeed = 1;
        speed = defaultSpeed;
        maxHealth = 5;
        health = maxHealth;
        attack = 4;
        defence = 0;
        exp = 2;

        if (game.environmentManager.lighting != null) {
            if (game.environmentManager.lighting.darknessState == States.DarknessStates.GLOOM ||
                    game.environmentManager.lighting.darknessState == States.DarknessStates.LIGHT_GLOOM ||
                    game.environmentManager.lighting.darknessState == States.DarknessStates.DARK_GLOOM) {
                attack += 1;
                maxHealth = maxHealth * 5;
                health = maxHealth;
            }
        }

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

    @Override
    public void update() {
        super.update();

        // Pathfinding
        if (game.pathFinding) {
            int xDistance = Math.abs(worldX - game.player.worldX);
            int yDistance = Math.abs(worldY - game.player.worldY);
            int tileDistance = (xDistance + yDistance) / game.tileSize;

            if (!onPath && tileDistance < 5) {
                int random = new Random().nextInt(2);
                if (random == 1) {
                    onPath = true;
                }
            } else if (tileDistance > 20) {
                onPath = false;
            }
        }
    }

    @Override
    public void setAction() {
        if (onPath) {
            int goalCol = (game.player.worldX + game.player.solidArea.x) / game.tileSize;
            int goalRow = (game.player.worldY + game.player.solidArea.y) / game.tileSize;

            searchPath(goalCol, goalRow, false);
        } else {
            actionLockCounter++;
            if (actionLockCounter == 120) {
                int random = new Random().nextInt(100);

                if (random <= 25) {
                    direction = "up";
                } else if (random <= 50) {
                    direction = "down";
                } else if ( random <= 75) {
                    direction = "left";
                } else {
                    direction = "right";
                }
                actionLockCounter = 0;
            }
        }
    }

    @Override
    public void damageReaction() {
        actionLockCounter = 0;

        // Pathfinding
        if (game.pathFinding) {
            onPath = true;
        } else {
            // If not, then change the direction
            switch (game.player.direction) {
                case "up" -> direction = "down";
                case "down" -> direction = "up";
                case "left" -> direction = "right";
                case "right" -> direction = "left";
            }
        }
    }

    @Override
    public void checkDrop() {
        ArrayList<Entity> loot = LootTableHandler.generateLoot(LootTableHandler.lootTables.get(lootTable));

        if (!loot.isEmpty() && loot.getFirst() != null) {
            dropItem(loot.getFirst());
        }
    }


    // Particles
    @Override public Color getParticleColor() {return new Color(63, 6, 5);}
    @Override public int getParticleSize() {return 6;} // 6 pixels
    @Override public int getParticleSpeed() {return 1;}
    @Override public int getParticleMaxHealth() {return 20;}
}
