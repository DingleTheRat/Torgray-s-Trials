package net.dingletherat.torgrays_trials.main;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    // ---------------------
    // Keys
    // ---------------------

    // Keys for main JSON
    /** The key used to get the name of the animation, which will be used in errors and as a key in the {@link #ANIMATIONS} map**/
    public static final String KEY_NAME = "name";
    /** The key used to get a list of components whose fields an animation JSON will modify (optional) **/
    public static final String KEY_DEPENDENCIES = "dependencies";
    /** An optional key that sets what the target component (set by the {@link AnimationComponent}) needs to be for this animation to function correctly.
    If the target component doesn't match the self you set, then the {@link AnimationComponent} will be discarded with a warning **/
    /** The key used to get the speed at which the animation will run at **/
    public static final String KEY_SPEED = "speed";
    /** The key used to obtain the conditions **/
    public static final String KEY_CONDITIONS = "conditions";

    // Keys for conditions
    public static String KEY_CONDITION = "condition";
    public static String KEY_FRAMES = "frames";
    public static final String KEY_DEPENDENCY_INDEX = "dependency_index";
    public static final String KEY_FIELD = "field";
    public static final String KEY_EXPECTATION = "expectation";
    public static final String KEY_EXPECTED = "expected";

    // Other keys
    /** The path used to get animations **/
    public static final String PATH = "values/animations/";

    // ---------------------
    // Records
    // ---------------------

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
    public record RawAnimation(List<Class<? extends Component>> dependencies, float speed, Map<RawDependencyField, List<List<RawDependencyField>>> frames) {
        public RawAnimation changeSpeed(float newSpeed) {
            return new RawAnimation(dependencies, newSpeed, frames);
        }
    }

    /**
     * When an {@link AnimationComponent} is declared, its entity's components and the will be used to initialize the dependencies of an animation and store it in here via {@link }.
     **/
    public record Animation(List<Component> dependencies, float speed, Map<DependencyField, List<List<DependencyField>>> frames) { }

    /** A helper record to the {@link RawAnimation} record, which stores data related to fields, but with the dependency uninitialized and the field's name as a string.
     * It's mainly used to store conditions and fieldSetters, with the {@link #expectation} parameter being exclusively for conditions.
     * If you wanna learn more, about how it's used, checked out the {@link #frames} in the {@link RawAnimation} record.
     * <p>
     * @param dependency The component class in which the {@link #field} SHOULD be located
     * @param field The name of the field that's in the {@link #dependency}
     * @param value This is either going to be the new value of the field (when used as a fieldSetter) or the expected value of the field (when used as a condition), depending on the use case of this record.
     **/
    public record RawDependencyField(Class<? extends Component> dependency, String field, Object value, String expectation) { }

    public record DependencyField(Component dependency, Field field, Object value, String expectation) { }

    // ---------------------
    // Main methods
    // ---------------------

    /**
     * Calls {@link #loadRawAnimations} for every file in the {@link #PATH}, as long as it's a JSON.
     * All {@link RawAnimation}s created by the {@link #loadRawAnimations} is added into the {@link #ANIMATIONS} {@link List} to be used by other methods.
     * This method is recommended to be called upon world creation or at the game's start.
     * If you wanna know more, look at {@link #loadRawAnimations}'s documentation.
     * <p>
     * Note: The {@link #ANIMATIONS} list is cleared at the start of the method.
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
        if (!json.has(KEY_CONDITIONS) || !(json.get(KEY_CONDITIONS) instanceof JSONArray conditionsArray)) {
            Main.LOGGER.warn("Invalid animation \"{}\": \"{}\" field is missing or is not a JSONArray.", name, KEY_CONDITIONS);
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

        // Convert the conditions array
        List<JSONObject> conditionJsons = new ArrayList<>(IntStream.range(0, conditionsArray.length())
                        .mapToObj(conditionsArray::getJSONObject)
                        .toList());

        /*
         * Each JSONObject should either contain fields that make up a condition or a condition field.
         * Determine which one it is and call the corresponding method to parse it. Once parsed add it into the conditions list
         * A map is used here because, later, we'll need to obtain the JSONObject for the frames, and since fails are not added indexes wouldn't match up with lists.
         */
        Map<JSONObject, RawDependencyField> conditions = new LinkedHashMap<>();
        conditionJsons.stream()
            .map(conditionJson -> Map.entry(conditionJson, conditionJson.has(KEY_CONDITION) ?
                    stringToCondition(conditionJson.getString(KEY_CONDITION), dependencies, "String condition " + conditionJson.getString(KEY_CONDITION)) : // Compact Format
                    jsonToCondition(conditionJson))) // Expanded Format
            .filter(entry -> entry.getValue() != null)
            .forEach(entry -> conditions.put(entry.getKey(), entry.getValue()));

        /* Now, onto the frames. We loop through the keySet of the conditions, as it's more useful here, though the values will be used later
           All frames will be paired up with their condition into the final conditionsAndFrames map */
        Map<RawDependencyField, List<List<RawDependencyField>>> conditionsAndFrames = new LinkedHashMap<>();
        int conditionIndex = 0; // For warning purposes
        for (JSONObject conditionJson : conditions.keySet()) {
            conditionIndex++;

            // Ensure frames is actually there
            if (!(conditionJson.has(KEY_FRAMES)) || !(conditionJson.get(KEY_FRAMES) instanceof JSONArray framesArray)) {
                Main.LOGGER.warn("Condition #{} in animation \"{}\": \"{}\" field is missing or is not a JSONArray.", conditionIndex, name, KEY_FRAMES);
                continue;
            }

            // Convert the frames to a list so it's easier to loop through
            List<JSONObject> jsonFrames = new ArrayList<>(IntStream.range(0, framesArray.length())
                            .mapToObj(framesArray::getJSONObject)
                            .toList());

            // Loop through the jsonFrames, which is a list of jsons that should each contain a fieldSetter that modifies a field
            List<List<RawDependencyField>> frames = new ArrayList<>();
            for (JSONObject frameJson : jsonFrames) {
                List<RawDependencyField> frame = new ArrayList<>();
                int targetFieldIndex = 0;

                for (String targetField : frameJson.keySet()) {
                    targetFieldIndex++;

                    // Each targetField should ideally be in this format: "[DEPENDENCY]:[FIELD]", so make sure it's following that format
                    String[] splitTargetField = targetField.split(":");
                    if (splitTargetField.length != 2) {
                        Main.LOGGER.warn("[Location: Condition #{} in animation {}] Target field #{} doesn't have enough \":\": expected 1, but got {}! Did you follow the \"[DEPENDENCY_INDEX:FIELD]\" format?", conditionIndex, name, targetFieldIndex, splitTargetField.length);
                        continue;
                    }

                    // Get the 3 essential parts for a fieldModifier
                    Class<? extends Component> dependency = getDependencyFromString(dependencies, targetField, "TargetField \"" + targetField + "\" in condition #" + conditionIndex + " in " + name);
                    String field = splitTargetField[1];
                    Object newValue = frameJson.get(targetField);

                    // Then create a new RawDependencyField with them and add it into the frame
                    frame.add(new RawDependencyField(dependency, field, newValue, null));
                }

                frames.add(frame);
            }

            conditionsAndFrames.put(conditions.get(conditionJson), frames);
        }

        // Create the animation and return it
        RawAnimation animation = new RawAnimation(dependencies, speed.floatValue(), conditionsAndFrames);
        return animation;
    }

    public static Animation initializeAnimation(int entity, RawAnimation rawAnimation, JSONObject dependencyIndices) {
        // Get the name for warning purposes
        String name = EntityHandler.getComponent(entity, NameComponent.class).get().name;
        String animationName = ANIMATIONS.entrySet().stream()
            .filter(entry -> entry.getValue().equals(rawAnimation)).map(Map.Entry::getKey).findFirst().orElse("unknown");

        // Go through the raw animation's dependencies and resolve them, adding the resolved ones into the list below
        List<Component> dependencies = new ArrayList<>();
        rawAnimation.dependencies().stream()
           .map(dependency -> EntityHandler.getComponent(entity, dependency))
           .flatMap(Optional::stream) // FYI: Flat-map skips over nulls
           .forEach(dependencies::add);

        // Warn if something was skipped
        rawAnimation.dependencies().stream()
           .filter(dependency -> EntityHandler.getComponent(entity, dependency).isEmpty())
           .forEach(dependency -> Main.LOGGER.warn("Dependency \"{}\" in animation \"{}\" was skipped: not found in entity {}", dependency.getSimpleName(), animationName, entity));

        // Index all the resolved dependencies
        List<Component> indexedDependencies = applyDependencyIndices(entity, dependencies, dependencyIndices);

        // Do the same for conditions, but also keep track of the raw condition as a key so we can pair them with frames later
        Map<RawDependencyField, DependencyField> resolvedConditions = new LinkedHashMap<>();
        AtomicInteger conditionIndex = new AtomicInteger(); // This is to provide more info for the location below
        rawAnimation.frames().keySet().stream()
            .filter(rawCondition -> !(rawCondition.dependency() == null && rawCondition.field() == null && rawCondition.value() == null && rawCondition.expectation() == null))
            .flatMap(rawCondition -> indexedDependencies.stream().filter(rawCondition.dependency()::isInstance).findFirst()
               .map(dependency -> {
                   int i = conditionIndex.getAndIncrement();
                   return resolveDependencyField(rawCondition, dependency, "Condition #" + i + " in animation \"" + animationName + "\"");
               })
               .filter(Objects::nonNull)
               .map(condition -> Map.entry(rawCondition, condition))
               .stream())
            .forEach(entry -> resolvedConditions.put(entry.getKey(), entry.getValue()));

        // Add in the blank conditions that were previously skipped as well (they were skipped cuz they don't need initialization, they're blank)
        rawAnimation.frames().keySet().stream()
            .filter(rawCondition -> rawCondition.dependency() == null && rawCondition.field() == null && rawCondition.value() == null && rawCondition.expectation() == null)
            .forEach(rawCondition -> resolvedConditions.put(rawCondition, new DependencyField(null, null, null, null)));

        // More warnings
        rawAnimation.frames().keySet().stream()
           .filter(rawCondition -> !resolvedConditions.containsKey(rawCondition))
           .filter(rawCondition -> !(rawCondition.dependency() == null && rawCondition.field() == null && rawCondition.value() == null && rawCondition.expectation() == null))
           .forEach(rawCondition -> Main.LOGGER.warn("Condition \"{}:{}:{}\" in animation \"{}\" was skipped: dependency \"{}\" not found in entity {}",
               rawCondition.dependency().getSimpleName(), rawCondition.field(), rawCondition.value(), animationName, rawCondition.dependency().getSimpleName(), entity));

        // Pair up conditions with their frames, digging into the raw animation to get the uninitialized frames and resolving the dependencies within
        Map<DependencyField, List<List<DependencyField>>> frames = new LinkedHashMap<>();
        AtomicInteger setterConditionIndex = new AtomicInteger();
        AtomicInteger setterIndex = new AtomicInteger();
        resolvedConditions.forEach((rawCondition, condition) -> {
           int conditionI = setterConditionIndex.getAndIncrement();
           List<List<DependencyField>> frameList = rawAnimation.frames().get(rawCondition).stream()
               .map(frame -> frame.stream()
                    .flatMap(fieldSetter -> indexedDependencies.stream().filter(fieldSetter.dependency()::isInstance).findFirst()
                       .map(dependency -> {
                           int i = setterIndex.getAndIncrement();
                           return resolveDependencyField(fieldSetter, dependency, "Field setter #" + i + " in condition #" + conditionI + " in animation \"" + animationName + "\"");
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
                   .flatMap(List::stream)
                    .filter(fieldSetter -> indexedDependencies.stream().noneMatch(fieldSetter.dependency()::isInstance))
                   .forEach(fieldSetter -> Main.LOGGER.warn("Frame dependency \"{}\" in condition \"{}:{}:{}\" of animation \"{}\" was skipped: not found in entity {}",
                       fieldSetter.dependency().getSimpleName(), rawCondition.dependency().getSimpleName(), rawCondition.field(), rawCondition.value(), animationName, entity)));

        // Use all that to create our animation!
        Animation animation = new Animation(dependencies, rawAnimation.speed(), frames);
        return animation;
    }

    // ---------------------
    // Helper methods
    // ---------------------

    // TODO: Allow for blank conditions
    public static RawDependencyField stringToCondition(String string, List<Class<? extends Component>> dependencies, String location) {
        /* Split the rawCondition into (what's supposed to be) 4 strings: the dependency, field, type of expectation (such as =) and the expected value.
           Warn in the case there's not 4 "splits" present */
        String[] splitString = string.split(":");
        if (splitString.length != 4) {
            Main.LOGGER.warn("[Location: {}] String condition doesn't have enough \":\": expected 4, but got {}! Did you follow the \"[DEPENDENCY_INDEX:FIELD]:[EXPECTATION]:[EXPECTED]\" format?", location, splitString.length);
            return null;
        }

        /* Get the 4 necessary segments.
           For the dependency specifically, we'll use the getDependencyFromString method on the whole string as it splits every by itself */
        Class<? extends Component> dependency = getDependencyFromString(dependencies, string, location);
        String field = splitString[1];
        String expectation = splitString[2];
        String expected = splitString[3];

        // Insert the segments accordingly into the RawDependencyField and return
        return new RawDependencyField(dependency, field, expected, expectation);
    }

    public static RawDependencyField jsonToCondition(JSONObject json) {
        Main.LOGGER.warn("Json to condition format not supported!");
        return null;
    }

    /**
     * A helper method that uses a field string (looks like this {@code [DEPENDENCY_POSITION]:[FIELD]}) to get a dependency from the provided dependencies list.
     * <p>
     * @param location Provide information about where this is called, so it's easier to debug should an error be thrown.
     * @param dependencies An ordered list of {@link Component}s that the code can index into via the provided string
     * @param string The string that will be used to obtain the index to use to get a dependency (format like so: {@code [DEPENDENCY_POSITION]:[FIELD]})
     * @return The successfully obtained dependency from the {@code dependencies}.
     **/
    public static Class<? extends Component> getDependencyFromString(List<Class<? extends Component>> dependencies, String string, String location) {
        // Split the string into parts split in between ":", since the dependencyIndex is SUPPOSED to be first, set the dependency index to the first part of the split
        String[] splitTargetField = string.split(":");
        String dependencyIndex = splitTargetField[0];

        // If the dependency is successfully obtained (no exception), return that. If not, get very angry.
        try {
             return dependencies.get(Integer.parseInt(dependencyIndex));
        } catch (NumberFormatException exception) {
            Main.LOGGER.error("[Location: {}] Invalid dependency index \"{}\": Index isn't a number! Did you follow the \"[DEPENDENCY_POSITION]:[FIELD]\" format?", location, dependencyIndex);
        } catch (IndexOutOfBoundsException exception) {
            Main.LOGGER.error("[Location: {}] Dependency index provided is \"{}\", however there are only {} dependencies! Remember that dependency indexes start at 0", location, dependencyIndex, dependencies.size());
        }
        return null;
    }

    public static List<Component> applyDependencyIndices(int entity, List<Component> dependencies, JSONObject dependencyIndices) {
        // Loop through all the dependencyIndices, checking if each one is correct. If it is, add it to the returnList, which is a copy of the dependencies list
        List<Component> returnList = new ArrayList<>(dependencies);
        for (String rawDependency : dependencyIndices.keySet()) {

            Class<? extends Component> dependencyClass = UtilityTool.getClassFromPath(rawDependency, Component.class, "");
            if (dependencyClass == null) continue;

            // Get the dependency index and make sure it's an Integer. This is done first for warnings
            Object rawEntryIndex = dependencyIndices.get(rawDependency);
            if (!(rawEntryIndex instanceof Integer entryIndex)) {
                Main.LOGGER.warn("Invalid entry index \"{}\" for dependency \"{}\": expected an Integer but got {}", rawEntryIndex, rawDependency,
                    rawEntryIndex == null ? "null" : rawEntryIndex.getClass().getSimpleName());
                continue;
            }

            // Get the component from the entity, via our previously obtained index
            Component indexedDependency = EntityHandler.getComponent(entity, dependencyClass, entryIndex).orElse(null);
            if (indexedDependency == null) {
                Main.LOGGER.warn("Couldn't find component \"{}\" at index {}: no such component present for entity {}", dependencyClass.getSimpleName(), entryIndex, entity);
                continue;
            }

            // Find the position of the original dependency, so we can replace it with the new one
            int oldIndex = IntStream.range(0, returnList.size())
                .filter(i -> dependencyClass.isInstance(returnList.get(i)))
                .findFirst()
                .orElse(-1);

            // If nothing is found, warn
            if (oldIndex == -1) {
                Main.LOGGER.warn("Couldn't load dependency index \"{}\" for component \"{}\": component is not a declared component in animation's dependencies!", entryIndex, dependencyClass.getSimpleName());
                continue;
            }

            // Replace the old dependency with the new one
            returnList.set(oldIndex, indexedDependency);
        }

        return returnList;
    }

    public static DependencyField resolveDependencyField(RawDependencyField rawDependencyField, Component dependency, String location) {
        Field field = null;

        /* If there's a getter method, use that instead of getting the field normally. If not, it'll throw an exception and we move on
           This is done first to prioritize getters */
        String getterName = "get" + UtilityTool.capitalize(rawDependencyField.field());
        try {
             Method getter = rawDependencyField.dependency().getMethod(getterName);
             Object getterResult = getter.invoke(dependency);

            if (getterResult == null) {
                Main.LOGGER.debug("[Location: {}] Attempting to get field: getter \"{}\" returned null", location, getterName);
                throw new NoSuchMethodException();
            }
            if(!(getterResult instanceof Field getterField)) {
                Main.LOGGER.warn("[Location: {}] Couldn't invoke getter method \"{}\": method doesn't return a \"Field\" object", location, getterName);
                return null;
            }
            field = getterField;
        } catch (NoSuchMethodException exception) { } // As mentioned, means it has no getter, so just go to the field block
        catch (IllegalAccessException exception) {
            Main.LOGGER.warn("[Location: {}] Couldn't invoke getter method \"{}\": method is inaccessible", location, getterName);
            return null;
        } catch (InvocationTargetException exception) {
            Main.handleException(exception);
            return null;
        }

        if (field == null) {
            try {
                field = rawDependencyField.dependency().getField(rawDependencyField.field());
            } catch (NoSuchFieldException exception) {
                Main.LOGGER.warn("[Location: {}] Field \"{}\" not found in dependency {} nor its supers. Is the field's visibility restricted?", location, rawDependencyField.field(), rawDependencyField.dependency().getSimpleName());
                return null;
            }
        }

        return new DependencyField(dependency, field, rawDependencyField.value(), rawDependencyField.expectation());
    }
}
