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
    var remainingTime by mutableStateOf(Const.DEFAULT_POMODORO_DURATION)
}

enum class TimerType {
    POMODORO,
    SHORT_BREAK,
    LONG_BREAK
}

enum class PomodoroState {
    READY,
    COUNTING,
    PAUSED,
    FINISHED
}