package net.dinglezz.torgrays_trials.environment;

import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;

import java.awt.*;

public class EnvironmentManager {
    Game game;
    public Lighting lighting;
    public boolean lightUpdated = false;

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

    public String getDarknessStateString() {
        String darknessStateString = switch (lighting.darknessState) {
            case DARKNESS_STATE_NIGHT -> "Night";
            case DARKNESS_STATE_NEW_DUSK -> "New Dusk";
            case DARKNESS_STATE_GLOOM -> "Gloom";
            case DARKNESS_STATE_DUSK -> "Dusk";
            default -> "Error";
        };
        return darknessStateString;
    }
    public States getDarknessState() {
        States darknessState = lighting.darknessState;
        return darknessState;
    }
}
