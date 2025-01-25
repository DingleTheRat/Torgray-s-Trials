package main;

import java.io.*;

public class Config {
    GamePanel gamePanel;

    public Config(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void saveConfig() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("config.txt"));

            // Full screen
            if (gamePanel.fullScreen) {
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
            BufferedReader bufferedReader = new BufferedReader(new FileReader("config.txt"));
            String s = bufferedReader.readLine();

            // Full screen
            if (s.equals("true")) {
                gamePanel.fullScreen = true;
            } else {
                gamePanel.fullScreen = false;
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
