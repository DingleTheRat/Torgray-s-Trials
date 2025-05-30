package net.dinglezz.torgrays_trials.npc;

import net.dinglezz.torgrays_trials.entity.Entity;
import net.dinglezz.torgrays_trials.entity.EntityTypes;
import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.object.Key;
import net.dinglezz.torgrays_trials.object.Map;
import net.dinglezz.torgrays_trials.object.Torgray_Soup;
import net.dinglezz.torgrays_trials.object.weapon.Stick;

public class Coiner extends Entity {
    Game game;

    public Coiner(Game game) {
        super(game);
        this.game = game;
        type = EntityTypes.TYPE_NPC;
        direction = "down";
        speed = 1;
        spriteSpeed = 80;

        // Solid Area
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

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
        dialogues.addFirst("Hey there partner, I'm Coiner! \nWanna buy or sell something? :D");
    }
    public void setItem() {
        inventory.add(new Torgray_Soup(game));
        inventory.add(new Key(game));
        inventory.add(new Stick(game));
        inventory.add(new Map(game));
    }
    @Override
    public void speak(boolean facePlayer) {
        super.speak(false);
        game.ui.uiState = States.UIStates.TRADE;
        game.ui.subUIState = "Select";
        game.ui.commandNumber = 0;
        game.ui.npc = this;
    }
}
