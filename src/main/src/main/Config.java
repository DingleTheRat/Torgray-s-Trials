package main;

import java.io.*;

public class Config {
    GamePanel gamePanel;

    public Config(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
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
            if (gamePanel.fullScreen) {
                bufferedWriter.write("true");
            } else {
                bufferedWriter.write("false");
            }
            bufferedWriter.newLine();

            // BRendering
            if (gamePanel.BRendering) {
                bufferedWriter.write("true");
            } else {
                bufferedWriter.write("false");
            }
            bufferedWriter.newLine();

            // Music Volume
            bufferedWriter.write(String.valueOf(gamePanel.music.volumeScale));
            bufferedWriter.newLine();

            // Sound Volume
            bufferedWriter.write(String.valueOf(gamePanel.sound.volumeScale));
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
                gamePanel.fullScreen = true;
            } else {
                gamePanel.fullScreen = false;
            }

            // BRendering
            s = bufferedReader.readLine();
            if (s.equals("true")) {
                gamePanel.BRendering = true;
            } else {
                gamePanel.BRendering = false;
            }


            // Music Volume
            s = bufferedReader.readLine();
            gamePanel.music.volumeScale = Integer.parseInt(s);

            // Sound Volume
            s = bufferedReader.readLine();
            gamePanel.sound.volumeScale = Integer.parseInt(s);

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
