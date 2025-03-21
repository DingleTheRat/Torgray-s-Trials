package net.dinglezz.torgrays_trials.environment;

import net.dinglezz.torgrays_trials.main.Game;

import java.awt.*;

public class EnvironmentManager {
    Game game;
    Lighting lighting;

    public EnvironmentManager(Game game) {
        this.game = game;
    }

    public void setup() {
        lighting = new Lighting(game);
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
