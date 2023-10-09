package chn.phm.pomodoro.domain.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import chn.phm.pomodoro.utils.Const

@Stable
class Pomodoro {
    var timerType by mutableStateOf(TimerType.POMODORO)
    var state by mutableStateOf(PomodoroState.READY)
    var remainingTime by mutableStateOf(timerType.duration * Const.SECOND_TO_MINUTE_VALUE)
}

enum class TimerType(val duration: Int) {
    POMODORO(Const.DEFAULT_POMODORO_DURATION),
    SHORT_BREAK(Const.DEFAULT_SHORT_BREAK_DURATION),
    LONG_BREAK(Const.DEFAULT_LONG_BREAK_DURATION)
}

enum class PomodoroState {
    READY,
    COUNTING,
    PAUSED,
    FINISHED
}