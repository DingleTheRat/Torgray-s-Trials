package net.dingletherat.torgrays_trials.main;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.AnimationComponent;
import net.dingletherat.torgrays_trials.component.Component;
import net.dingletherat.torgrays_trials.component.MovementComponent;
import net.dingletherat.torgrays_trials.component.NameComponent;

public class AnimationReader {
    public static final Map<String, RawAnimation> ANIMATIONS = new HashMap<>();

    // Keys for stuff (yay!)
    /** The key used to get the name of the animation, which will be used in errors and as a key in the {@code ANIMATIONS} map**/
    public static final String KEY_NAME = "name";
    /** The key used to get a list of components whose fields an animation JSON will modify (optional) **/
    public static final String KEY_DEPENDENCIES = "dependencies";
    /** An optional key that sets what the target component (set by the {@link AnimationComponent}) needs to be for this animation to function correctly.
    If the target component doesn't match the self you set, then the {@link AnimationComponent} will be discarded with a warning **/
    public static final String KEY_SELF = "self";
    /** The key used to get the speed at which the animation will run at **/
    public static final String KEY_SPEED = "speed";
    /** The key used to obtain the frames  **/
    public static final String KEY_FRAMES = "frames";
    /** The path used to get animations **/
    public static final String PATH = "values/animations/";

    // Records
    /** Animation files, when all goes according to plan, will be parsed into this record.
     * It holds all the data that the JSON contains, just in its proper form (EX: Class paths as classes).
     * However, none of the classes are actually initialized, so it's called "RawAnimation", not animation
     * <p>
     * @param dependencies A list of paths to SELF components whose fields you wanna modify in frames.
     *      For instance, if I add the path to a {@link SpriteComponent}, I may modify one of its fields in a frame
     *      by putting its position in the list starting from 0 (since its the only one I'll use 0) and the field I wanna modify after a ":".
     *      So I would make it look like so: {@code "[LIST_POSITION]:[FIELD]": [VALUE]} or in my case {@code "0:sprite": "entity/player/torgray_sheet"}
     * @param self Whenever a {@link AnimationComponent} calls an animation, its target parameter gets passed as "self" and can be used as a dependency with "self" instead of a number.
     *      However, self is very unpredictable, so you can also add a self field (this) that specifies what the target component (self) should be.
     *      If it is not that, the animation component will be removed with a warning. Self is also really the only way animations can access multi-components,
     *      such as Sprites, so use it wisely.
     * @param speed The speed at which each frame of the animation is running in delta-time, as a float.
     * @param frames This is basically the actual animation. The first layer is the map of {@link RawDependencyField}s to frames. If you don't want a condition, just leave an empty string.
     *      If you do want one, put it in this format: {@code [DEPENDENCY]:[FIELD]:[=/>/<][VALUE]}. If it passes the condition, it will play the frames that you put
     *      in the condition. For instance, if I wanted an animation to only play if my entity is idle (uses {@link MovementComponent}), I would do {@code "0:state:=IDLE": [animation...]}.
     *      The condition must lead into another array, an array of {@link JSONObject} in the JSON, these are the frames. In code, this would be a list of lists of {@link RawDependencyField}s.
     *      Every entry in the list is a frame inside the animation that will play every few seconds (determined by the {@link #speed}) while the condition is met. In each frame, you may change the properties of any dependency, these are called fieldSetters in code.
     *      (see parameter {@link #dependencies} for more info on the format). This is a list, so you're able to change multiple properties at once if you want.
     **/
    public record RawAnimation(List<Class<? extends Component>> dependencies, Class<? extends Component> self,
            float speed, Map<RawDependencyField, List<List<RawDependencyField>>> frames) { }

    /**
     * When an {@link AnimationComponent} is declared, its entity's components and the will be used to initialize the dependencies of an animation and store it in here via {@link }.
     **/
    public record Animation(List<Component> dependencies, Component self,
            float speed, Map<DependencyField, List<List<DependencyField>>> frames) { }

