package chn.phm.pomodoro.domain.model

data class Pomodoro(
    var timerType: TimerType = TimerType.POMODORO,
    var state: PomodoroState = PomodoroState.READY,
    var remainingTime: Int = timerType.duration * 60
)

enum class TimerType(val duration: Int) {
    POMODORO(25),
    SHORT_BREAK(5),
    LONG_BREAK(15)
}

enum class PomodoroState {
    READY,
    COUNTING,
    PAUSED
}