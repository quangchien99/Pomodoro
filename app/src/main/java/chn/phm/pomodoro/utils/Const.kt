package chn.phm.pomodoro.utils

internal object Const {
    /*
    Default values
     */
    const val DEFAULT_POMODORO_INTERVAL = 4
    const val DEFAULT_POMODORO_DURATION = 25
    const val DEFAULT_SHORT_BREAK_DURATION = 5
    const val DEFAULT_LONG_BREAK_DURATION = 15

    /*
    Commonly used values
    */
    const val SECOND_TO_MINUTE_VALUE = 60
    const val COUNTDOWN_INTERVAL = 1000L
    const val ZERO_VALUE = 0

    /*
    Data store value
    */
    const val POMODORO_DATA_STORE_NAME = "pomodoro_config.json"
}