    /** A helper record to the {@link RawAnimation} record, which stores data related to fields, but with the dependency uninitialized and the field's name as a string.
     * It's mainly used to store conditions and fieldSetters, with the {@link #expectation} parameter being exclusively for conditions.
     * If you wanna learn more, about how it's used, checked out the {@link #frames} in the {@link RawAnimation} record.
     * <p>
     * @param dependency The component class in which the {@link #field} SHOULD be located
     * @param field The name of the field that's in the {@link #dependency}
     * @param value This is either going to be the new value of the field (when used as a fieldSetter) or the expected value of the field (when used as a condition), depending on the use case of this record.
     **/
    public record RawDependencyField(Class<? extends Component> dependency, String field, Object value, String expectation) { }

    public record DependencyField(Component dependency, Field field, Object expectedValue, String expectation) { }

    /**
     * Calls {@code loadRawAnimations} for every file in the {@code PATH}, as long as it's a JSON.
     * All {@link RawAnimation}s created by the {@code loadRawAnimations} is added into the {@code ANIMATIONS} {@code List} to be used by other methods.
     * This method is recommended to be called upon world creation or at the game's start.
     * If you wanna know more, look at {@code loadRawAnimations}'s documentation.
     * <p>
     * Note: The {@code ANIMATIONS} list is cleared at the start of the method.
     **/
    public static void loadRawAnimations() {
        ANIMATIONS.clear();

        // Get the fileNames of everything inside the directory from the filepath
        List<String> fileNames = UtilityTool.getDecendantFileNames(PATH, ".json");

        // Append the filepath onto the fileNames
        fileNames.replaceAll(name -> PATH + name);

        // Create a new jsons list that contains a list of all the fileNames in JSONObject form
        List<JSONObject> jsons = new ArrayList<>(fileNames.stream()
            .map(UtilityTool::getJsonObject)
            .toList());

        // Get rid of the json objects that returned null
        jsons.removeIf(Objects::isNull);

        // Loop through the rest of the stuff
        for (JSONObject json : jsons) {
            // Get the raw animation via the loadRawAnimation method, making sure NULL SHALL NOT PASS
            RawAnimation rawAnimation = loadRawAnimation(json, fileNames.get(jsons.indexOf(json)));
            if (rawAnimation == null) continue;

            // Add the rawAnimation in :3
            ANIMATIONS.put(json.getString("name"), rawAnimation);
        }

        Main.LOGGER.info("Loaded {} animations", ANIMATIONS.size());
    }

