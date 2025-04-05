package net.dinglezz.torgrays_trials.npc;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.object.OBJ_Key;
import net.dinglezz.torgrays_trials.object.OBJ_Map;
import net.dinglezz.torgrays_trials.object.OBJ_Stick;
import net.dinglezz.torgrays_trials.object.OBJ_Torgray_Soup;

public class NPC_Coiner extends Entity {
    Game game;

    public NPC_Coiner(Game game) {
        super(game);
        this.game = game;
        type = EntityTypes.TYPE_NPC;
        direction = "down";
        speed = 1;
        spriteSpeed = 80;

        getImage();
        setDialogue();
        setItem();
    }

    public void getImage() {
        down1 = registerEntitySprite("/npc/coiner/coiner_1");
        down2 = registerEntitySprite("/npc/coiner/coiner_2");
        down3 = registerEntitySprite("/npc/coiner/coiner_3");
    }
    public void setDialogue() {
        dialogues.put(0, "Hey there partner, I'm Coiner! /nWanna Trade? :D");
    }
    public void setItem() {
        inventory.add(new OBJ_Torgray_Soup(game));
        inventory.add(new OBJ_Key(game));
        inventory.add(new OBJ_Stick(game));
        inventory.add(new OBJ_Map(game));
    }
    @Override
    public void speak(boolean facePlayer) {
        super.speak(false);
        game.gameState = States.STATE_TRADE;
        game.ui.npc = this;
        game.ui.subState = States.TRADE_STATE_SELECT;
    }
}
