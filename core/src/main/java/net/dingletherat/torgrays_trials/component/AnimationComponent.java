package net.dingletherat.torgrays_trials.component;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.main.AnimationReader;
import net.dingletherat.torgrays_trials.main.AnimationReader.Animation;
import net.dingletherat.torgrays_trials.main.AnimationReader.RawAnimation;
import net.dingletherat.torgrays_trials.system.AnimationSystem;

public class AnimationComponent implements Component {
    /** The animation data with uninitialized dependencies. It's initialized in the constructor **/
    public RawAnimation rawAnimation;
    /** The animation data with initialized dependencies. It's initialized in {@link AnimationSystem} **/
    public Animation animation;
    /** The entry index of the component referenced in {@link #selfPath} that's initialized in the constructor.
     * In the alternate constructor, it is automatically set to 0.
     * It's declared in the constructor and used to initialize the self for {@link #animation} in {@link AnimationSystem}
     **/

    public JSONObject dependencyIndices;
    public float counter;
    public Map<Integer, Integer> frames = new HashMap<>();

    public AnimationComponent(String animationName, JSONObject dependencyIndices) {
        if (!AnimationReader.ANIMATIONS.containsKey(animationName)) {
            Main.LOGGER.warn("Couldn't find animation \"{}\"", animationName);
            return;
        }

        rawAnimation = AnimationReader.ANIMATIONS.get(animationName);
        this.dependencyIndices = dependencyIndices;
    }

    public AnimationComponent(String animationName) {
        this(animationName, new JSONObject());
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MULTI;
    }
}