    public static RawAnimation loadRawAnimation(JSONObject json, String fileName) {
        // Check if the json has the necessary stuff. If not, warn and return null
        if (!json.has(KEY_NAME) || !(json.get(KEY_NAME) instanceof String name)) {
            Main.LOGGER.warn("Invalid animation \"{}\": \"{}\" field is missing or is not a String.", fileName, KEY_NAME);
            return null;
        }
        if (!json.has(KEY_SPEED) || !(json.get(KEY_SPEED) instanceof BigDecimal speed)) {
            Main.LOGGER.warn("Invalid animation \"{}\": \"{}\" field is missing or is not a BigDecimal.", name, KEY_SPEED);
            return null;
        }
        if (!json.has(KEY_FRAMES) || !(json.get(KEY_FRAMES) instanceof JSONObject framesObject)) {
            Main.LOGGER.warn("Invalid animation \"{}\": \"{}\" field is missing or is not a JSONObject.", name, KEY_FRAMES);
            return null;
        }

        // Convert the dependencies to a list from a JSONArray, then loop through them converting them from path to a proper class
        List<Class<? extends Component>> dependencies = new ArrayList<>();
        if (json.has(KEY_DEPENDENCIES) && json.get(KEY_DEPENDENCIES) instanceof JSONArray dependenciesArray) {
            // Convert it to a list
            List<String> dependencyPaths = new ArrayList<>(IntStream.range(0, dependenciesArray.length())
                            .mapToObj(dependenciesArray::getString)
                            .toList());

            // Loop through it converting it to components
            for (String dependencyPath : dependencyPaths) {
                Class<? extends Component> dependency = UtilityTool.getClassFromPath(dependencyPath, Component.class, name + " animation file");
                if (dependency != null) dependencies.add(dependency);
            }
        }

        // Do the same for the self, just don't do all the weird looping shenanigans
        Class<? extends Component> self = null;
        if (json.has(KEY_SELF) && json.get(KEY_SELF) instanceof String selfPath)
            self = UtilityTool.getClassFromPath(selfPath, Component.class, name + " animation file");

        // Now, onto THE BIG ONE, the animations
        Map<RawDependencyField, List<List<RawDependencyField>>> frames = new HashMap<>();

        // Since we're dealing with a JSON object instead of the list, we'll loop through its keySet and get the array with the key
        for (String rawCondition : framesObject.keySet()) {
            JSONArray conditionFramesArray = framesObject.getJSONArray(rawCondition);

            // As usual with JSONArrays, turn the conditionFrames into a list
            List<JSONObject> rawConditionFrames = new ArrayList<>(IntStream.range(0, conditionFramesArray.length())
                            .mapToObj(conditionFramesArray::getJSONObject)
                            .toList());

            // Then, once again loop through it, putting all the data obtained in the list below
            List<List<RawDependencyField>> conditionFrames = new ArrayList<>();
            for (JSONObject rawFrame : rawConditionFrames) {

                // All done here is just converting the dependencyIndex to a dependency class and get the newValue, putting both as well as the field portion of the targetField into the RawDependencyField record
                List<RawDependencyField> frame = new ArrayList<>();
                for (String targetField : rawFrame.keySet()) {
                    // Get the new value from the JSONObject by using the targetField as a key (like last time)
                    Object newValue = rawFrame.get(targetField);

                    // Get the dependency from the targetField as well, adding both the newValue, the field portion of targetField, and the obtained dependency to a RawDependencyField, adding that to the list
                    Class<? extends Component> targetDependency = getDependencyFromString("TargetField \"" + targetField + "\" in " + name, dependencies, self, targetField);
                    frame.add(new RawDependencyField(targetDependency, targetField.split(":")[1], newValue, ""));
                }

                conditionFrames.add(frame);
            }

            // Split the rawCondition into (what's supposed to be) 3 strings: the dependency, variable, and condition.
            // Get the dependency class with the first one, and use the second two in the condition declaration
            String[] splitCondition = rawCondition.split(":");
            Class<? extends Component> conditionDependency = getDependencyFromString("Condition \"" + rawCondition + "\" in " + name, dependencies, self, rawCondition);
            RawDependencyField condition = new RawDependencyField(conditionDependency, splitCondition[1], splitCondition[3], splitCondition[2]);

            frames.put(condition, conditionFrames);
        }

        // TODO: Remove
        Main.LOGGER.debug("{}", frames);

        // Create the animation and return it
        RawAnimation animation = new RawAnimation(dependencies, self, speed.floatValue(), frames);
        return animation;
    }

