package main;

import entity.Entity;
import entity.NPC_GateKeeper;
import mob.MOB_Dracore;
import object.*;

import java.util.HashMap;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {this.gp = gp;}

    public void setObject() {
        int i = 0;
        gp.obj.put(i, new OBJ_Key(gp));
        gp.obj.get(i).worldX = 23 * gp.tileSize;
        gp.obj.get(i).worldY = 7 * gp.tileSize;
        i++;
        gp.obj.put(i, new OBJ_Key(gp));
        gp.obj.get(i).worldX = 23 * gp.tileSize;
        gp.obj.get(i).worldY = 40 * gp.tileSize;
        i++;
        gp.obj.put(i, new OBJ_Key(gp));
        gp.obj.get(i).worldX = 38 * gp.tileSize;
        gp.obj.get(i).worldY = 8 * gp.tileSize;
        i++;

        // Weapons
        gp.obj.put(i, new OBJ_Shield_Amethyst(gp));
        gp.obj.get(i).worldX = gp.tileSize * 9;
        gp.obj.get(i).worldY = gp.tileSize * 10;
        i++;
        gp.obj.put(i, new OBJ_Sword_Amethyst(gp));
        gp.obj.get(i).worldX = gp.tileSize * 11;
        gp.obj.get(i).worldY = gp.tileSize * 10;
        i++;

        // Gates
        gp.obj.put(i, new OBJ_Gate(gp));
        gp.obj.get(i).worldX = 10 * gp.tileSize;
        gp.obj.get(i).worldY = 12 * gp.tileSize;
        i++;
        gp.obj.put(i, new OBJ_Gate(gp));
        gp.obj.get(i).worldX = 8 * gp.tileSize;
        gp.obj.get(i).worldY = 28 * gp.tileSize;
        i++;
        gp.obj.put(i, new OBJ_Gate(gp));
        gp.obj.get(i).worldX = 12 * gp.tileSize;
        gp.obj.get(i).worldY = 23 * gp.tileSize;
        i++;

        // Others
        gp.obj.put(i, new OBJ_Torgray_Soup(gp));
        gp.obj.get(i).worldX = 35 * gp.tileSize;
        gp.obj.get(i).worldY = 40 * gp.tileSize;
        i++;
        gp.obj.put(i, new OBJ_Lantern(gp));
        gp.obj.get(i).worldX = 21 * gp.tileSize;
        gp.obj.get(i).worldY = 19 * gp.tileSize;

    }
    public void setNPC() {
        gp.npc.put(0, new NPC_GateKeeper(gp));
        gp.npc.get(0).worldX = gp.tileSize * 21;
        gp.npc.get(0).worldY = gp.tileSize * 21;
    }
    public void setMonster() {
        int i = 0;

        // In grass thing
        gp.mob.put(i, new MOB_Dracore(gp));
        gp.mob.get(i).worldX = gp.tileSize * 23;
        gp.mob.get(i).worldY = gp.tileSize * 36;
        i++;
        gp.mob.put(i, new MOB_Dracore(gp));
        gp.mob.get(i).worldX = gp.tileSize * 23;
        gp.mob.get(i).worldY = gp.tileSize * 37;
        i++;
        gp.mob.put(i, new MOB_Dracore(gp));
        gp.mob.get(i).worldX = gp.tileSize * 23;
        gp.mob.get(i).worldY = gp.tileSize * 38;
        i++;

        // In path thing
        gp.mob.put(i, new MOB_Dracore(gp));
        gp.mob.get(i).worldX = gp.tileSize * 35;
        gp.mob.get(i).worldY = gp.tileSize * 10;
        i++;
        gp.mob.put(i, new MOB_Dracore(gp));
        gp.mob.get(i).worldX = gp.tileSize * 37;
        gp.mob.get(i).worldY = gp.tileSize * 8;
        i++;
        gp.mob.put(i, new MOB_Dracore(gp));
        gp.mob.get(i).worldX = gp.tileSize * 39;
        gp.mob.get(i).worldY = gp.tileSize * 10;
        i++;

        // In gate thing
        gp.mob.put(i, new MOB_Dracore(gp));
        gp.mob.get(i).worldX = gp.tileSize * 10;
        gp.mob.get(i).worldY = gp.tileSize * 29;
        i++;
        gp.mob.put(i, new MOB_Dracore(gp));
        gp.mob.get(i).worldX = gp.tileSize * 11;
        gp.mob.get(i).worldY = gp.tileSize * 29;
        i++;
        gp.mob.put(i, new MOB_Dracore(gp));
        gp.mob.get(i).worldX = gp.tileSize * 12;
        gp.mob.get(i).worldY = gp.tileSize * 29;
    }
}
