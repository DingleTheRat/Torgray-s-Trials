// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import java.util.HashMap;


import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.main.World;
import net.dingletherat.torgrays_trials.rendering.Map;
import net.dingletherat.torgrays_trials.rendering.Tile;
import net.dingletherat.torgrays_trials.rendering.TileManager;
import net.dingletherat.torgrays_trials.rendering.TileManager.Position;

public class TileSystem implements System {
    @Override
    public void draw(World world) {
        if (!TileManager.maps.containsKey(world.getMap())) {
            Main.LOGGER.error("Map '{}' not found", world.getMap());
            return;
        }
        Map map = TileManager.maps.get(world.getMap());

        Main.batch.begin();

        // Draw the ground
        drawLayer(world, map, map.ground());
        drawLayer(world, map, map.foreground());

        Main.batch.end();
    }

    @Override
    public void update(World world, float deltaTime) { }

    private void drawLayer(World world, Map map, HashMap<Position, Integer> layer) {
        float cameraX = world.cameraX;
        float cameraY = world.cameraY;
        int tileSize = Main.tileSize;
        for (int worldRow = 0; worldRow < map.y(); worldRow++) {
            for (int worldCol = 0; worldCol < map.x(); worldCol++) {
                int tileNumber = layer.get(new Position(worldCol, worldRow));
                int worldX = worldCol * tileSize;
                int worldY = worldRow * tileSize;
                float screenX = worldX - cameraX + Main.screenWidth / 2f;
                float screenY = worldY - cameraY + Main.screenHeight / 2f;

                // Check if the tile is within the visible screen
                if (worldX + tileSize > cameraX - Main.screenWidth / 2f &&
                    worldX - tileSize < cameraX + Main.screenWidth / 2f &&
                    worldY + tileSize > cameraY - Main.screenHeight / 2f &&
                    worldY - tileSize < cameraY + Main.screenHeight / 2f) {
                    Tile currentTile = TileManager.tileTypes.get(tileNumber);
                    Main.batch.draw(currentTile.image().getTexture(), Math.round(screenX), Math.round(screenY));
                }
            }
        }
    }
}
