package net.dinglezz.torgrays_trials.tile;

import net.dinglezz.torgrays_trials.main.Game;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MapHandler {
    Game game;
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
        BufferedReader bufferedReader;
        try {
            // Input Stream for the map file
            InputStream inputStream = getClass().getResourceAsStream("/values/maps/" + fileName + ".json");
            if (inputStream == null) {
                inputStream = getClass().getResourceAsStream("/values/maps/Disabled.txt");
                System.err.println("Warning: \"" + fileName + "\" is not a valid path.");
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            for (String line; (line = bufferedReader.readLine()) != null;) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            JSONObject json = new JSONObject(stringBuilder.toString());
            String name = json.getString("name");
            int numberKey = json.getInt("numberKey");
            JSONArray map = json.getJSONArray("map");

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

            mapStrings.put(numberKey, name);
            mapNumbers.put(name, numberKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
