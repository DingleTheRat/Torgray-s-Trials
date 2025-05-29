package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.main.Game;

import java.awt.*;

public class Particle extends Entity{
    Entity generator;
    Color color;
    int size;
    int xd;
    int yd;

    public Particle(Game game, Entity generator, Color color, int size, int speed, int maxHealth, int xd, int yd) {
        super(game);

        this.generator = generator;
        this.color = color;
        this.size = size;
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.xd = xd;
        this.yd = yd;

        health = maxHealth;
        int offset = (game.tileSize / 2) - (size / 2);
        worldX = generator.worldX + offset;
        worldY = generator.worldY + offset;
    }
    public void update() {
        health--;

        if (health == maxHealth / 3) {
            yd++;
        }

        worldX += xd * speed;
        worldY += yd * speed;

        if (health == 0) {
            alive = false;
        }
    }
    public void draw(Graphics2D graphics2D) {
        float screenX = worldX - game.player.worldX + game.player.screenX;
        float screenY = worldY - game.player.worldY + game.player.screenY;

        graphics2D.setColor(color);
        graphics2D.fillRect(Math.round(screenX), Math.round(screenY), size, size);
    }
}
