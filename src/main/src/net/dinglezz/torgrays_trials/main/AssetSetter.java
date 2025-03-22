package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.npc.NPC_GateKeeper;
import net.dinglezz.torgrays_trials.mob.MOB_Dracore;
import net.dinglezz.torgrays_trials.object.*;

public class AssetSetter {
    Game game;
    private static int i = 0;

    public AssetSetter(Game game) {this.game = game;}

    public void setObject() {
        int i = 0;
        game.obj.put(i, new OBJ_Key(game));
        game.obj.get(i).worldX = 23 * game.tileSize;
        game.obj.get(i).worldY = 7 * game.tileSize;
        i++;
        game.obj.put(i, new OBJ_Key(game));
        game.obj.get(i).worldX = 23 * game.tileSize;
        game.obj.get(i).worldY = 40 * game.tileSize;
        i++;
        game.obj.put(i, new OBJ_Key(game));
        game.obj.get(i).worldX = 38 * game.tileSize;
        game.obj.get(i).worldY = 8 * game.tileSize;
        i++;

        // Gates
        game.obj.put(i, new OBJ_Gate(game));
        game.obj.get(i).worldX = 10 * game.tileSize;
        game.obj.get(i).worldY = 12 * game.tileSize;
        i++;
        game.obj.put(i, new OBJ_Gate(game));
        game.obj.get(i).worldX = 8 * game.tileSize;
        game.obj.get(i).worldY = 28 * game.tileSize;
        i++;
        game.obj.put(i, new OBJ_Gate(game));
        game.obj.get(i).worldX = 12 * game.tileSize;
        game.obj.get(i).worldY = 23 * game.tileSize;
        i++;

        // Chest
        game.obj.put(i, new OBJ_Chest(game, new OBJ_Sword_Amethyst(game), new OBJ_Shield_Amethyst(game)));
        game.obj.get(i).worldX = 10 * game.tileSize;
        game.obj.get(i).worldY = 10 * game.tileSize;
        i++;
    }
    public void setNPC() {
        game.npc.put(0, new NPC_GateKeeper(game));
        game.npc.get(0).worldX = game.tileSize * 21;
        game.npc.get(0).worldY = game.tileSize * 21;
    }
    public void setMonster() {
        int i = 0;

        // In grass thing
        game.mob.put(i, new MOB_Dracore(game));
        game.mob.get(i).worldX = game.tileSize * 23;
        game.mob.get(i).worldY = game.tileSize * 36;
        i++;
        game.mob.put(i, new MOB_Dracore(game));
        game.mob.get(i).worldX = game.tileSize * 23;
        game.mob.get(i).worldY = game.tileSize * 37;
        i++;
        game.mob.put(i, new MOB_Dracore(game));
        game.mob.get(i).worldX = game.tileSize * 23;
        game.mob.get(i).worldY = game.tileSize * 38;
        i++;

        // In path thing
        game.mob.put(i, new MOB_Dracore(game));
        game.mob.get(i).worldX = game.tileSize * 35;
        game.mob.get(i).worldY = game.tileSize * 10;
        i++;
        game.mob.put(i, new MOB_Dracore(game));
        game.mob.get(i).worldX = game.tileSize * 37;
        game.mob.get(i).worldY = game.tileSize * 8;
        i++;
        game.mob.put(i, new MOB_Dracore(game));
        game.mob.get(i).worldX = game.tileSize * 39;
        game.mob.get(i).worldY = game.tileSize * 10;
        i++;

        // In gate thing
        game.mob.put(i, new MOB_Dracore(game));
        game.mob.get(i).worldX = game.tileSize * 10;
        game.mob.get(i).worldY = game.tileSize * 29;
        i++;
        game.mob.put(i, new MOB_Dracore(game));
        game.mob.get(i).worldX = game.tileSize * 11;
        game.mob.get(i).worldY = game.tileSize * 29;
        i++;
        game.mob.put(i, new MOB_Dracore(game));
        game.mob.get(i).worldX = game.tileSize * 12;
        game.mob.get(i).worldY = game.tileSize * 29;
    }
}
