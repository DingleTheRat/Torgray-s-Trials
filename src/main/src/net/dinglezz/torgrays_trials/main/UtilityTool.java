package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.event.Event;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

public class UtilityTool {

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }

    public static JSONObject getJsonObject(String filePath) {
        try (InputStream inputStream = UtilityTool.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.err.println("Warning: \"" + filePath + "\" is not a valid path.");
                return null;
            }

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                return new JSONObject(stringBuilder.toString());
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Entity generateEntity(String path) {
        try {
            Class<?> clazz = Class.forName(path);
            return (Entity) clazz.getDeclaredConstructor(Game.class).newInstance(Main.game);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
    public static Entity generateEntity(String path, String lootTable) {
        try {
            Class<?> clazz = Class.forName(path);
            return (Entity) clazz.getDeclaredConstructor(Game.class, String.class).newInstance(Main.game, lootTable);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                 | NoSuchMethodException |
                 ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }
    public static Event generateEvent(String path, String map, int col, int row, JSONObject parameters) {
        TilePoint tilePoint = new TilePoint(map, col, row);
        try {
            Class<?> clazz = Class.forName(path);
            return (Event) clazz.getDeclaredConstructor(TilePoint.class, JSONObject.class).newInstance(tilePoint, parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException | ClassNotFoundException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String[] getResourceFileNames(String directoryPath) {
        try (InputStream inputStream = UtilityTool.class.getResourceAsStream(directoryPath)) {
            if (inputStream == null) {
                System.err.println("Warning: \"" + directoryPath + "\" is not a valid path.");
                return new String[0];
            }

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                return bufferedReader.lines().toArray(String[]::new);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
