package net.dinglezz.torgrays_trials.main;

import java.io.*;

public class Config {
    Game game;

    public Config(Game game) {
        this.game = game;
    }

    public void saveConfig() {
        try {
            String userHome = System.getProperty("user.home");
            File configFile = new File(userHome, "torgrays-trials-config.txt");
            
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));

            // Music Volume
            bufferedWriter.write(String.valueOf(game.music.volumeScale));
            bufferedWriter.newLine();

            // Sound Volume
            bufferedWriter.write(String.valueOf(game.sound.volumeScale));
            bufferedWriter.newLine();

            // Full screen
            bufferedWriter.write(String.valueOf(game.fullScreen));
            bufferedWriter.newLine();

            // BRendering
            bufferedWriter.write(String.valueOf(game.BRendering));
            bufferedWriter.newLine();

            // Pathfinding
            bufferedWriter.write(String.valueOf(game.pathFinding));
            bufferedWriter.newLine();

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadConfig() {
        try {
            String userHome = System.getProperty("user.home");
            File configFile = new File(userHome, "torgrays-trials-config.txt");
            if (configFile.createNewFile()) {
                FileWriter fileWriter = new FileWriter(configFile);
                fileWriter.write("3\n");
                fileWriter.write("3\n");
                fileWriter.write("false\n");
                fileWriter.write("false\n");
                fileWriter.write("false\n");
                fileWriter.close();
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
            String s = bufferedReader.readLine();

            // Music Volume
            game.music.volumeScale = Integer.parseInt(s);

            // Sound Volume
            s = bufferedReader.readLine();
            game.sound.volumeScale = Integer.parseInt(s);

            // Full screen
            s = bufferedReader.readLine();
            game.fullScreen = s.equals("true");

            // BRendering
            s = bufferedReader.readLine();
            game.BRendering = s.equals("true");

            // Pathfinding
            s = bufferedReader.readLine();
            game.pathFinding = s.equals("true");

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
