package net.dinglezz.torgrays_trials.tile;

import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.UtilityTool;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.text.Style;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class MapHandler {
    Game game;
    public final HashMap<String, JSONObject> mapFiles = new HashMap<>();
    public final HashMap<Integer, String> mapStrings = new HashMap<>();
    public final HashMap<String, Integer> mapNumbers = new HashMap<>();

    public MapHandler(Game game) {
        this.game = game;
        loadMap("disabled");
        loadMap("main_island");
        loadMap("coiner's_shop");
        createWorldMap();
    }

    public void loadMap(String fileName) {
        // Get the file and check if it exists
        JSONObject file = UtilityTool.getJsonObject("/values/maps/" + fileName + ".json");
        if (file == null) {
            file = UtilityTool.getJsonObject("/values/maps/disabled.json");
        }

        // Read the map file
        String name;
        int numberKey;
        JSONArray map;

        try {
            name = file.getString("name");
            numberKey = file.getInt("numberKey");
            map = file.getJSONArray("map");
        } catch (JSONException jsonException) {
            System.err.println("Failed to find essential map data in " + fileName + ".json. Using default map.");
            file = UtilityTool.getJsonObject("/values/maps/disabled.json");
            name = file.getString("name");
            numberKey = file.getInt("numberKey");
            map = file.getJSONArray("map");
        }

        // Read the map file
        int col = 0;
        int row = 0;

        while (row < game.maxWorldRow) {
            String mapLine = map.getString(row);
            while (col < game.maxWorldCol) {
                String[] numbers = mapLine.split(" ");
                int number = Integer.parseInt(numbers[col]);
                game.tileManager.mapTileNum[numberKey][col][row] = number;
                col++;
            }
            if (col == game.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        // Add to some useful HashMaps
        mapFiles.put(name, file);
        mapStrings.put(numberKey, name);
        mapNumbers.put(name, numberKey);
    }

    public void createWorldMap() {
        int worldMapWidth = game.tileSize * game.maxWorldCol;
        int worldMapHeight = game.tileSize * game.maxWorldRow;

        for (String map : mapStrings.values()) {
            game.tileManager.worldMap.put(map, new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB));
            Graphics2D graphics2D = game.tileManager.worldMap.get(map).createGraphics();

            int col = 0;
            int row = 0;
            while (col < game.maxWorldCol && row < game.maxWorldRow) {
                int tileNumber = game.tileManager.mapTileNum[mapNumbers.get(map)][col][row];
                int x = col * game.tileSize;
                int y = row * game.tileSize;

                // Draw Tiles
                if (game.tileManager.tile.get(tileNumber) != null) {
                    graphics2D.drawImage(game.tileManager.tile.get(tileNumber).image, x, y, null);
                }
                col++;
                if (col == game.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
        }
    }
}
