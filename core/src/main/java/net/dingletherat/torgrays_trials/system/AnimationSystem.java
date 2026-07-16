package net.dingletherat.torgrays_trials.system;

import java.util.ArrayList;
import java.util.List;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.AnimationComponent;
import net.dingletherat.torgrays_trials.main.AnimationReader;
import net.dingletherat.torgrays_trials.main.EntityHandler;
import net.dingletherat.torgrays_trials.main.UtilityTool;
import net.dingletherat.torgrays_trials.main.World;
import net.dingletherat.torgrays_trials.main.AnimationReader.Animation;
import net.dingletherat.torgrays_trials.main.AnimationReader.DependencyField;

public class AnimationSystem implements System {
    public void draw(World world) { }
    public void update(World world, float deltaTime) {
        for (Integer entity : EntityHandler.queryAll(AnimationComponent.class)) {
            for (AnimationComponent component : EntityHandler.getComponents(entity, AnimationComponent.class)) {
                // If the animation for it hasn't yet been initialized, do that (as long as there is a raw animation)
                if (component.animation == null && component.rawAnimation != null) {
                    // Create de animation. If it returns null, something likely went wrong, so remove the component and continue. If nothing goes wrong, set it
                    Animation animation = AnimationReader.initializeAnimation(entity, component.rawAnimation, component.dependencyIndices);
                    if (animation == null) {
                        Main.gameWorld.removeComponent(entity, component);
                        continue;
                    }
                    component.animation = animation;
                }

                // Increment the counter until the speed goal is met. Then, reset it
                component.counter += deltaTime;
                if (!(component.counter >= component.animation.speed())) continue;
                component.counter = 0f;

                /* Loop through all the conditions and look for a valid one. If a valid one is found, add it into passedConditions. It will then be used to get its frames
                   We get the conditions instead of the frames directly cuz it's used for a warning */
                List<DependencyField> conditionList = new ArrayList<>(component.animation.frames().keySet());
                List<DependencyField> passedConditions = new ArrayList<>();
                for (int index = 0; index < conditionList.size(); index++) {
                    DependencyField condition = conditionList.get(index);

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

                    // Convert the expectedValue to whatever the field's type actually is to make it checkable
                    Object expectedValue = UtilityTool.convertToType(condition.value(), condition.field().getType());

                    // If the expectation has the "~" symbol, meaning contains, it can only be verified if it's a string, so make sure the expectation is a string
                    if (condition.expectation().contains("~") && !(value instanceof String)) {
                        Main.LOGGER.warn("Condition #{} skipped: field {} in dependency {} must be a string to use the \"~\" operator, but it's a {}", index, condition.field().getName(), condition.dependency().getClass().getSimpleName(), condition.value().getClass().getSimpleName());
                        continue;
                    }

                    // If the expectation has a ">" or a "<" symbol, meaning bigger or smaller than, it can only be verified if it's a Comparable, so make sure the expectation is a Comparable
                    if ((condition.expectation().contains(">") || condition.expectation().contains("<")) && !(value instanceof Comparable)) {
                        Main.LOGGER.warn("Condition #{} skipped: field {} in dependency {} must be a Comparable (a number) to use the \">\" and \"<\" operator, but it's a {}", index, condition.field().getName(), condition.dependency().getClass().getSimpleName(), condition.value().getClass().getSimpleName());
                        continue;
                    }

                    // Declare a boolean that is true when the condition, depending on the expectation, is met
                    boolean met = switch (condition.expectation()) {
                        case "~" -> ((String) value).contains((String) expectedValue);
                        case "!~" -> !((String) value).contains((String) expectedValue);
                        case "!=" -> !value.equals(expectedValue);
                        case ">=" -> ((Comparable<Object>) value).compareTo(expectedValue) >= 0;
                        case ">" -> ((Comparable<Object>) value).compareTo(expectedValue) > 0;
                        case "<=" -> ((Comparable<Object>) value).compareTo(expectedValue) <= 0;
                        case "<" -> ((Comparable<Object>) value).compareTo(expectedValue) < 0;
                        default -> value.equals(expectedValue);
                    };

                    if (met) passedConditions.add(condition);
                }
                Main.LOGGER.debug("{}", passedConditions);

                // Loop through the passedConditions, getting the frames of each condition
                for (DependencyField condition : passedConditions) {
                    List<List<DependencyField>> frames = component.animation.frames().get(condition);

                    // Get the current frame number and get the current frame with it
                    int framesSize = frames.size() - 1; // The size is decreased by 1 to account for indexes starting at 0
                    int frameNumber = component.frames.getOrDefault(framesSize, 0);
                    List<DependencyField> frame = frames.get(frameNumber);

                    // Get the correct frame from the frames and loop through each of its variable changes, implementing them
                    for (DependencyField dependencyField : frame) {
                        try {
                            dependencyField.field().set(dependencyField.dependency(), UtilityTool.convertToType(dependencyField.value(), dependencyField.field().getType()));
                        } catch (IllegalAccessException exception) {
                           Main.LOGGER.warn("Removed field setter #{} from condition {}'s frames: unable to access field \"{}\" in dependency while trying to modify it", frame.indexOf(dependencyField), condition, dependencyField.field().getName());
                           component.animation.frames().get(condition).get(frameNumber).remove(dependencyField);
                        }
                    }

                    // Last and least, update the frame to the next one
                    frameNumber++;
                    component.frames.put(framesSize, frameNumber);
                    if (frameNumber > framesSize) component.frames.put(framesSize, 0);
                }
            }
        }
    }
}