    public static Animation initializeAnimation(int entity, RawAnimation rawAnimation, String selfPath, int entryIndex) {
        // Get the name for warning purposes
        String name = EntityHandler.getComponent(entity, NameComponent.class).get().name;
        String animationName = ANIMATIONS.entrySet().stream()
            .filter(entry -> entry.getValue().equals(rawAnimation)).map(Map.Entry::getKey).findFirst().orElse("unknown");

        /*
         * The reason we don't use the self from the rawAnimation here is because the rawAnimation self is the one expected, not provided by the component.
         * Even though we could just declare the expected one, it's optional, meaning if it's not provided we can't get a self.
         * With the self from rawAnimation, we also can't get a self with an entry index, which is its whole point.
         */
        // Get the raw self via the get class from path the method
        Class<? extends Component> rawSelf = UtilityTool.getClassFromPath(selfPath, Component.class, "AnimationComponent self declaration");
        if (rawSelf == null) return null;

        // Make sure the raw self matches up with the rawAnimation's expected self, if it has one
        if (rawAnimation.self() != null && rawAnimation.self() != rawSelf) {
            Main.LOGGER.warn("Self mismatch in entity {}: animation {} expects \"{}\", but got \"{}\"", name, animationName, rawAnimation.self().getSimpleName(), rawSelf.getSimpleName());
            return null;
        }

        // Using EntityHandler.getComponent, get the dependency component from the entity. If it's not present, warn and return null.
        Component self = EntityHandler.getComponent(entity, rawSelf, entryIndex).orElse(null);
        if (self == null) {
           Main.LOGGER.warn("Failed to find self for {}'s animation component: entity expects {}, but it wasn't found", entity, animationName, rawSelf.getSimpleName());
           return null;
        }

        // It returns an optional, and if the component is present, add it in. Warn if any were skipped.
        List<Component> dependencies = new ArrayList<>();
        rawAnimation.dependencies().stream()
           .map(dependency -> EntityHandler.getComponent(entity, dependency))
           .flatMap(Optional::stream) // FYI Flat-map skips over nulls
           .forEach(dependencies::add);

        // Warn if something was skipped
        rawAnimation.dependencies().stream()
           .filter(dependency -> EntityHandler.getComponent(entity, dependency).isEmpty())
           .forEach(dependency -> Main.LOGGER.warn("Dependency \"{}\" in animation \"{}\" was skipped: not found in entity {}", dependency.getSimpleName(), animationName, entity));

        // Do the same for conditions, but also keep track of the raw condition as a key so we can pair them with frames later
        Map<RawDependencyField, DependencyField> resolvedConditions = new LinkedHashMap<>();
        AtomicInteger index = new AtomicInteger();
        rawAnimation.frames().keySet().stream()
           .flatMap(rawCondition -> EntityHandler.getComponent(entity, rawCondition.dependency())
               .map(dependency -> {
                   // The field from the RawDependencyField must be converted into an actual field, so do that as well
                   int i = index.getAndIncrement(); // This is for the warning below
                   try {
                        Field field = rawCondition.dependency().getDeclaredField(rawCondition.field());
                        return new DependencyField(dependency, field, rawCondition.value(), rawCondition.expectation());
                   } catch (NoSuchFieldException exception) {
                       Main.LOGGER.warn("Condition #{} was skipped in animation {}: field {} not found in dependency {}", i, animationName, rawCondition.field(), rawCondition.dependency().getSimpleName());
                   }
                   return null;
               })
               .filter(Objects::nonNull)
               .map(condition -> Map.entry(rawCondition, condition))
               .stream())
           .forEach(entry -> resolvedConditions.put(entry.getKey(), entry.getValue()));

        // More warnings
        rawAnimation.frames().keySet().stream()
           .filter(rawCondition -> !resolvedConditions.containsKey(rawCondition))
           .forEach(rawCondition -> Main.LOGGER.warn("Condition \"{}:{}:{}\" in animation \"{}\" was skipped: dependency \"{}\" not found in entity {}",
               rawCondition.dependency().getSimpleName(), rawCondition.field(), rawCondition.value(), animationName, rawCondition.dependency().getSimpleName(), entity));

        // Pair up conditions with their frames, digging into the raw animation to get the uninitialized frames and resolving the dependencies within
        Map<DependencyField, List<List<DependencyField>>> frames = new HashMap<>();
        resolvedConditions.forEach((rawCondition, condition) -> {
           List<List<DependencyField>> frameList = rawAnimation.frames().get(rawCondition).stream()
               .map(frame -> frame.stream()
                   .flatMap(fieldSetter -> EntityHandler.getComponent(entity, fieldSetter.dependency())
                       .map(dependency -> {
                           // The field from the RawDependencyField must be converted into an actual field, so do that as well
                           int i = index.getAndIncrement(); // This is for the warning below
                           try {
                               // TODO: Check supers as well
                                Field field = fieldSetter.dependency().getDeclaredField(fieldSetter.field());
                                return new DependencyField(dependency, field, fieldSetter.value(), fieldSetter.expectation());
                           } catch (NoSuchFieldException exception) {
                               Main.LOGGER.warn("Field setter #{} was skipped in animation {}: field {} not found in dependency {}", i, animationName, fieldSetter.field(), fieldSetter.dependency().getSimpleName());
                           }
                           return null;
                       })
                       .filter(Objects::nonNull)
                       .stream())
                   .collect(Collectors.toList()))
               .collect(Collectors.toList());
           frames.put(condition, frameList);
        });

        // EVEN MORE SKIP WARNINGS
        resolvedConditions.forEach((rawCondition, condition) ->
           rawAnimation.frames().get(rawCondition).stream()
                   .flatMap(frame -> frame.stream())
                   .filter(fieldSetter -> EntityHandler.getComponent(entity, fieldSetter.dependency()).isEmpty())
                   .forEach(fieldSetter -> Main.LOGGER.warn("Frame dependency \"{}\" in condition \"{}:{}:{}\" of animation \"{}\" was skipped: not found in entity {}",
                       fieldSetter.dependency().getSimpleName(), rawCondition.dependency().getSimpleName(), rawCondition.field(), rawCondition.value(), animationName, entity)));

        // Use all that to create our animation!
        Animation animation = new Animation(dependencies, self, rawAnimation.speed(), frames);
        return animation;
    }

