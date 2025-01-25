package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    GamePanel gamePanel;

    public Config(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void saveConfig() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("config.txt"));

            if (gamePanel.fullScreen) {
                bufferedWriter.write("true");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadConfig() {

    }
}
