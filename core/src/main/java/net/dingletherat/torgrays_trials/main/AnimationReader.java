package net.dingletherat.torgrays_trials.main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.Component;
import net.dingletherat.torgrays_trials.component.MovementComponent;

public class AnimationReader {
    public static final Map<String, Animation> ANIMATIONS = new HashMap<>();

    // Keys for stuff (yay!)
    /** The key used to get the name of the animation, which will be used in errors and as a key in the {@code ANIMATIONS} map**/
    public static final String KEY_NAME = "name";
    /** The key used to get a list of components whose fields an animation JSON will modify (optional) **/
    public static final String KEY_DEPENDENCIES = "dependencies";
    /** The key used to get the speed at which the animation will run at **/
    public static final String KEY_SPEED = "speed";
    /** The key used to obtain the frames  **/
    public static final String KEY_FRAMES = "frames";
    /** The path used to get animations **/
    public static final String PATH = "values/animations/";

    // Records
    /** Animation files, when all goes according to plan, will be parsed into this record.
     * It holds all the data that the JSON contains, just in its proper form (EX: Class paths as classes).
     * <p>
     * @param dependencies A list of paths to components whose fields you wanna modify in frames.
     *      For instance, if I add the path to a {@link SpriteComponent}, I may modify one of its fields in a frame
     *      by putting its position in the list starting from 0 (since its the only one I'll use 0) and the field I wanna modify after a ":".
     *      So I would make it look like so: {@code "[LIST_POSITION]:[FIELD]": [VALUE]} or in my case {@code "0:sprite": "entity/player/torgray_sheet"}
     * @param speed The speed at which each frame of the animation is running in delta-time, as a float.
     * @param frames This is basically the actual animation. The first layer is the map of conditions to frames. If you don't want a condition, just leave an empty string.
     *      If you do want one, put it in this format: {@code [DEPENDENCY]:[FIELD]:[=/>/<][VALUE]}. If it passes the condition, it will play the frames that you put.
     *      in the condition. For instance, if I wanted an animation to only play if my entity is idle (uses {@link MovementComponent}), I would do {@code "0:state:=IDLE": [animation...]}.
     *      Onto the animations. The condition must lead into another list, a list of {@link JSONObject} in the JSON. Or, in this case a list of maps.
     *      Every entry in the list is a frame inside the animation that will play while the condition is met. In each frame, you may change the properties of any dependency.
     *      (see parameter {@code dependencies} for more info on the format). This is a map, so you're able to change multiple properties at once if you want.
     **/
    public record Animation(List<Class<? extends Component>> dependencies,
            float speed, Map<Condition, List<Map<Class<? extends Component>, Object>>> frames) { }

    /** A helper record to the {@link Animations} record, which stores important data to a condition.
     * If you wanna know more about the parameters here, check that record.
     * <p>
     * @param dependency The component class that will be used to find the {@code field}.
     * @param field The field that will be checked an made sure it matches the {@code expectedValue}
     * @param expectedValue This is what we're checking the field to be. If the field is larger, smaller, or not equal to this, (depending on what you put) the animation under this condition will not play.
     **/
    public record Condition(Class<? extends Component> dependency, String field, Object expectedValue) { }


    public static void loadAnimations() {
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
            // Check if the json has the necessary stuff. If not, warn and continue
            if (!json.has(KEY_NAME) || !(json.get(KEY_NAME) instanceof String name)) {
                Main.LOGGER.warn("Invalid animation \"{}\": \"{}\" field is missing or is not a String.", fileNames.get(jsons.indexOf(json)), KEY_NAME);
                continue;
            }
            if (!json.has(KEY_SPEED) || !(json.get(KEY_SPEED) instanceof BigDecimal speed)) {
                Main.LOGGER.warn("Invalid animation \"{}\": \"{}\" field is missing or is not a BigDecimal.", name, KEY_SPEED);
                continue;
            }
            if (!json.has(KEY_FRAMES) || !(json.get(KEY_FRAMES) instanceof JSONObject framesObject)) {
                Main.LOGGER.warn("Invalid animation \"{}\": \"{}\" field is missing or is not a JSONObject.", name, KEY_FRAMES);
                continue;
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

            // Now, onto THE BIG ONE, the animations
            Map<Condition, List<Map<Class<? extends Component>, Object>>> frames = new HashMap<>();

            // Since we're dealing with a JSON object instead of the list, we'll loop through its keySet and get the array with the key
            for (String rawCondition : framesObject.keySet()) {
                JSONArray conditionFramesArray = framesObject.getJSONArray(rawCondition);

                // As usual with JSONArrays, turn the conditionFrames into a list
                List<JSONObject> rawConditionFrames = new ArrayList<>(IntStream.range(0, conditionFramesArray.length())
                                .mapToObj(conditionFramesArray::getJSONObject)
                                .toList());

                // Then, once again loop through it, putting all the data obtained in the list below
                List<Map<Class<? extends Component>, Object>> conditionFrames = new ArrayList<>();
                for (JSONObject rawFrame : rawConditionFrames) {

                    // All done here is just converting the dependencyIndex to a dependency and get the newValue, adding both to the Map below.
                    Map<Class<? extends Component>, Object> frame = new HashMap<>();
                    for (String targetField : rawFrame.keySet()) {
                        // Get the new value from the JSONObject by using the targetField as a key (like last time)
                        Object newValue = rawFrame.get(targetField);

                        // Get the dependency from the targetField as well, adding both the newValue and the obtained dependency to the frame map
                        Class<? extends Component> targetDependency = getDependencyFromString("TargetField \"" + targetField + "\" in " + name, dependencies, targetField);
                        frame.put(targetDependency, newValue);
                    }

                    conditionFrames.add(frame);
                }

                // Split the rawCondition into (what's supposed to be) 3 strings: the dependency, variable, and condition.
                // Get the dependency class with the first one, and use the second two in the condition declaration
                String[] splitCondition = rawCondition.split(":");
                Class<? extends Component> conditionDependency = getDependencyFromString("Condition \"" + rawCondition + "\" in " + name, dependencies, rawCondition);
                Condition condition = new Condition(conditionDependency, splitCondition[1], splitCondition[2]);

                frames.put(condition, conditionFrames);
            }

            Main.LOGGER.debug("{}", frames);

            // Least but last, create the animation and add it into THE ANIMATIONS LIST
            Animation animation = new Animation(dependencies, speed.floatValue(), frames);
            ANIMATIONS.put(name, animation);
        }

        Main.LOGGER.info("Loaded {} animations!", ANIMATIONS.size());
    }

    public static Class<? extends Component> getDependencyFromString(String location, List<Class<? extends Component>> dependencies, String string) {
        String[] splitTargetField = string.split(":");
        String dependencyIndex = splitTargetField[0];

        try {
             return dependencies.get(Integer.valueOf(dependencyIndex));
        } catch (NumberFormatException exception) {
            Main.LOGGER.error("[Location: {}] Invalid dependency index \"{}\": Index isn't a number!", location, string);
            Main.LOGGER.error("Did you follow the \"[DEPENDENCY_POSITION]:[FIELD]\" format?");
        }
        return null;
    }
}
