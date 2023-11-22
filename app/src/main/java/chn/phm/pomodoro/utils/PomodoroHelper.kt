package chn.phm.pomodoro.utils

object PomodoroHelper {
    fun Int.convertToMinuteFormat(): String {
        val minutes = this / 60
        val secondsRemaining = this % 60

        val minutesStr = if (minutes < 10) "0$minutes" else minutes.toString()
        val secondsStr =
            if (secondsRemaining < 10) "0$secondsRemaining" else secondsRemaining.toString()

        return "$minutesStr:$secondsStr"
    }
}
