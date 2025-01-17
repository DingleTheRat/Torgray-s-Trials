package main;

import entity.NPC_GateKeeper;
import mob.MOB_Dracore;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp) {this.gp = gp;}

    public void setObject() {

    }
    public void setNPC() {
        gp.npc[0] = new NPC_GateKeeper(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;
    }
    public void setMonster() {
        int i = 0;

        // In grass thing
        gp.mob[i] = new MOB_Dracore(gp);
        gp.mob[i].worldX = gp.tileSize * 23;
        gp.mob[i].worldY = gp.tileSize * 36;
        i++;
        gp.mob[i] = new MOB_Dracore(gp);
        gp.mob[i].worldX = gp.tileSize * 23;
        gp.mob[i].worldY = gp.tileSize * 37;
        i++;
        // In path thing
        gp.mob[i] = new MOB_Dracore(gp);
        gp.mob[i].worldX = gp.tileSize * 35;
        gp.mob[i].worldY = gp.tileSize * 10;
        i++;
        gp.mob[i] = new MOB_Dracore(gp);
        gp.mob[i].worldX = gp.tileSize * 37;
        gp.mob[i].worldY = gp.tileSize * 8;
        i++;
        gp.mob[i] = new MOB_Dracore(gp);
        gp.mob[i].worldX = gp.tileSize * 39;
        gp.mob[i].worldY = gp.tileSize * 10;
        i++;
    }
}
