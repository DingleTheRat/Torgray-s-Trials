package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.object.OBJ_Coins;
import net.dinglezz.torgrays_trials.object.OBJ_Torgray_Soup;

import java.util.ArrayList;
import java.util.Random;

public class LootTable {
    // Non-static stuff
    public Entity loot;
    public float chance;

    // LootTables
    public static final LootTable DRACORE_RANDOM_COIN = new LootTable(new OBJ_Coins(Main.game, 0), 0.50f);

    public static final ArrayList<LootTable> LOOT_TABLE_DRACORE_COINS = new ArrayList<>() {{
        add(new LootTable(new OBJ_Coins(Main.game, 1), 0.5f));
        add(new LootTable(new OBJ_Coins(Main.game, 2), 0.35f));
        add(new LootTable(new OBJ_Coins(Main.game, 3), 0.15f));
    }};
    public static final ArrayList<LootTable> LOOT_TABLE_DRACORE = new ArrayList<>() {{
        add(new LootTable(null, 0.25f)); // NOTHING!
        add(new LootTable(new OBJ_Torgray_Soup(Main.game), 0.25f));
        add(DRACORE_RANDOM_COIN);
    }};

    public LootTable(Entity loot, float chance) {
        this.loot = loot;
        this.chance = chance;
    }

    public static Entity chooseSingleLoot(ArrayList<LootTable> lootTables) {
        // Make sure the chances add up to 100
        float chances = 0f;
        for (LootTable loot : lootTables) {
            chances += loot.chance;
        }
        if (chances != 1f) {
            throw new IllegalArgumentException("Loot Table chances must add up to 100");
        }

        float random = new Random().nextFloat();
        float cumulativeChance = 0f;
        for (LootTable loot : lootTables) {
            cumulativeChance += loot.chance;
            if (random <= cumulativeChance) {
                return loot.loot;
            }
        }

        System.err.println("Warning: Loot not chosen");
        System.err.println("Random: " + random);
        return null;
     }
}
