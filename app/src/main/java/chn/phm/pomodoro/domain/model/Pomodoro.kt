package chn.phm.pomodoro.domain.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class Pomodoro {
    var timerType by mutableStateOf(TimerType.POMODORO)
    var state by mutableStateOf(PomodoroState.READY)
    var remainingTime by mutableStateOf(timerType.duration * 60)
}

enum class TimerType(val duration: Int) {
    POMODORO(25),
    SHORT_BREAK(5),
    LONG_BREAK(15)
}

enum class PomodoroState {
    READY,
    COUNTING,
    PAUSED,
    FINISHED
}