package net.dinglezz.torgrays_trials.entity;

import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.UtilityTool;
import net.dinglezz.torgrays_trials.object.OBJ_Coins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class LootTable {
    public static HashMap<String, JSONObject> lootTables = new HashMap<>();
    public static final Entity RANDOM_COIN = new OBJ_Coins(Main.game, 0);

    public static void registerLootTable(String fileName) {
        JSONObject file = UtilityTool.getJsonObject("/values/loot_tables/" + fileName + ".json");
        try {
            String name = file.getString("name");
            lootTables.put(name, file);
        } catch (JSONException e) {
            System.err.println("Couldn't load loot table");
            System.err.println("Missing loot table data in " + fileName + ".json");
        }
    }

     public static void loadLootTables() {
         String[] mapFiles = UtilityTool.getResourceFileNames("/values/loot_tables");
         for (String mapFile : mapFiles) {
             if (mapFile.endsWith(".json")) {
                 String mapName = mapFile.substring(0, mapFile.lastIndexOf(".json"));
                 registerLootTable(mapName);
             } else if (!mapFile.contains(".")) { // Check if it's a directory
                 String[] subFiles = UtilityTool.getResourceFileNames("/values/loot_tables/" + mapFile);
                 for (String subFile : subFiles) {
                     if (subFile.endsWith(".json")) {
                         String mapName = subFile.substring(0, subFile.lastIndexOf(".json"));
                         registerLootTable(mapFile + "/" + mapName);
                     }
                 }
             }
         }
     }

     public static ArrayList<Entity> generateLoot(JSONObject lootTable) {
         ArrayList<Entity> loot = new ArrayList<>();
         String lootType;
         JSONArray potentialLoot;

        try {
            // Get required data
            lootType = lootTable.getString("type");
            potentialLoot = lootTable.getJSONArray("loot");
        } catch (JSONException e) {
            System.err.println("Couldn't load loot table");
            System.err.println("Missing loot table data in " + " in loot table by the name of '" + lootTable.getString("name") + "'");
            return loot;
        }

        // If lootType is "single", choose a single loot item
         switch (lootType) {
             case "single" -> {
                 float random = new Random().nextFloat(); // Random number between 0 and 1
                 float cumulativeChance = 0f;


                 for (int i = 0; i < potentialLoot.length(); i++) {
                     JSONObject lootItem = potentialLoot.getJSONObject(i);

                     try {
                         float chance = lootItem.getFloat("chance");
                         cumulativeChance += chance;

                         if (random <= cumulativeChance) {
                             String object = lootItem.getString("object");

                             // Check for keywords
                             if ("RANDOM_COIN".equals(object)) {
                                 loot.add(RANDOM_COIN);
                             } else if (object.startsWith("COIN_")) {
                                 String coinValue = object.replace("COIN_", "");
                                 loot.add(new OBJ_Coins(Main.game, Integer.parseInt(coinValue)));
                             } else if (!object.isEmpty()) {
                                 loot.add(UtilityTool.generateEntity(object));
                             }
                             break;
                         }
                     } catch (JSONException e) {
                         System.err.println("Couldn't load loot item");
                         System.err.println("Missing loot item data in " + lootItem + " in loot table by the name of '" + lootTable.getString("name") + "'");
                     } catch (NumberFormatException e) {
                         System.err.println("Invalid coin format in loot item. Ensure it is 'COIN_X', where X is a number.");
                     }
                 }
             }
             case "multiple" -> {
                 for (int i = 0; i < potentialLoot.length(); i++) {
                     JSONObject lootItem = potentialLoot.getJSONObject(i);

                     try {
                         float random = new Random().nextFloat(); // Random number between 0 and 1
                         System.out.println(random);

                         if (random <= lootItem.getFloat("chance")) {
                             String object = lootItem.getString("object");

                             // Check for keywords
                             if (Objects.equals(object, "RANDOM_COIN")) {
                                 loot.add(RANDOM_COIN);
                             } else if (object.contains("COIN")) {
                                 object = object.replace("COIN_", "");
                                 loot.add(new OBJ_Coins(Main.game, Integer.parseInt(object)));
                             } else if (!object.isEmpty()) {
                                 loot.add(UtilityTool.generateEntity(object));
                             }
                         }
                     } catch (JSONException e) {
                         System.err.println("Couldn't load loot item");
                         System.err.println("Missing loot item data in " + lootItem + " in loot table by the name of '" + lootTable.getString("name") + "'");
                     } catch (NumberFormatException e) {
                         System.err.println("Invalid coin format in loot item: " + lootItem + " in loot table by the name of '" + lootTable.getString("name") + "'");
                         System.err.println("Make sure it is 'COIN_X', where X is a number");
                     }
                 }
             }
             // If it's "all", add all loot items
             case "all" -> {
                 for (int i = 0; i < potentialLoot.length(); i++) {
                     JSONObject lootItem = potentialLoot.getJSONObject(i);
                     try {
                         String object = lootItem.getString("object");

                         // Check for keywords
                         if (Objects.equals(object, "RANDOM_COIN")) {
                             loot.add(RANDOM_COIN);
                         } else if (object.contains("COIN")) {
                             object = object.replace("COIN_", "");
                             loot.add(new OBJ_Coins(Main.game, Integer.parseInt(object)));
                         } else if (!object.isEmpty()) {
                             loot.add(UtilityTool.generateEntity(object));
                         }
                     } catch (JSONException e) {
                         System.err.println("Couldn't load loot item");
                         System.err.println("Missing loot item data in " + lootItem + " in loot table by the name of '" + lootTable.getString("name") + "'");
                     } catch (NumberFormatException e) {
                         System.err.println("Invalid coin format in loot item: " + lootItem + " in loot table by the name of '" + lootTable.getString("name") + "'");
                         System.err.println("Make sure it is 'COIN_X', where X is a number");
                     }
                 }
             }
             default -> {
                 System.err.println("Invalid loot type: " + lootType + " in loot table by the name of '" + lootTable.getString("name") + "'");
                 System.err.println("Valid types are: single, multiple, all");
             }
         }
        return loot;
     }
}
