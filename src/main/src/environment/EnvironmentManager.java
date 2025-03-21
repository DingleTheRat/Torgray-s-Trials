package environment;

import main.GamePanel;

import java.awt.*;

public class EnvironmentManager {
    GamePanel gamePanel;
    Lighting lighting;

    public EnvironmentManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setup() {
        lighting = new Lighting(gamePanel);
    }
    public void update() {
        lighting.update();
    }
    public void draw(Graphics2D g2) {
        if (lighting != null) {
            lighting.draw(g2);
        }
    }
}
