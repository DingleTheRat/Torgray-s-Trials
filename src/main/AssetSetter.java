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
        // In grass thing
        gp.mob[0] = new MOB_Dracore(gp);
        gp.mob[0].worldX = gp.tileSize * 23;
        gp.mob[0].worldY = gp.tileSize * 36;

        gp.mob[1] = new MOB_Dracore(gp);
        gp.mob[1].worldX = gp.tileSize * 23;
        gp.mob[1].worldY = gp.tileSize * 37;

        // In path thing
        gp.mob[2] = new MOB_Dracore(gp);
        gp.mob[2].worldX = gp.tileSize * 35;
        gp.mob[2].worldY = gp.tileSize * 10;

        gp.mob[3] = new MOB_Dracore(gp);
        gp.mob[3].worldX = gp.tileSize * 37;
        gp.mob[3].worldY = gp.tileSize * 8;

        gp.mob[4] = new MOB_Dracore(gp);
        gp.mob[4].worldX = gp.tileSize * 39;
        gp.mob[4].worldY = gp.tileSize * 10;
    }
}
