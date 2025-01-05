package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[50];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        getTileImage();
        loadMap("/maps/world02.txt");
    }

    public void getTileImage() {
        // Disabled
        registerTile(0, "disabled", false);
        registerTile(1, "disabled", false);
        registerTile(2, "disabled", false);
        registerTile(3, "disabled", false);
        registerTile(4, "disabled", false);
        registerTile(5, "disabled", false);
        registerTile(6, "disabled", false);
        registerTile(7, "disabled", false);
        registerTile(8, "disabled", false);
        registerTile(9, "disabled", false);

        // Grass
        registerTile(10, "grass_1", false);
        registerTile(11, "grass_2", false);

        // Water
        registerTile(12, "water", true);
        registerTile(13, "white_line_water", true);
        registerTile(14, "water_corner_1", true);
        registerTile(15, "water_edge_3", true);
        registerTile(16, "water_corner_3", true);
        registerTile(17, "water_edge_4", true);
        registerTile(18, "water_edge_2", true);
        registerTile(19, "water_corner_2", true);
        registerTile(20, "water_edge_1", true);
        registerTile(21, "water_corner_4", true);
        registerTile(22, "water_outer_corner_1", true);
        registerTile(23, "water_outer_corner_2", true);
        registerTile(24, "water_outer_corner_3", true);
        registerTile(25, "water_outer_corner_4", true);

        // Path
        registerTile(26, "path", false);
        registerTile(27, "path_outer_corner_2", false);
        registerTile(28, "path_edge_1", false);
        registerTile(29, "path_outer_corner_3", false);
        registerTile(30, "path_edge_4", false);
        registerTile(31, "path_edge_2", false);
        registerTile(32, "path_outer_corner_4", false);
        registerTile(33, "path_edge_3", false);
        registerTile(34, "path_outer_corner_1", false);
        registerTile(35, "path_corner_4", false);
        registerTile(36, "path_corner_2", false);
        registerTile(37, "path_corner_3", false);
        registerTile(38, "path_corner_1", false);

        // Dirt
        registerTile(39, "dirt", false);

        // Wall
        registerTile(40, "wall", true);

        // Tree
        registerTile(41, "tree", true);
    }
    public void registerTile(int i, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {
            tile[i] = new Tile();
            tile[i].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/" + imageName + ".png")));
            tile[i].image = uTool.scaleImage(tile[i].image, gp.tileSize, gp.tileSize);
            tile[i].collision = collision;
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

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();
                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
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

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
