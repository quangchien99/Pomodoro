package chn.phm.pomodoro.domain.model

import chn.phm.pomodoro.utils.Const

data class PomodoroConfig(
    val pomodoroTime: Int = Const.DEFAULT_POMODORO_DURATION,
    val shortBreakTime: Int = Const.DEFAULT_SHORT_BREAK_DURATION,
    val longBreakTime: Int = Const.DEFAULT_LONG_BREAK_DURATION,
    val longBreakInterval: Int = Const.DEFAULT_POMODORO_INTERVAL,
    val alarmSound: String,
    val background: String,
)
