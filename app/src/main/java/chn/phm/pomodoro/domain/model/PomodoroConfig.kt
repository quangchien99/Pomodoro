package chn.phm.pomodoro.domain.model

data class PomodoroConfig(
    val pomodoroTime: Int = 25,
    val shortBreakTime: Int = 5,
    val longBreakTime: Int = 15,
    val longBreakInterval: Int = 4,
    val alarmSound: String,
    val background: String,
)