    /**
     * A helper method that uses a field string (looks like this {@code [DEPENDENCY_POSITION]:[FIELD]}) to get a dependency from the provided dependencies list.
     * <p>
     * @param location Provide information about where this is called, so it's easier to debug should an error be thrown.
     * @param dependencies An ordered list of {@link Component}s that the code can index into via the provided string
     * @param string The string that will be used to obtain the index to use to get a dependency (format like so: {@code [DEPENDENCY_POSITION]:[FIELD]})
     * @return The successfully obtained dependency from the {@code dependencies}.
     **/
    public static Class<? extends Component> getDependencyFromString(String location, List<Class<? extends Component>> dependencies,  String string) {
        // Split the string into parts split in between ":", since the dependencyIndex is SUPPOSED to be first, set the dependency index to the first part of the split
        String[] splitTargetField = string.split(":");
        String dependencyIndex = splitTargetField[0];

        // If the dependency is successfully obtained (no exception), return that. If not, get very angry.
        try {
             return dependencies.get(Integer.valueOf(dependencyIndex));
        } catch (NumberFormatException exception) {
            Main.LOGGER.error("[Location: {}] Invalid dependency index \"{}\": Index isn't a number! Did you follow the \"[DEPENDENCY_POSITION]:[FIELD]\" format?", location, dependencyIndex);
        } catch (IndexOutOfBoundsException exception) {
            Main.LOGGER.error("[Location: {}] Dependency index provided is {}, however there are only {} dependencies! Remember that dependency indexes start at 0", location, dependencyIndex, dependencies.size());
        }
        return null;
    }

    /**
     * An overload method of the method sharing its name that also accepts {@code self} as an index.
     * <p>
     * @param location Provide information about where this is called, so it's easier to debug should an error be thrown.
     * @param dependencies An ordered list of {@link Component}s that the code can index into via the provided string
     * @param string The string that will be used to obtain the index to use to get a dependency (format like so: {@code [DEPENDENCY_POSITION]:[FIELD]})
     * @return The successfully obtained dependency from the {@code dependencies}.
     **/
    public static Class<? extends Component> getDependencyFromString(String location, List<Class<? extends Component>> dependencies, Class<? extends Component> self, String string) {
        if (string.contains("self")) return getDependencyFromString(location + " (via self)", List.of(self), string.replace("self", "0"));
        else return getDependencyFromString(location, dependencies, string);
    }
}
