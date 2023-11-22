package chn.phm.pomodoro.ui

sealed class PomodoroAction {
    data class PlaySound(val soundId: Int) : PomodoroAction()
    data class StartCountingService(
        val countingTime: Int,
        val soundId: Int,
        val backgroundId: Int
    ) : PomodoroAction()

    data class CountingServiceDestroyed(val remainingTime: Int)
    object StopCountingService : PomodoroAction()
    object NONE : PomodoroAction()
}
