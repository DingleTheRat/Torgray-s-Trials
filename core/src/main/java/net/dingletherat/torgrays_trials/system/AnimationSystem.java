package net.dingletherat.torgrays_trials.system;

import java.util.ArrayList;
import java.util.List;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.AnimationComponent;
import net.dingletherat.torgrays_trials.main.AnimationReader;
import net.dingletherat.torgrays_trials.main.EntityHandler;
import net.dingletherat.torgrays_trials.main.World;
import net.dingletherat.torgrays_trials.main.AnimationReader.Animation;
import net.dingletherat.torgrays_trials.main.AnimationReader.DependencyField;

public class AnimationSystem implements System {
    public void draw(World world) { }
    public void update(World world, float deltaTime) {
        for (Integer entity : EntityHandler.queryAll(AnimationComponent.class)) {
            // Get the component (and return if it doesn't exist)
            AnimationComponent component = EntityHandler.getComponent(entity, AnimationComponent.class).orElse(null);
            if (component == null) continue;

            // If the animation for it hasn't yet been initialized, do that (as long as there is a raw animation)
            if (component.animation == null && component.rawAnimation != null) {
                // Create de animation. If it returns null, something likely went wrong, so remove the component and continue. If nothing goes wrong, set it
                Animation animation = AnimationReader.initializeAnimation(entity, component.rawAnimation, component.selfPath, component.selfEntryIndex);
                if (animation == null) {
                    Main.gameWorld.removeComponent(entity, component);
                    continue;
                }
                component.animation = animation;
            }

            // Increment the counter until the speed goal is met
            component.counter++;
            if (!(component.counter >= component.animation.speed())) continue;

            /* Loop through all the conditions and look for a valid one. If a valid one is found, add it into passedConditions. It will then be used to get its frames
               We get the conditions instead of the frames directly cuz it's used for a warning */
            List<DependencyField> passedConditions = new ArrayList<>();
            for (int index = 0; index < component.animation.frames().size(); index++) {
                DependencyField condition = new ArrayList<>(component.animation.frames().keySet()).get(index);

                // If the condition is blank, meaning it always passes, make it, well, always pass
                if (condition.dependency() == null && condition.field() == null && condition.value() == null && condition.expectation() == null) {
                    passedConditions.add(condition);
                    continue;
                }

                // Get the field's value from the dependency. Warn and continue if that fails
                Object value;
                try {
                    value = condition.field().get(condition.dependency());
                } catch (IllegalAccessException exception) {
                    Main.LOGGER.warn("Condition #{} skipped: field {} is not accessible in dependency {}", index, condition.field().getName(), condition.dependency().getClass().getSimpleName());
                    continue;
                }

                // If an expectation has the "~" symbol, meaning contains, it can only be verified if it's a string, so make sure the expectation is a string
                if (condition.expectation().contains("~") && !(value instanceof String)) {
                    Main.LOGGER.warn("Condition #{} skipped: field {} in dependency {} must be a string to use the \"~\" operator, but it's a {}", index, condition.field().getName(), condition.dependency().getClass().getSimpleName(), condition.value().getClass().getSimpleName());
                    continue;
                }

                // Declare a boolean that is true when the condition, depending on the expectation, is met
                boolean met = switch (condition.expectation()) {
                    case "~" -> ((String) value).contains((String) condition.value());
                    case "!~" -> !((String) value).contains((String) condition.value());
                    case "!=" -> !value.equals(condition.value());
                    case ">=" -> ((Comparable<Object>) value).compareTo(condition.value()) >= 0;
                    case ">" -> ((Comparable<Object>) value).compareTo(condition.value()) > 0;
                    case "<=" -> ((Comparable<Object>) value).compareTo(condition.value()) <= 0;
                    case "<" -> ((Comparable<Object>) value).compareTo(condition.value()) < 0;
                    default -> value.equals(condition.value());
                };

                if (met) passedConditions.add(condition);
            }

            // Loop through the passedConditions, getting the frames of each condition
            for (DependencyField condition : passedConditions) {
                List<List<DependencyField>> frames = component.animation.frames().get(condition);

                // Get the current frame number and get the current frame with it
                int frameNumber = component.frames.getOrDefault(frames.size(), 0);
                List<DependencyField> frame = frames.get(frameNumber);
                Main.LOGGER.debug("Key {}, Value {}", frames.size(), frameNumber);

                // Get the correct frame from the frames and loop through each of its variable changes, implementing them
                for (DependencyField dependencyField : frame) {
                    try {
                        dependencyField.field().set(dependencyField.dependency(), dependencyField.value());
                    } catch (IllegalAccessException exception) {
                       Main.LOGGER.warn("Removed field setter #{} from condition {}'s frames: unable to access field \"{}\" in dependency while trying to modify it", frame.indexOf(dependencyField), condition, dependencyField.field().getName());
                       component.animation.frames().get(condition).get(frameNumber).remove(dependencyField);
                    }
                }

                // Last and least, update the frame to the next one
                component.frames.put(frame.size(), frameNumber++);
                if (frameNumber >= frames.size()) component.frames.put(frame.size(), 0);
            }
        }
    }
}
