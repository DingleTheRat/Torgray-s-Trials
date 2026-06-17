package net.dingletherat.torgrays_trials.system;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.AnimationComponent;
import net.dingletherat.torgrays_trials.main.AnimationReader;
import net.dingletherat.torgrays_trials.main.EntityHandler;
import net.dingletherat.torgrays_trials.main.World;
import net.dingletherat.torgrays_trials.main.AnimationReader.Animation;

public class AnimationSystem implements System {
    public void draw(World world) { }
    public void update(World world, float deltaTime) {
        for (Integer entity : EntityHandler.queryAll(AnimationComponent.class)) {
            // Get the component (and return if it doesn't exist)
            AnimationComponent component = EntityHandler.getComponent(entity, AnimationComponent.class).orElse(null);
            if (component == null) continue;

            if (component.animation == null && component.rawAnimation != null) {
                Animation animation = AnimationReader.initializeAnimation(entity, component.rawAnimation, component.targetComponent, component.targetEntryIndex);
                if (animation == null) {
                    Main.gameWorld.removeComponent(entity, component);
                    return;
                }
                component.animation = animation;
            }
        }
    }
}
