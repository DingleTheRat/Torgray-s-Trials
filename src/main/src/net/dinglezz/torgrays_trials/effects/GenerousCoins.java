package net.dinglezz.torgrays_trials.effects;

import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.entity.monster.Monster;
import net.dinglezz.torgrays_trials.main.Main;

import java.util.HashMap;

public class GenerousCoins extends Effect {
    public GenerousCoins(int duration, Mob host) {
        super("Generous Coins", duration, host);
        registerEffectImage("entity/item/coin");
    }

    @Override
    public void onApply() {}

    @Override
    public void during() {}

    @Override
    public void onEnd() {}
}
