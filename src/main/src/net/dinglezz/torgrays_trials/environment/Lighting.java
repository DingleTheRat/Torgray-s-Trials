package net.dinglezz.torgrays_trials.environment;

import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Lighting {
    Game game;
    BufferedImage darknessFilter;
    public States darknessState = States.DARKNESS_STATE_NIGHT;
    public int darknessCounter = 0;

    public Lighting(Game game) {
        this.game = game;
        setLightSource();
    }

    public void setLightSource() {
        // Make buffered image for darkness filter
        darknessFilter = new BufferedImage(game.screenWidth, game.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D) darknessFilter.getGraphics();

        // Calculate the centre of the player
        int centreX = game.player.screenX + (game.tileSize) / 2;
        int centreY = game.player.screenY + (game.tileSize) / 2;

        // Create a radial gradient paint depending on the darkness state
        Color[] color = new Color[12];
        float[] fraction = new float[12];

        if ((darknessState == States.DARKNESS_STATE_NIGHT) ||
                (darknessState == States.DARKNESS_STATE_NEW_DUSK && !game.ui.fadeBack) ||
                (darknessState == States.DARKNESS_STATE_DUSK && game.ui.fadeBack)) {
            color[0] = new Color(0, 0, 0.1f, 0.1f);
            color[1] = new Color(0, 0, 0.1f, 0.15f);
            color[2] = new Color(0, 0, 0.1f, 0.2f);
            color[3] = new Color(0, 0, 0.1f, 0.3f);
            color[4] = new Color(0, 0, 0.1f, 0.4f);
            color[5] = new Color(0, 0, 0.1f, 0.5f);
            color[6] = new Color(0, 0, 0.1f, 0.6f);
            color[7] = new Color(0, 0, 0.1f, 0.7f);
            color[8] = new Color(0, 0, 0.1f, 0.78f);
            color[9] = new Color(0, 0, 0.1f, 0.84f);
            color[10] = new Color(0, 0, 0.1f, 0.88f);
            color[11] = new Color(0, 0, 0.1f, 0.88f);

            fraction[0] = 0f;
            fraction[1] = 0.4f;
            fraction[2] = 0.5f;
            fraction[3] = 0.6f;
            fraction[4] = 0.65f;
            fraction[5] = 0.7f;
            fraction[6] = 0.75f;
            fraction[7] = 0.8f;
            fraction[8] = 0.85f;
            fraction[9] = 0.9f;
            fraction[10] = 0.95f;
            fraction[11] = 1f;
        }
        if ((darknessState == States.DARKNESS_STATE_GLOOM) ||
                (darknessState == States.DARKNESS_STATE_DUSK && !game.ui.fadeBack) ||
                (darknessState == States.DARKNESS_STATE_NEW_DUSK && game.ui.fadeBack)) {
            color[0] = new Color(0, 0, 0.1f, 0.1f);
            color[1] = new Color(0, 0, 0.1f, 0.42f);
            color[2] = new Color(0, 0, 0.1f, 0.52f);
            color[3] = new Color(0, 0, 0.1f, 0.61f);
            color[4] = new Color(0, 0, 0.1f, 0.69f);
            color[5] = new Color(0, 0, 0.1f, 0.76f);
            color[6] = new Color(0, 0, 0.1f, 0.82f);
            color[7] = new Color(0, 0, 0.1f, 0.87f);
            color[8] = new Color(0, 0, 0.1f, 0.91f);
            color[9] = new Color(0, 0, 0.1f, 0.94f);
            color[10] = new Color(0, 0, 0.1f, 0.96f);
            color[11] = new Color(0, 0, 0.1f, 0.99f);

            fraction[0] = 0f;
            fraction[1] = 0.4f;
            fraction[2] = 0.5f;
            fraction[3] = 0.6f;
            fraction[4] = 0.65f;
            fraction[5] = 0.7f;
            fraction[6] = 0.75f;
            fraction[7] = 0.8f;
            fraction[8] = 0.85f;
            fraction[9] = 0.9f;
            fraction[10] = 0.95f;
            fraction[11] = 1f;
        }

        // Set the radius of the darkness filter
        if (game.currentMap.equals("Coiner's Shop")) {
            RadialGradientPaint gradientPaint = new RadialGradientPaint(centreX, centreY, 500, fraction, color);
            graphics2D.setPaint(gradientPaint);
        } else if (game.player.currentLight == null) {
            graphics2D.setColor(color[11]);
            graphics2D.setPaint(null);
        } else {
            RadialGradientPaint gradientPaint = new RadialGradientPaint(centreX, centreY, game.player.currentLight.lightRadius, fraction, color);
            graphics2D.setPaint(gradientPaint);
        }

        // Fill the buffered image with the radial gradient paint
        graphics2D.fillRect(0, 0, game.screenWidth, game.screenHeight);
        graphics2D.dispose();
    }
    public void update() {
        if (game.environmentManager.lightUpdated) {
            setLightSource();
            game.environmentManager.lightUpdated = false;
        }

        // Darkness state stuff
        switch (darknessState) {
            case DARKNESS_STATE_NIGHT: updateDarknessState(States.DARKNESS_STATE_NEW_DUSK, 600, true); break;
            case DARKNESS_STATE_NEW_DUSK: updateDarknessState(States.DARKNESS_STATE_GLOOM, 1, false); break;
            case DARKNESS_STATE_GLOOM: updateDarknessState(States.DARKNESS_STATE_DUSK, 600, true); break;
            case DARKNESS_STATE_DUSK: updateDarknessState(States.DARKNESS_STATE_NIGHT, 1, false); break;
        }
    }
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(new Color(0, 0, 0.1f, 0.25f));
        graphics2D.fillRect(0, 0, game.screenWidth, game.screenHeight);

        graphics2D.drawImage(darknessFilter, 0, 0, null);
    }

    private void updateDarknessState(States nextState, int threshold, boolean transitionWhenDone) {
        if (game.ui.transitioning) return;
        darknessCounter++;
        if (darknessCounter < threshold)  return;

        darknessCounter = 0;
        darknessState = nextState;

        if (transitionWhenDone) {
            game.ui.transitioning = true;

            // Set the transition settings
            game.ui.transitionOpenSpeed = (darknessState == States.DARKNESS_STATE_NEW_DUSK) ? 0.002f : 0.02f;
            game.ui.transitionCloseSpeed = (darknessState == States.DARKNESS_STATE_DUSK) ? 0.002f : 0.02f;
            game.ui.actionMethod = "transitionDarkness";
            game.ui.transitionColor = new Color(0, 0, 0.1f);
        }
    }
}
