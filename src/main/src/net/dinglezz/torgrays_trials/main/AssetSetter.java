package net.dinglezz.torgrays_trials.main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AssetSetter {
//    public void setObjects() {
//        setObjects(true);
//        // Disabled Map
//            assetMap.put(0, null);
//            game.object.put("Disabled", assetMap);
//            assetMap = new HashMap<>();
//
//        // Main Island Map
//            // Keys
//            setAsset(new OBJ_Key(game), 23, 40);
//            setAsset(new OBJ_Key(game), 38, 8);
//
//            // Gates
//            setAsset(new OBJ_Gate(game), 10, 12);
//            setAsset(new OBJ_Gate(game), 14, 28);
//            setAsset(new OBJ_Gate(game), 8, 28);
//            setAsset(new OBJ_Gate(game), 12, 23);
//
//            // Chests
//            setAsset(new OBJ_Chest(game, new ArrayList<>(
//                List.of(new OBJ_Sword_Amethyst(game),
//                        new OBJ_Shield_Amethyst(game),
//                        new OBJ_Coins(game, 4)
//                ))), 10, 10);
//            setAsset(new OBJ_Dark_Chest(game, LootTable.LOOT_TABLE_DARK_CHEST), 37, 40);
//            setAsset(new OBJ_Dark_Chest(game, LootTable.LOOT_TABLE_DARK_CHEST), 31, 10);
//
//            game.object.put("Main Island", assetMap);
//            assetMap = new HashMap<>();
//            i = 0;
//
//        // Coiner's Shop Map
//            setAsset(new OBJ_Table(game), 12, 10);
//
//            setAsset(new OBJ_Dark_Chest(game, LootTable.LOOT_TABLE_DARK_CHEST), 10, 9);
//
//            game.object.put("Coiner's Shop", assetMap);
//            assetMap = new HashMap<>();
//            i = 0;
//    }
//    public void setNPCs() {
//        // Disabled Map
//            assetMap.put(0, null);
//            game.npc.put("Disabled", assetMap);
//            assetMap = new HashMap<>();
//
//        // Main Island Map
//            setAsset(new NPC_GateKeeper(game), 21, 21);
//
//            game.npc.put("Main Island", assetMap);
//            assetMap = new HashMap<>();
//            i = 0;
//
//        // Coiner's Shop Map
//            setAsset(new NPC_Coiner(game), 12, 9);
//
//            game.npc.put("Coiner's Shop", assetMap);
//            assetMap = new HashMap<>();
//            i = 0;
//    }
//    public void setMonsters() {
//        // Disabled Map
//            assetMap.put(0, null);
//            game.monster.put("Disabled", assetMap);
//            assetMap = new HashMap<>();
//
//        // Main Island Map
//            // In grass thing
//            setAsset(new MON_Dracore(game), 23, 36);
//            setAsset(new MON_Dracore(game), 24, 37);
//            setAsset(new MON_Dracore(game), 25, 38);
//
//            // In path thing
//            setAsset(new MON_Dracore(game), 35, 10);
//            setAsset(new MON_Dracore(game), 37, 8);
//            setAsset(new MON_Dracore(game), 39, 10);
//
//            // In gate thing
//            setAsset(new MON_Dracore(game), 10, 29);
//            setAsset(new MON_Dracore(game), 11, 29);
//            setAsset(new MON_Dracore(game), 12, 29);
//
//            // In that one place with a bit of trees
//            setAsset(new MON_Dracore(game), 36, 36);
//            setAsset(new MON_Dracore(game), 36, 32);
//            setAsset(new MON_Dracore(game), 37, 41);
//            setAsset(new MON_Dracore(game), 31, 40);
//
//            game.monster.put("Main Island", assetMap);
//            assetMap = new HashMap<>();
//            i = 0;
//
//        // Coiner's Shop Map
//            assetMap.put(0, null);
//            game.monster.put("Coiner's Shop", assetMap);
//            assetMap = new HashMap<>();
//            i = 0;
//    }
//
//    public void setAsset(Entity asset, int col, int row) {
//        assetMap.put(i, asset);
//        assetMap.get(i).worldX = game.tileSize * col;
//        assetMap.get(i).worldY = game.tileSize * row;
//        i++;
//    }

    public static void setObjects(boolean removePrevious) {
        JSONObject file = Main.game.mapHandler.mapFiles.get(Main.game.currentMap);
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

                    Main.game.object.get(Main.game.currentMap).putIfAbsent(i, UtilityTool.generateEntity(path));
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
        JSONObject file = Main.game.mapHandler.mapFiles.get(Main.game.currentMap);
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
        JSONObject file = Main.game.mapHandler.mapFiles.get(Main.game.currentMap);
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

                    Main.game.monster.get(Main.game.currentMap).putIfAbsent(i, UtilityTool.generateEntity(path));
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
