package main;

public enum States {
    // Main
    STATE_TILE,
    STATE_PLAY,
    STATE_PAUSE,
    STATE_DIALOGUE,
    STATE_CHARACTER,

    // Title Sub-States
    TITLE_STATE_MAIN,
    TITLE_STATE_MODES,

    // Pause Sub-States
    PAUSE_SETTINGS_MAIN,
    PAUSE_STATE_MAIN,
    PAUSE_CONTROLS,
    PAUSE_SETTINGS_NOTIFICATION,
    PAUSE_SETTINGS_CONFIRM
}
