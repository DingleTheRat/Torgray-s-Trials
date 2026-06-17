package net.dingletherat.torgrays_trials.component;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.main.AnimationReader;
import net.dingletherat.torgrays_trials.main.AnimationReader.Animation;
import net.dingletherat.torgrays_trials.main.AnimationReader.RawAnimation;

public class AnimationComponent implements Component {
    public RawAnimation rawAnimation;
    public Animation animation;
    public int targetEntryIndex;
    public String targetComponent;

    public AnimationComponent(String animationName, String targetComponent, Integer targetEntryIndex) {
        if (!AnimationReader.ANIMATIONS.containsKey(animationName)) {
            Main.LOGGER.warn("Couldn't find animation \"{}\"", animationName);
            return;
        }

        rawAnimation = AnimationReader.ANIMATIONS.get(animationName);
        this.targetComponent = targetComponent;
        this.targetEntryIndex = targetEntryIndex;
    }
    public AnimationComponent(String animationName, String targetComponent) {
        this(animationName, targetComponent, 0);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SINGLE;
    }
}
