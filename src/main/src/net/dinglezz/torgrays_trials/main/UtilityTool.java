package net.dinglezz.torgrays_trials.main;

import org.json.JSONObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
