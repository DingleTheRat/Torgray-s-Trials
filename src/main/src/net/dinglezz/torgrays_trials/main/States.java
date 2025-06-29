package net.dinglezz.torgrays_trials.main;

public class States {
    public enum GameStates {
        TITLE,
        PLAY,
        PAUSE,
        GAME_END,
        EXCEPTION,
    }
    public enum UIStates {
        NONE(false, false),
        JUST_DEFAULT(true, true),
        DIALOGUE(true, true),
        INTERACT(true, true),
        PAUSE(true, true),
        CHARACTER(false, false),
        TRADE(false, false),
        MAP(false, false),
        SAVE(false, false),;

        final boolean defaultKeyboardInput;
        final boolean defaultUI;
        UIStates(boolean defaultKeyboardInput, boolean defaultUI) {
            this.defaultKeyboardInput = defaultKeyboardInput;
            this.defaultUI = defaultUI;
        }
    }
    public enum DarknessStates {
        NIGHT,
        NEW_DUSK,
        GLOOM,
        LIGHT_GLOOM,
        DARK_GLOOM,
        DUSK
    }
    public enum ExceptionStates {
        NOTHING,
        ONLY_IGNORABLE,
        IGNORABLE_QUITABLE,
        ONLY_QUITABLE,
        INSTANT_QUIT,
    }
}
