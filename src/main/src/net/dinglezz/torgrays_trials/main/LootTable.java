package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.object.OBJ_Coins;
import net.dinglezz.torgrays_trials.object.OBJ_Torgray_Soup;
import net.dinglezz.torgrays_trials.object.weapon.OBJ_Stick;

import java.util.ArrayList;
import java.util.Random;

public class LootTable {
    // Non-static stuff
    public Entity loot;
    public float chance;

    public LootTable(Entity loot, float chance) {
        this.loot = loot;
        this.chance = chance;
    }

    // LootTables
    public static final Entity RANDOM_COIN = new OBJ_Coins(Main.game, 0);
    public static final ArrayList<LootTable> LOOT_TABLE_DRACORE_COINS = new ArrayList<>() {{
        add(new LootTable(new OBJ_Coins(Main.game, 1), 0.5f));
        add(new LootTable(new OBJ_Coins(Main.game, 2), 0.35f));
        add(new LootTable(new OBJ_Coins(Main.game, 3), 0.15f));
    }};
    public static final ArrayList<LootTable> LOOT_TABLE_DRACORE = new ArrayList<>() {{
        add(new LootTable(RANDOM_COIN, 0.4f));
        add(new LootTable(new OBJ_Torgray_Soup(Main.game), 0.35f));
        add(new LootTable(null, 0.25f)); // Nothing :D
    }};

    public static final ArrayList<LootTable> LOOT_TABLE_DARK_CHEST = new ArrayList<>() {{
        add(new LootTable(new OBJ_Torgray_Soup(Main.game), 0.55f));
        add(new LootTable(RANDOM_COIN, 0.35f));
        add(new LootTable(new OBJ_Stick(Main.game), 0.1f));
    }};
    public static final ArrayList<LootTable> LOOT_TABLE_DARK_CHEST_COINS = new ArrayList<>() {{
        add(new LootTable(new OBJ_Coins(Main.game, 1), 0.45f));
        add(new LootTable(new OBJ_Coins(Main.game, 2), 0.30f));
        add(new LootTable(new OBJ_Coins(Main.game, 3), 0.15f));
        add(new LootTable(new OBJ_Coins(Main.game, 4), 0.07f));
        add(new LootTable(new OBJ_Coins(Main.game, 5), 0.03f));
    }};

    // Methods
    public static Entity chooseSingleLoot(ArrayList<LootTable> lootTables) {
        // Sort
        lootTables.sort((a, b) -> Float.compare(a.chance, b.chance));

        // Make sure the chances add up to 100
        float chances = 0f;
        for (LootTable loot : lootTables) {
            chances += loot.chance;
        }
        if (chances != 1f) {
            throw new IllegalArgumentException("Loot Table chances must add up to 100");
        }

        // Choose loot
        float random = new Random().nextFloat();
        float cumulativeChance = 0f;
        for (LootTable loot : lootTables) {
            cumulativeChance += loot.chance;
            if (random <= cumulativeChance) {
                return loot.loot;
            }
        }

        // Warn and return null
        System.err.println("Warning: Loot not chosen");
        System.err.println("Random: " + random);
        return null;
     }
     public static ArrayList<Entity> chooseMultipleLoot(ArrayList<LootTable> lootTables) {
        // Sort
        lootTables.sort((a, b) -> Float.compare(a.chance, b.chance));

         // Choose loot
         ArrayList<Entity> selectedLoot = new ArrayList<>();
         Random randomGenerator = new Random();

         for (LootTable loot : lootTables) {
             float random = randomGenerator.nextFloat();
             if (random <= loot.chance) {
                 selectedLoot.add(loot.loot);
             }
         }

         return selectedLoot;
     }
}
