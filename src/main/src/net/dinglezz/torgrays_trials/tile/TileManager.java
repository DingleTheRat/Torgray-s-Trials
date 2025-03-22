package net.dinglezz.torgrays_trials.tile;

import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class TileManager {
    Game game;
    public HashMap<Integer, Tile> tile;
    public int[][] mapTileNum;

    public TileManager(Game game) {
        this.game = game;
        tile = new HashMap<>();
        mapTileNum = new int[game.maxWorldCol][game.maxWorldRow];
        getTileImage();
        loadMap("/values/maps/world02.txt");
    }

    public void getTileImage() {

        // Grass
        registerTile(10, "grass_1", false);
        registerTile(11, "grass_2", false);

        // Water
        registerTile(12, "/water/water", true);
        registerTile(13, "/water/white_line_water", true);
        registerTile(14, "/water/water_corner_1", true);
        registerTile(15, "/water/water_edge_3", true);
        registerTile(16, "/water/water_corner_3", true);
        registerTile(17, "/water/water_edge_4", true);
        registerTile(18, "/water/water_edge_2", true);
        registerTile(19, "/water/water_corner_2", true);
        registerTile(20, "/water/water_edge_1", true);
        registerTile(21, "/water/water_corner_4", true);
        registerTile(22, "/water/water_outer_corner_1", true);
        registerTile(23, "/water/water_outer_corner_2", true);
        registerTile(24, "/water/water_outer_corner_3", true);
        registerTile(25, "/water/water_outer_corner_4", true);

        // Path
        registerTile(26, "/path/path", false);
        registerTile(27, "/path/path_corner_1", false);
        registerTile(28, "/path/path_edge_1", false);
        registerTile(29, "/path/path_corner_2", false);
        registerTile(30, "/path/path_edge_4", false);
        registerTile(31, "/path/path_edge_2", false);
        registerTile(32, "/path/path_corner_3", false);
        registerTile(33, "/path/path_edge_3", false);
        registerTile(34, "/path/path_corner_4", false);
        registerTile(35, "/path/path_outer_corner_1", false);
        registerTile(36, "/path/path_outer_corner_2", false);
        registerTile(37, "/path/path_outer_corner_3", false);
        registerTile(38, "/path/path_outer_corner_4", false);

        // Dirt
        registerTile(39, "dirt", false);

        // Wall
        registerTile(40, "wall", true);

        // Tree
        registerTile(41, "tree", true);

        // Event Ties
        registerTile(42, "/path/path_pit", false);
        registerTile(43, "grass_pit", false);
        registerTile(44, "grass_healing", false);
    }
    public void registerTile(int i, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();
        try {
            tile.put(i, new Tile());
            try {
                tile.get(i).image = ImageIO.read(getClass().getResourceAsStream("/drawable/tiles/" + imageName + ".png"));
            } catch (IllegalArgumentException e) {
                System.out.println("\"" + imageName + "\" is not a valid path.");
                tile.get(i).image = ImageIO.read(getClass().getResourceAsStream("/drawable/tiles/disabled.png"));
            }
            tile.get(i).image = uTool.scaleImage(tile.get(i).image, game.tileSize, game.tileSize);
            tile.get(i).collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;

            while (col < game.maxWorldCol && row < game.maxWorldRow) {
                String line = br.readLine();
                while (col < game.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == game.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < game.maxWorldCol && worldRow < game.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
            int worldX = worldCol * game.tileSize;
            int worldY = worldRow * game.tileSize;
            int screenX = worldX - game.player.worldX + game.player.screenX;
            int screenY = worldY - game.player.worldY + game.player.screenY;

            if (worldX + game.tileSize > game.player.worldX - game.player.screenX &&
                    worldX - game.tileSize < game.player.worldX + game.player.screenX &&
                    worldY + game.tileSize > game.player.worldY - game.player.screenY &&
                    worldY - game.tileSize < game.player.worldY + game.player.screenY) {
                g2.drawImage(tile.get(tileNum).image, screenX, screenY, null);
            }
            worldCol++;
            if (worldCol == game.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
