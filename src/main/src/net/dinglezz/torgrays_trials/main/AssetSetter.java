package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.tile.MapHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AssetSetter {
    public static void setObjects(boolean removePrevious) {
        JSONObject file = MapHandler.mapFiles.get(Main.game.currentMap);
        if (file != null) {
            Main.game.object.putIfAbsent(Main.game.currentMap, new HashMap<>());
            if (removePrevious) {
                Main.game.object.get(Main.game.currentMap).clear();
            }

            try {
                JSONArray objects = file.getJSONObject("entities").getJSONArray("objects");

                for (int i = 0; i < objects.length(); i++) {
                    JSONObject object = objects.getJSONObject(i);
                    String path = object.getString("path");
                    int col = object.getInt("col");
                    int row = object.getInt("row");

                    if (object.has("loot table")) {
                        String lootTable = object.getString("loot table");
                        Main.game.object.get(Main.game.currentMap).putIfAbsent(i, UtilityTool.generateLootTableEntity(path, lootTable));
                    } else {
                        Main.game.object.get(Main.game.currentMap).putIfAbsent(i, UtilityTool.generateEntity(path));
                    }
                    Main.game.object.get(Main.game.currentMap).get(i).worldX = col * Main.game.tileSize;
                    Main.game.object.get(Main.game.currentMap).get(i).worldY = row * Main.game.tileSize;
                }
            } catch (JSONException e) {
                System.err.println("Couldn't load objects");
                System.err.println("Missing object data in " + Main.game.currentMap + ".json");
            }
        }
    }
    public static void setNPCs(boolean removePrevious) {
        JSONObject file = MapHandler.mapFiles.get(Main.game.currentMap);
        if (file != null) {
            Main.game.npc.putIfAbsent(Main.game.currentMap, new HashMap<>());
            if (removePrevious) {
                Main.game.npc.get(Main.game.currentMap).clear();
            }

            try {
                JSONArray npcs = file.getJSONObject("entities").getJSONArray("npcs");

                for (int i = 0; i < npcs.length(); i++) {
                    JSONObject npc = npcs.getJSONObject(i);
                    String path = npc.getString("path");
                    int col = npc.getInt("col");
                    int row = npc.getInt("row");

                    if (npc.has("loot table")) {
                        String lootTable = npc.getString("loot table");
                        Main.game.npc.get(Main.game.currentMap).putIfAbsent(i, UtilityTool.generateLootTableEntity(path, lootTable));
                    } else {
                        Main.game.npc.get(Main.game.currentMap).putIfAbsent(i, UtilityTool.generateEntity(path));
                    }
                    Main.game.npc.get(Main.game.currentMap).putIfAbsent(i, UtilityTool.generateEntity(path));
                    Main.game.npc.get(Main.game.currentMap).get(i).worldX = col * Main.game.tileSize;
                    Main.game.npc.get(Main.game.currentMap).get(i).worldY = row * Main.game.tileSize;
                }
            } catch (JSONException e) {
                System.err.println("Couldn't load npcs");
                System.err.println("Missing npc data in " + Main.game.currentMap + ".json");
            }
        }
    }
    public static void setMonsters(boolean removePrevious) {
        JSONObject file = MapHandler.mapFiles.get(Main.game.currentMap);
        if (file != null) {
            Main.game.monster.putIfAbsent(Main.game.currentMap, new HashMap<>());
            if (removePrevious) {
                Main.game.monster.get(Main.game.currentMap).clear();
            }

            try {
                JSONArray monsters = file.getJSONObject("entities").getJSONArray("monsters");

                for (int i = 0; i < monsters.length(); i++) {
                    JSONObject monster = monsters.getJSONObject(i);
                    String path = monster.getString("path");
                    int col = monster.getInt("col");
                    int row = monster.getInt("row");

                    if (monster.has("loot table")) {
                        String lootTable = monster.getString("loot table");
                        Main.game.monster.get(Main.game.currentMap).putIfAbsent(i, UtilityTool.generateLootTableEntity(path, lootTable));
                    } else {
                        Main.game.monster.get(Main.game.currentMap).putIfAbsent(i, UtilityTool.generateEntity(path));
                    }
                    Main.game.monster.get(Main.game.currentMap).get(i).worldX = col * Main.game.tileSize;
                    Main.game.monster.get(Main.game.currentMap).get(i).worldY = row * Main.game.tileSize;
                }
            } catch (JSONException e) {
                System.err.println("Couldn't load monsters");
                System.err.println("Missing monster data in " + Main.game.currentMap + ".json");
            }
        }
    }
}
