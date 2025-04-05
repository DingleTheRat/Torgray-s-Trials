package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;

import java.util.HashMap;

public class CollisionChecker {
    Game game;

    public CollisionChecker(Game game) {
        this.game = game;
    }

    public void checkTile(Entity entity) {
        int entityLeftWordX = entity.worldX + entity.solidArea.x;
        int entityRightWordX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWordY = entity.worldY + entity.solidArea.y;
        int entityBottomWordY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWordX / game.tileSize;
        int entityRightCol = entityRightWordX / game.tileSize;
        int entityTopRow = entityTopWordY / game.tileSize;
        int entityBottomRow = entityBottomWordY / game.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWordY - entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityTopRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case  "up left":
                entityTopRow = (entityTopWordY - entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityTopRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityLeftCol = (entityLeftWordX - entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityBottomRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "up right":
                entityTopRow = (entityTopWordY - entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityTopRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityRightCol = (entityRightWordX + entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityBottomRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWordY + entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityBottomRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityBottomRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down left":
                entityBottomRow = (entityBottomWordY + entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityBottomRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityBottomRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityLeftCol = (entityLeftWordX - entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityBottomRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case  "down right":
                entityBottomRow = (entityBottomWordY + entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityBottomRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityBottomRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityRightCol = (entityRightWordX + entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityBottomRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWordX - entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityBottomRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWordX + entity.speed) / game.tileSize;
                tileNum1 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityLeftCol][entityTopRow];
                tileNum2 = game.tileManager.mapTileNum[game.tileManager.mapNumbers.get(game.currentMap)][entityRightCol][entityBottomRow];
                if (game.tileManager.tile.get(tileNum1).collision || game.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

public int checkObject(Entity entity, boolean player) {
    int index = 999;

    for (int i = 0; i < game.object.get(game.currentMap).size(); i++) {
        if (game.object.get(game.currentMap).get(i) != null) {
            entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
            entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;
            game.object.get(game.currentMap).get(i).solidArea.x = game.object.get(game.currentMap).get(i).worldX + game.object.get(game.currentMap).get(i).solidAreaDefaultX;
            game.object.get(game.currentMap).get(i).solidArea.y = game.object.get(game.currentMap).get(i).worldY + game.object.get(game.currentMap).get(i).solidAreaDefaultY;

            switch (entity.direction) {
                case "up": entity.solidArea.y -= entity.speed; break;
                case "down": entity.solidArea.y += entity.speed; break;
                case "left": entity.solidArea.x -= entity.speed; break;
                case "right": entity.solidArea.x += entity.speed; break;
            }
            if (entity.solidArea.intersects(game.object.get(game.currentMap).get(i).solidArea)) {
                if (game.object.get(game.currentMap).get(i).collision) {
                    entity.collisionOn = true;
                }
                if (player) {
                    index = i;
                }
            }

            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            game.object.get(game.currentMap).get(i).solidArea.x = game.object.get(game.currentMap).get(i).solidAreaDefaultX;
            game.object.get(game.currentMap).get(i).solidArea.y = game.object.get(game.currentMap).get(i).solidAreaDefaultY;
        }
    }

    return index;
}

public int checkEntity(Entity entity, HashMap<String, HashMap<Integer, Entity>> target) {
    int index = 999;

    for (int i = 0; i < target.get(game.currentMap).size(); i++) {
        if (target.get(game.currentMap).get(i) != null) {
            entity.solidArea.x = entity.worldX + entity.solidAreaDefaultX;
            entity.solidArea.y = entity.worldY + entity.solidAreaDefaultY;
            target.get(game.currentMap).get(i).solidArea.x = target.get(game.currentMap).get(i).worldX + target.get(game.currentMap).get(i).solidAreaDefaultX;
            target.get(game.currentMap).get(i).solidArea.y = target.get(game.currentMap).get(i).worldY + target.get(game.currentMap).get(i).solidAreaDefaultY;

            switch (entity.direction) {
                case "up": entity.solidArea.y -= entity.speed; break;
                case "down": entity.solidArea.y += entity.speed; break;
                case "left": entity.solidArea.x -= entity.speed; break;
                case "right": entity.solidArea.x += entity.speed; break;
            }
            if (entity.solidArea.intersects(target.get(game.currentMap).get(i).solidArea)) {
                if (target.get(game.currentMap).get(i) != entity) {
                    entity.collisionOn = true;
                    index = i;
                }
            }
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            target.get(game.currentMap).get(i).solidArea.x = target.get(game.currentMap).get(i).solidAreaDefaultX;
            target.get(game.currentMap).get(i).solidArea.y = target.get(game.currentMap).get(i).solidAreaDefaultY;
        }
    }

    return index;
}
    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        game.player.solidArea.x = game.player.worldX + game.player.solidArea.x;
        game.player.solidArea.y = game.player.worldY + game.player.solidArea.y;

        switch (entity.direction) {
            case "up": entity.solidArea.y -= entity.speed; break;
            case "down": entity.solidArea.y += entity.speed; break;
            case "left": entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed; break;
        }
        if (entity.solidArea.intersects(game.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        game.player.solidArea.x = game.player.solidAreaDefaultX;
        game.player.solidArea.y = game.player.solidAreaDefaultY;
        return contactPlayer;
    }
}
