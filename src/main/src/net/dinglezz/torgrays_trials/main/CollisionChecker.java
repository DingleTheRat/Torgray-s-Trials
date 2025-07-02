package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.mob.Mob;
import net.dinglezz.torgrays_trials.tile.TileManager;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.util.ArrayList;

public class CollisionChecker {
    public static void checkTile(Mob mob) {
        if (TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, 0, 0)) == null) return;

        int mobLeftWorldX = mob.worldX + mob.solidArea.x;
        int mobRightWorldX = mob.worldX + mob.solidArea.x + mob.solidArea.width;
        int mobTopWorldY = mob.worldY + mob.solidArea.y;
        int mobBottomWorldY = mob.worldY + mob.solidArea.y + mob.solidArea.height;

        int mobLeftCol = mobLeftWorldX / Main.game.tileSize;
        int mobRightCol = mobRightWorldX / Main.game.tileSize;
        int mobTopRow = mobTopWorldY / Main.game.tileSize;
        int mobBottomRow = mobBottomWorldY / Main.game.tileSize;

        int tileNumber1, tileNumber2;

        switch (mob.direction) {
            case "up":
                mobTopRow = (mobTopWorldY - mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobTopRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                break;
            case  "up left":
                mobTopRow = (mobTopWorldY - mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobTopRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                mobLeftCol = (mobLeftWorldX - mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                break;
            case "up right":
                mobTopRow = (mobTopWorldY - mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobTopRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                mobRightCol = (mobRightWorldX + mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                break;
            case "down":
                mobBottomRow = (mobBottomWorldY + mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobBottomRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                break;
            case "down left":
                mobBottomRow = (mobBottomWorldY + mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobBottomRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                mobLeftCol = (mobLeftWorldX - mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                break;
            case  "down right":
                mobBottomRow = (mobBottomWorldY + mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobBottomRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                mobRightCol = (mobRightWorldX + mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobBottomRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                break;
            case "left":
                mobLeftCol = (mobLeftWorldX - mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                break;
            case "right":
                mobRightCol = (mobRightWorldX + mob.speed) / Main.game.tileSize;
                tileNumber1 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobLeftCol, mobTopRow));
                tileNumber2 = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(Main.game.currentMap, mobRightCol, mobBottomRow));
                if (TileManager.tile.get(tileNumber1).collision || TileManager.tile.get(tileNumber2).collision) {
                    mob.colliding = true;
                }
                break;
        }
    }

    /**
     * Checks if a given entity collides with any entity in a list of target entities.
     * If a collision is detected, the colliding target entity is returned, if not, then null is returned.
     *
     * @param <T> the type of the source entity being checked, which extends Entity
     * @param <V> the type of the target entities in the list, which extends Entity
     * @param entity the source entity whose collision is being checked
     * @param targets the list of target entities to check for collisions
     * @return the first target entity that the source entity collides with, or null if no collision is detected
     */
    public static <T extends Entity, V extends Entity> V checkEntity(T entity, ArrayList<V> targets) {
        // Define the return variable, if the target is not found, it will return null
        V target = null;

        // If the targets list is null or empty, return
        if (targets == null || targets.isEmpty()) return target;

        for (V checkedEntity : targets) {
            // Make sure the checked entity is not null
            if (checkedEntity == null) continue;

            // Set the solid area for both the entity and the checked entity
            entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
            entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;
            checkedEntity.solidArea.x = checkedEntity.worldX + checkedEntity.solidAreaDefaultX;
            checkedEntity.solidArea.y = checkedEntity.worldY + checkedEntity.solidAreaDefaultY;

            // Adjust the solid area based on the entity's direction (If it's a Mob)
            if (entity instanceof Mob mob) {
                switch (mob.direction) {
                    case "up" -> mob.solidArea.y -= mob.speed;
                    case "down" -> mob.solidArea.y += mob.speed;
                    case "left" -> mob.solidArea.x -= mob.speed;
                    case "right" -> mob.solidArea.x += mob.speed;
                }
            }
            // Check if the solid areas intersect
            if (entity.solidArea.intersects(checkedEntity.solidArea)) {
                // If the checked entity exists, set the target as the checked entity and mark the entity as colliding
                if (checkedEntity != entity) {
                    entity.colliding = true;
                    target = checkedEntity;
                }
            }

            // Reset the solid areas to their default positions
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            checkedEntity.solidArea.x = checkedEntity.solidAreaDefaultX;
            checkedEntity.solidArea.y = checkedEntity.solidAreaDefaultY;
        }

        return target;
    }
    public static boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        Main.game.player.solidArea.x = Main.game.player.worldX + Main.game.player.solidArea.x;
        Main.game.player.solidArea.y = Main.game.player.worldY + Main.game.player.solidArea.y;

        if (entity instanceof Mob mob) {
            switch (mob.direction) {
                case "up" -> mob.solidArea.y -= mob.speed;
                case "down" -> mob.solidArea.y += mob.speed;
                case "left" -> mob.solidArea.x -= mob.speed;
                case "right" -> mob.solidArea.x += mob.speed;
            }
        }
        if (entity.solidArea.intersects(Main.game.player.solidArea)) {
            entity.colliding = true;
            contactPlayer = true;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        Main.game.player.solidArea.x = Main.game.player.solidAreaDefaultX;
        Main.game.player.solidArea.y = Main.game.player.solidAreaDefaultY;
        return contactPlayer;
    }

    public static <T extends Entity> T getDetected(Mob user, ArrayList<T> targets, String targetName) {
        T detected = null;

        // Check surrounding objects
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch (user.direction) {
            case "up" -> nextWorldY = user.getTopY() - 10;
            case "down" -> nextWorldY = user.getBottomY() + 10;
            case "left" -> nextWorldX = user.getLeftX() - 10;
            case "right" -> nextWorldY = user.getRightX() + 10;
        }
        int col = nextWorldX / Main.game.tileSize;
        int row = nextWorldY / Main.game.tileSize;

        for (T target : targets) {
            if (target != null) {
                if (target.getCol() == col && target.getRow() == row && target.name.equals(targetName)) {
                    detected = target;
                    break;
                }
            }
        }
        return detected;
    }
}
