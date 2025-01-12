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
        gp.mob[0] = new MOB_Dracore(gp);
        gp.mob[0].worldX = gp.tileSize * 23;
        gp.mob[0].worldY = gp.tileSize * 36;

        gp.mob[1] = new MOB_Dracore(gp);
        gp.mob[1].worldX = gp.tileSize * 23;
        gp.mob[1].worldY = gp.tileSize * 37;
    }
}
