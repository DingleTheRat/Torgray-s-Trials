package net.dinglezz.torgrays_trials.main;

public enum States {
    // Main
    STATE_TITLE,
    STATE_PLAY,
    STATE_PAUSE,
    STATE_DIALOGUE,
    STATE_CHARACTER,
    STATE_GAME_OVER,
    STATE_TRANSITION,
    STATE_TRADE,
    STATE_MAP,

    // Title Sub-States
    TITLE_STATE_MAIN,
    TITLE_STATE_MODES,

    // Pause Sub-States
    PAUSE_STATE_SETTINGS_MAIN,
    PAUSE_STATE_MAIN,
    PAUSE_STATE_CONTROLS,
    PAUSE_STATE_NOTIFICATION,
    PAUSE_STATE_CONFIRM,

    // Trade Sub-States
    TRADE_STATE_SELECT,
    TRADE_STATE_BUY,
    TRADE_STATE_SELL,
}
