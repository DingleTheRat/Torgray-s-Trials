package main;

import entity.Entity;

import java.util.HashMap;

public class CollisionChecker {
    GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkTile(Entity entity) {
        int entityLeftWordX = entity.worldX + entity.solidArea.x;
        int entityRightWordX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWordY = entity.worldY + entity.solidArea.y;
        int entityBottomWordY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWordX / gamePanel.tileSize;
        int entityRightCol = entityRightWordX / gamePanel.tileSize;
        int entityTopRow = entityTopWordY / gamePanel.tileSize;
        int entityBottomRow = entityBottomWordY / gamePanel.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWordY - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case  "up left":
                entityTopRow = (entityTopWordY - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityLeftCol = (entityLeftWordX - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "up right":
                entityTopRow = (entityTopWordY - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityRightCol = (entityRightWordX + entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWordY + entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down left":
                entityBottomRow = (entityBottomWordY + entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityLeftCol = (entityLeftWordX - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case  "down right":
                entityBottomRow = (entityBottomWordY + entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                entityRightCol = (entityRightWordX + entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWordX - entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWordX + entity.speed) / gamePanel.tileSize;
                tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
                if (gamePanel.tileManager.tile.get(tileNum1).collision || gamePanel.tileManager.tile.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {
        int index = 999;

        for (int i = 0; i < gamePanel.obj.size(); i++) {
            if (gamePanel.obj.get(i) != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                gamePanel.obj.get(i).solidArea.x = gamePanel.obj.get(i).worldX + gamePanel.obj.get(i).solidArea.x;
                gamePanel.obj.get(i).solidArea.y = gamePanel.obj.get(i).worldY + gamePanel.obj.get(i).solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed; break;
                    case "down":
                        entity.solidArea.y += entity.speed; break;
                    case "left":
                        entity.solidArea.x -= entity.speed; break;
                    case "right":
                        entity.solidArea.x += entity.speed; break;
                }
                if (entity.solidArea.intersects(gamePanel.obj.get(i).solidArea)) {
                    if (gamePanel.obj.get(i).collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gamePanel.obj.get(i).solidArea.x = gamePanel.obj.get(i).solidAreaDefaultX;
                gamePanel.obj.get(i).solidArea.y = gamePanel.obj.get(i).solidAreaDefaultY;
            }
        }

        return index;
    }
    public int checkEntity(Entity entity, HashMap<Integer, Entity> target) {
        int index = 999;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                switch (entity.direction) {
                    case "up": entity.solidArea.y -= entity.speed; break;
                    case "down": entity.solidArea.y += entity.speed; break;
                    case "left": entity.solidArea.x -= entity.speed; break;
                    case "right": entity.solidArea.x += entity.speed; break;
                }
                if (entity.solidArea.intersects(target.get(i).solidArea)) {
                    if (target.get(i) != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;
            }
        }

        return index;
    }
    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
        gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;

        switch (entity.direction) {
            case "up": entity.solidArea.y -= entity.speed; break;
            case "down": entity.solidArea.y += entity.speed; break;
            case "left": entity.solidArea.x -= entity.speed; break;
            case "right": entity.solidArea.x += entity.speed; break;
        }
        if (entity.solidArea.intersects(gamePanel.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
        gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
        return contactPlayer;
    }
}
