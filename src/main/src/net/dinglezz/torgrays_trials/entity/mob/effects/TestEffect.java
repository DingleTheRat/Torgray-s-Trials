package net.dinglezz.torgrays_trials.entity.mob.effects;

import net.dinglezz.torgrays_trials.entity.mob.Mob;

public class TestEffect extends Effect {
    public TestEffect(int duration, Mob host) {
        super("Test Effect", duration, host);
        registerEffectImage("entity/item/soup/torgray_soup");
    }

    @Override
    public void onApply() {
        host.speed = 100;
    }

    @Override
    public void during() {
        host.damage(1);
    }

    @Override
    public void onEnd() {
        host.speed = host.defaultSpeed;
    }
}
