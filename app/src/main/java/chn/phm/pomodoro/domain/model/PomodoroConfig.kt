package chn.phm.pomodoro.domain.model

import chn.phm.pomodoro.R
import chn.phm.pomodoro.utils.Const
import kotlinx.serialization.Serializable

@Serializable
data class PomodoroConfig(
    val pomodoroDuration: Int = Const.DEFAULT_POMODORO_DURATION,
    val shortBreakDuration: Int = Const.DEFAULT_SHORT_BREAK_DURATION,
    val longBreakDuration: Int = Const.DEFAULT_LONG_BREAK_DURATION,
    val longBreakInterval: Int = Const.DEFAULT_POMODORO_INTERVAL,
    val alarmSound: AlarmSound = AlarmSound.DIGITAL,
    val background: Background = Background.NIGHT,
)

enum class AlarmSound(val resId: Int) {
    BEEP(R.raw.alarm_beep),
    CHURCH(R.raw.alarm_church),
    DIGITAL(R.raw.alarm_digital),
    ELECTRONIC(R.raw.alarm_electronic),
    MARIMBA(R.raw.alarm_marimba),
    OLD_MECHANIC(R.raw.alarm_old_mechanic),
    RINGTONE(R.raw.alarm_ringtone),
    SIMPLE(R.raw.alarm_simple)
}

enum class Background(val resId: Int) {
    GALAXY(R.drawable.bg_galaxy),
    LOVE_NIGHT(R.drawable.bg_love_night),
    NIGHT(R.drawable.bg_night),
    RAINY(R.drawable.bg_rainy),
    WINDOW(R.drawable.bg_window),
}
