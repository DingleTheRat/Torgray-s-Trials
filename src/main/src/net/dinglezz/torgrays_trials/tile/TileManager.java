package net.dinglezz.torgrays_trials.tile;

import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class TileManager {
    Game game;
    public HashMap<Integer, Tile> tile = new HashMap<>();
    public int[][][] mapTileNum;

    // Map Screen
    public HashMap<String, BufferedImage> worldMap = new HashMap<>();

    public TileManager(Game game) {
        this.game = game;
        mapTileNum = new int[game.maxMaps][game.maxWorldCol][game.maxWorldRow];
        getTileImage();
    }

    public void getTileImage() {
        // Grass
        registerTile(10, "grass/grass_1", false);
        registerTile(11, "grass/grass_2", false);

        // Water
        registerTile(12, "water/water", true);
        registerTile(13, "water/white_line_water", true);
        registerTile(14, "water/water_corner_1", true);
        registerTile(15, "water/water_edge_3", true);
        registerTile(16, "water/water_corner_3", true);
        registerTile(17, "water/water_edge_4", true);
        registerTile(18, "water/water_edge_2", true);
        registerTile(19, "water/water_corner_2", true);
        registerTile(20, "water/water_edge_1", true);
        registerTile(21, "water/water_corner_4", true);
        registerTile(22, "water/water_outer_corner_1", true);
        registerTile(23, "water/water_outer_corner_2", true);
        registerTile(24, "water/water_outer_corner_3", true);
        registerTile(25, "water/water_outer_corner_4", true);

        // Path
        registerTile(26, "path/path", false);
        registerTile(27, "path/path_corner_1", false);
        registerTile(28, "path/path_edge_1", false);
        registerTile(29, "path/path_corner_2", false);
        registerTile(30, "path/path_edge_4", false);
        registerTile(31, "path/path_edge_2", false);
        registerTile(32, "path/path_corner_3", false);
        registerTile(33, "path/path_edge_3", false);
        registerTile(34, "path/path_corner_4", false);
        registerTile(35, "path/path_outer_corner_1", false);
        registerTile(36, "path/path_outer_corner_2", false);
        registerTile(37, "path/path_outer_corner_3", false);
        registerTile(38, "path/path_outer_corner_4", false);

        // Building Stuff
        registerTile(39, "floor", false);
        registerTile(40, "planks", true);

        // Tree
        registerTile(41, "tree", true);

        // Event Tiles
        registerTile(42, "path/path_pit", false);
        registerTile(43, "grass/grass_pit", false);
        registerTile(44, "grass/grass_healing", false);

        registerTile(45, "lil_hut", false);
    }
    public void registerTile(int i, String imageName, boolean collision) {
        try {
            tile.put(i, new Tile());
            try {
                tile.get(i).image = ImageIO.read(getClass().getResourceAsStream("/drawable/tiles/" + imageName + ".png"));
            } catch (IllegalArgumentException e) {
                System.err.println("\"" + imageName + "\" is not a valid path.");
                tile.get(i).image = ImageIO.read(getClass().getResourceAsStream("/drawable/disabled.png"));
            }
            tile.get(i).image = UtilityTool.scaleImage(tile.get(i).image, game.tileSize, game.tileSize);
            tile.get(i).collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public void draw(Graphics2D graphics2D) {
    int playerWorldX = game.player.worldX;
    int playerWorldY = game.player.worldY;
    int playerScreenX = game.player.screenX;
    int playerScreenY = game.player.screenY;
    int tileSize = game.tileSize;

    for (int worldRow = 0; worldRow < game.maxWorldRow; worldRow++) {
        for (int worldCol = 0; worldCol < game.maxWorldCol; worldCol++) {
            int tileNumber = mapTileNum[game.mapHandler.mapNumbers.get(game.currentMap)][worldCol][worldRow];
            int worldX = worldCol * tileSize;
            int worldY = worldRow * tileSize;
            int screenX = worldX - playerWorldX + playerScreenX;
            int screenY = worldY - playerWorldY + playerScreenY;

            // Check if the tile is within the visible screen
            if (worldX + tileSize > playerWorldX - playerScreenX &&
                worldX - tileSize < playerWorldX + playerScreenX &&
                worldY + tileSize > playerWorldY - playerScreenY &&
                worldY - tileSize < playerWorldY + playerScreenY) {

                Tile currentTile = tile.get(tileNumber);
                if (currentTile != null) {
                    graphics2D.drawImage(currentTile.image, screenX, screenY, null);

                    if (game.debugHitBoxes && currentTile.collision) {
                        graphics2D.setColor(new Color(0.7f, 0, 0, 0.3f));
                        graphics2D.fillRect(screenX, screenY, tileSize, tileSize);
                    }
                } else {
                    System.err.println("Index " + tileNumber + " is not a valid tile.");
                    registerTile(tileNumber, "", false);
                }
            }
        }
    }

    if (game.debugPathfinding) {
        graphics2D.setColor(new Color(0.7f, 0, 0, 0.3f));
        for (var pathNode : game.pathFinder.pathList) {
            int worldX = pathNode.col * tileSize;
            int worldY = pathNode.row * tileSize;
            int screenX = worldX - playerWorldX + playerScreenX;
            int screenY = worldY - playerWorldY + playerScreenY;

            graphics2D.fillRect(screenX, screenY, tileSize, tileSize);
        }
    }
}
}
