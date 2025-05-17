package net.dinglezz.torgrays_trials.main;

public class States {
    public enum GameStates {
        TITLE,
        PLAY,
        DIALOGUE,
        PAUSE,
        CHARACTER,
        GAME_OVER,
        EXCEPTION,
        TRADE,
        MAP
    }
    public enum UIStates {
        // Title
        TITLE_STATE_MAIN,
        TITLE_STATE_MODES,
        PAUSE_STATE_MAIN,

        // Pause
        PAUSE_STATE_SETTINGS_MAIN,
        PAUSE_STATE_CONTROLS,
        PAUSE_STATE_NOTIFICATION,
        PAUSE_STATE_CONFIRM,

        // Trade
        TRADE_STATE_SELECT,
        TRADE_STATE_BUY,
        TRADE_STATE_SELL
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
