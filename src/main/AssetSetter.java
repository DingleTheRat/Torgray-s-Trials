package main;

import entity.NPC_GateKeeper;
import mob.MOB_Dracore;
import object.*;

public class AssetSetter {
    GamePanel gamePanel;
    private static int i = 0;

    public AssetSetter(GamePanel gamePanel) {this.gamePanel = gamePanel;}

    public void setObject() {
        int i = 0;
        gamePanel.obj.put(i, new OBJ_Key(gamePanel));
        gamePanel.obj.get(i).worldX = 23 * gamePanel.tileSize;
        gamePanel.obj.get(i).worldY = 7 * gamePanel.tileSize;
        i++;
        gamePanel.obj.put(i, new OBJ_Key(gamePanel));
        gamePanel.obj.get(i).worldX = 23 * gamePanel.tileSize;
        gamePanel.obj.get(i).worldY = 40 * gamePanel.tileSize;
        i++;
        gamePanel.obj.put(i, new OBJ_Key(gamePanel));
        gamePanel.obj.get(i).worldX = 38 * gamePanel.tileSize;
        gamePanel.obj.get(i).worldY = 8 * gamePanel.tileSize;
        i++;

        // Gates
        gamePanel.obj.put(i, new OBJ_Gate(gamePanel));
        gamePanel.obj.get(i).worldX = 10 * gamePanel.tileSize;
        gamePanel.obj.get(i).worldY = 12 * gamePanel.tileSize;
        i++;
        gamePanel.obj.put(i, new OBJ_Gate(gamePanel));
        gamePanel.obj.get(i).worldX = 8 * gamePanel.tileSize;
        gamePanel.obj.get(i).worldY = 28 * gamePanel.tileSize;
        i++;
        gamePanel.obj.put(i, new OBJ_Gate(gamePanel));
        gamePanel.obj.get(i).worldX = 12 * gamePanel.tileSize;
        gamePanel.obj.get(i).worldY = 23 * gamePanel.tileSize;
        i++;

        // Chest
        gamePanel.obj.put(i, new OBJ_Chest(gamePanel, new OBJ_Sword_Amethyst(gamePanel), new OBJ_Shield_Amethyst(gamePanel)));
        gamePanel.obj.get(i).worldX = 10 * gamePanel.tileSize;
        gamePanel.obj.get(i).worldY = 10 * gamePanel.tileSize;
        i++;
    }
    public void setNPC() {
        gamePanel.npc.put(0, new NPC_GateKeeper(gamePanel));
        gamePanel.npc.get(0).worldX = gamePanel.tileSize * 21;
        gamePanel.npc.get(0).worldY = gamePanel.tileSize * 21;
    }
    public void setMonster() {
        int i = 0;

        // In grass thing
        gamePanel.mob.put(i, new MOB_Dracore(gamePanel));
        gamePanel.mob.get(i).worldX = gamePanel.tileSize * 23;
        gamePanel.mob.get(i).worldY = gamePanel.tileSize * 36;
        i++;
        gamePanel.mob.put(i, new MOB_Dracore(gamePanel));
        gamePanel.mob.get(i).worldX = gamePanel.tileSize * 23;
        gamePanel.mob.get(i).worldY = gamePanel.tileSize * 37;
        i++;
        gamePanel.mob.put(i, new MOB_Dracore(gamePanel));
        gamePanel.mob.get(i).worldX = gamePanel.tileSize * 23;
        gamePanel.mob.get(i).worldY = gamePanel.tileSize * 38;
        i++;

        // In path thing
        gamePanel.mob.put(i, new MOB_Dracore(gamePanel));
        gamePanel.mob.get(i).worldX = gamePanel.tileSize * 35;
        gamePanel.mob.get(i).worldY = gamePanel.tileSize * 10;
        i++;
        gamePanel.mob.put(i, new MOB_Dracore(gamePanel));
        gamePanel.mob.get(i).worldX = gamePanel.tileSize * 37;
        gamePanel.mob.get(i).worldY = gamePanel.tileSize * 8;
        i++;
        gamePanel.mob.put(i, new MOB_Dracore(gamePanel));
        gamePanel.mob.get(i).worldX = gamePanel.tileSize * 39;
        gamePanel.mob.get(i).worldY = gamePanel.tileSize * 10;
        i++;

        // In gate thing
        gamePanel.mob.put(i, new MOB_Dracore(gamePanel));
        gamePanel.mob.get(i).worldX = gamePanel.tileSize * 10;
        gamePanel.mob.get(i).worldY = gamePanel.tileSize * 29;
        i++;
        gamePanel.mob.put(i, new MOB_Dracore(gamePanel));
        gamePanel.mob.get(i).worldX = gamePanel.tileSize * 11;
        gamePanel.mob.get(i).worldY = gamePanel.tileSize * 29;
        i++;
        gamePanel.mob.put(i, new MOB_Dracore(gamePanel));
        gamePanel.mob.get(i).worldX = gamePanel.tileSize * 12;
        gamePanel.mob.get(i).worldY = gamePanel.tileSize * 29;
    }
}
