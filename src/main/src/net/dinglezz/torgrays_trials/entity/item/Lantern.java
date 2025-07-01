package net.dinglezz.torgrays_trials.entity.item;

import net.dinglezz.torgrays_trials.tile.TilePoint;

public class Lantern extends Item {
    public Lantern(TilePoint tilePoint) {
        super("Lantern", tilePoint);
        tags.add(ItemTags.TAG_LIGHT);
        tags.add(ItemTags.TAG_NON_SELLABLE);
        lightRadius = 150;
        icon = registerEntitySprite("entity/item/lantern");
        currentImage = icon;
        description = "SHINE BRIGHTTTTTT TONIGHTTTTTT /lyr \nLight Radius: " + lightRadius;
    }
}
