package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.tile.MapHandler;
import net.dinglezz.torgrays_trials.tile.TileManager;

import java.util.HashMap;

public class CollisionChecker {
    public static void checkTile(Entity entity) {
        int entityLeftWordX = entity.worldX + entity.solidArea.x;
        int entityRightWordX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWordY = entity.worldY + entity.solidArea.y;
        int entityBottomWordY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWordX / Main.game.tileSize;
        int entityRightCol = entityRightWordX / Main.game.tileSize;
        int entityTopRow = entityTopWordY / Main.game.tileSize;
        int entityBottomRow = entityBottomWordY / Main.game.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWordY - entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityTopRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case  "up left":
                entityTopRow = (entityTopWordY - entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityTopRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityLeftCol = (entityLeftWordX - entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityBottomRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "up right":
                entityTopRow = (entityTopWordY - entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityTopRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityRightCol = (entityRightWordX + entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityBottomRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWordY + entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityBottomRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityBottomRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down left":
                entityBottomRow = (entityBottomWordY + entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityBottomRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityBottomRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityLeftCol = (entityLeftWordX - entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityBottomRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case  "down right":
                entityBottomRow = (entityBottomWordY + entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityBottomRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityBottomRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityRightCol = (entityRightWordX + entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityBottomRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWordX - entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityBottomRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWordX + entity.speed) / Main.game.tileSize;
                tileNum1 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = TileManager.mapTileNum[MapHandler.mapNumbers.get(Main.game.currentMap)][entityRightCol][entityBottomRow];
                if (TileManager.tile.get(tileNum1).collision || TileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

public static int checkObject(Entity entity, boolean player) {
    int index = 999;

    for (int i = 0; i < Main.game.object.get(Main.game.currentMap).size(); i++) {
        if (Main.game.object.get(Main.game.currentMap).get(i) != null) {
            entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
            entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;
            Main.game.object.get(Main.game.currentMap).get(i).solidArea.x = Main.game.object.get(Main.game.currentMap).get(i).worldX + Main.game.object.get(Main.game.currentMap).get(i).solidAreaDefaultX;
            Main.game.object.get(Main.game.currentMap).get(i).solidArea.y = Main.game.object.get(Main.game.currentMap).get(i).worldY + Main.game.object.get(Main.game.currentMap).get(i).solidAreaDefaultY;

            switch (entity.direction) {
                case "up": entity.solidArea.y -= entity.speed; break;
                case "down": entity.solidArea.y += entity.speed; break;
                case "left": entity.solidArea.x -= entity.speed; break;
                case "right": entity.solidArea.x += entity.speed; break;
            }
            if (entity.solidArea.intersects(Main.game.object.get(Main.game.currentMap).get(i).solidArea)) {
                if (Main.game.object.get(Main.game.currentMap).get(i).collision) {
                    entity.collisionOn = true;
                }
                if (player) {
                    index = i;
                }
            }

            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            Main.game.object.get(Main.game.currentMap).get(i).solidArea.x = Main.game.object.get(Main.game.currentMap).get(i).solidAreaDefaultX;
            Main.game.object.get(Main.game.currentMap).get(i).solidArea.y = Main.game.object.get(Main.game.currentMap).get(i).solidAreaDefaultY;
        }
    }

    return index;
}

public static int checkEntity(Entity entity, HashMap<String, HashMap<Integer, Entity>> target) {
    int index = 999;

    for (int i = 0; i < target.get(Main.game.currentMap).size(); i++) {
        if (target.get(Main.game.currentMap).get(i) != null) {
            entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
            entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;
            target.get(Main.game.currentMap).get(i).solidArea.x = target.get(Main.game.currentMap).get(i).worldX + target.get(Main.game.currentMap).get(i).solidAreaDefaultX;
            target.get(Main.game.currentMap).get(i).solidArea.y = target.get(Main.game.currentMap).get(i).worldY + target.get(Main.game.currentMap).get(i).solidAreaDefaultY;

            switch (entity.direction) {
                case "up": entity.solidArea.y -= entity.speed; break;
                case "down": entity.solidArea.y += entity.speed; break;
                case "left": entity.solidArea.x -= entity.speed; break;
                case "right": entity.solidArea.x += entity.speed; break;
            }
            if (entity.solidArea.intersects(target.get(Main.game.currentMap).get(i).solidArea)) {
                if (target.get(Main.game.currentMap).get(i) != entity) {
                    entity.collisionOn = true;
                    index = i;
                }
            }
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            target.get(Main.game.currentMap).get(i).solidArea.x = target.get(Main.game.currentMap).get(i).solidAreaDefaultX;
            target.get(Main.game.currentMap).get(i).solidArea.y = target.get(Main.game.currentMap).get(i).solidAreaDefaultY;
        }
    }

    return index;
}
    public static boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        Main.game.player.solidArea.x = Main.game.player.worldX + Main.game.player.solidArea.x;
        Main.game.player.solidArea.y = Main.game.player.worldY + Main.game.player.solidArea.y;

        switch (entity.direction) {
            case "up": entity.solidArea.y -= entity.speed; break;
            case "down": entity.solidArea.y += entity.speed; break;
            case "left": entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed; break;
        }
        if (entity.solidArea.intersects(Main.game.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        Main.game.player.solidArea.x = Main.game.player.solidAreaDefaultX;
        Main.game.player.solidArea.y = Main.game.player.solidAreaDefaultY;
        return contactPlayer;
    }
}
