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
            
            BufferedWriter bufferedWriter =
                    new BufferedWriter(
                            new FileWriter(configFile)
                    );

            // Full screen
            if (game.fullScreen) {
                bufferedWriter.write("true");
            } else {
                bufferedWriter.write("false");
            }
            bufferedWriter.newLine();

            // BRendering
            if (game.BRendering) {
                bufferedWriter.write("true");
            } else {
                bufferedWriter.write("false");
            }
            bufferedWriter.newLine();

            // Music Volume
            bufferedWriter.write(String.valueOf(game.music.volumeScale));
            bufferedWriter.newLine();

            // Sound Volume
            bufferedWriter.write(String.valueOf(game.sound.volumeScale));
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
                fileWriter.write("false\n");
                fileWriter.write("true\n");
                fileWriter.write("3\n");
                fileWriter.write("3\n");
                fileWriter.close();
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
            String s = bufferedReader.readLine();

            // Full screen
            if (s.equals("true")) {
                game.fullScreen = true;
            } else {
                game.fullScreen = false;
            }

            // BRendering
            s = bufferedReader.readLine();
            if (s.equals("true")) {
                game.BRendering = true;
            } else {
                game.BRendering = false;
            }


            // Music Volume
            s = bufferedReader.readLine();
            game.music.volumeScale = Integer.parseInt(s);

            // Sound Volume
            s = bufferedReader.readLine();
            game.sound.volumeScale = Integer.parseInt(s);

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
