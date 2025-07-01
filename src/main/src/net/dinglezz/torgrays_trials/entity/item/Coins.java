package net.dinglezz.torgrays_trials.entity.item;

import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.Sound;
import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Coins extends Item {
    public Coins(TilePoint tilePoint, int amount) {
        super("Coins", tilePoint);
        this.amount = amount;

        maxStack = 2;
        tags.add(ItemTags.TAG_PICKUP_ONLY);
        tags.add(ItemTags.TAG_NON_SELLABLE);
        icon = registerEntitySprite("entity/item/coin");
        currentImage = icon;
        description = "Precious pieces of gold";
        price = 1;
    }
    public Coins(TilePoint tilePoint) {
        super("Coins", tilePoint);
        maxStack = 2;
        tags.add(ItemTags.TAG_PICKUP_ONLY);
        tags.add(ItemTags.TAG_NON_SELLABLE);
        icon = registerEntitySprite("entity/item/coin");
        currentImage = icon;
        description = "Precious pieces of gold";
        price = 1;
    }

    @Override
    public void onPlayerHit() {
        Main.game.player.coins += amount;
        Sound.playSFX("Coin");

        // Grammar check
        if (amount == 1) Main.game.ui.addMiniNotification("+" + amount + " Coin");
        else Main.game.ui.addMiniNotification("+" + amount + " Coins");

        // Remove it
        Main.game.items.get(Main.game.currentMap).remove(this);
    }
}
