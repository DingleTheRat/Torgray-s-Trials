package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.npc.NPC_Coiner;
import net.dinglezz.torgrays_trials.npc.NPC_GateKeeper;
import net.dinglezz.torgrays_trials.monster.MON_Dracore;
import net.dinglezz.torgrays_trials.object.*;
import net.dinglezz.torgrays_trials.object.shield.OBJ_Shield_Amethyst;
import net.dinglezz.torgrays_trials.object.weapon.OBJ_Sword_Amethyst;

import java.util.HashMap;

public class AssetSetter {
    Game game;
    public int i = 0;
    public HashMap<Integer, Entity> assetMap = new HashMap<>();

    public AssetSetter(Game game) {this.game = game;}

    public void setObjects() {
        // Disabled Map
            assetMap.put(0, null);
            game.object.put("Disabled", assetMap);
            assetMap = new HashMap<>();

        // Main Island Map
            // Keys
            setAsset(new OBJ_Key(game), 23, 40);
            setAsset(new OBJ_Key(game), 38, 8);

            // Gates
            setAsset(new OBJ_Gate(game), 10, 12);
            setAsset(new OBJ_Gate(game), 14, 28);
            setAsset(new OBJ_Gate(game), 8, 28);
            setAsset(new OBJ_Gate(game), 12, 23);

            // Other
            setAsset(new OBJ_Chest(game, new OBJ_Sword_Amethyst(game), new OBJ_Shield_Amethyst(game)), 10, 10);

            game.object.put("Main Island", assetMap);
            assetMap = new HashMap<>();
            i = 0;

        // Coiner's Shop Map
            setAsset(new OBJ_Table(game), 12, 10);

            game.object.put("Coiner's Shop", assetMap);
            assetMap = new HashMap<>();
            i = 0;
    }
    public void setNPCs() {
        // Disabled Map
            assetMap.put(0, null);
            game.npc.put("Disabled", assetMap);
            assetMap = new HashMap<>();

        // Main Island Map
            setAsset(new NPC_GateKeeper(game), 21, 21);

            game.npc.put("Main Island", assetMap);
            assetMap = new HashMap<>();
            i = 0;

        // Coiner's Shop Map
            setAsset(new NPC_Coiner(game), 12, 9);

            game.npc.put("Coiner's Shop", assetMap);
            assetMap = new HashMap<>();
            i = 0;
    }
    public void setMonsters() {
        // Disabled Map
            assetMap.put(0, null);
            game.monster.put("Disabled", assetMap);
            assetMap = new HashMap<>();

        // Main Island Map
            // In grass thing
            setAsset(new MON_Dracore(game), 23, 36);
            setAsset(new MON_Dracore(game), 23, 37);
            setAsset(new MON_Dracore(game), 23, 38);

            // In path thing
            setAsset(new MON_Dracore(game), 35, 10);
            setAsset(new MON_Dracore(game), 37, 8);
            setAsset(new MON_Dracore(game), 39, 10);

            // In gate thing
            setAsset(new MON_Dracore(game), 10, 29);
            setAsset(new MON_Dracore(game), 11, 29);

            setAsset(new MON_Dracore(game), 12, 29);

            game.monster.put("Main Island", assetMap);
            assetMap = new HashMap<>();
            i = 0;

        // Coiner's Shop Map
            assetMap.put(0, null);
            game.monster.put("Coiner's Shop", assetMap);
            assetMap = new HashMap<>();
            i = 0;
    }

    public void setAsset(Entity asset, int col, int row) {
        assetMap.put(i, asset);
        assetMap.get(i).worldX = game.tileSize * col;
        assetMap.get(i).worldY = game.tileSize * row;
        i++;
    }
}
