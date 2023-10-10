package chn.phm.pomodoro.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chn.phm.pomodoro.data.datastore.PomodoroConfigCache
import chn.phm.pomodoro.domain.model.Pomodoro
import chn.phm.pomodoro.domain.model.PomodoroConfig
import chn.phm.pomodoro.domain.model.PomodoroState
import chn.phm.pomodoro.domain.model.TimerType
import chn.phm.pomodoro.utils.Const.COUNTDOWN_INTERVAL
import chn.phm.pomodoro.utils.Const.DEFAULT_POMODORO_INTERVAL
import chn.phm.pomodoro.utils.Const.SECOND_TO_MINUTE_VALUE
import chn.phm.pomodoro.utils.Const.ZERO_VALUE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val pomodoroConfigCache: PomodoroConfigCache
) : ViewModel() {
    private val _currentPomodoro = mutableStateOf(Pomodoro())
    val currentPomodoro: State<Pomodoro> = _currentPomodoro

    private val _currentPomodoroConfig = mutableStateOf(PomodoroConfig())
    val currentPomodoroConfig: State<PomodoroConfig> = _currentPomodoroConfig

    private var inProgressPomodoro: Pomodoro? = null

    private var countDownTimer: CountDownTimer? = null

    private var pomodoroCount = ZERO_VALUE

    init {
        viewModelScope.launch {
            pomodoroConfigCache.cacheState.collect { pomodoroConfig ->
                _currentPomodoroConfig.value = pomodoroConfig
                _currentPomodoro.value.remainingTime = getCurrentDuration()
            }
        }

        viewModelScope.launch {
            pomodoroConfigCache.fetchConfig()
        }
    }

    fun changeType(timerType: TimerType) {
        if (_currentPomodoro.value.state != PomodoroState.READY) {
            if (inProgressPomodoro != null) {
                copyPomodoroValues(inProgressPomodoro!!, _currentPomodoro.value)
            } else {
                inProgressPomodoro = createInProgressPomodoro()
            }
        }

        if (_currentPomodoro.value.timerType != timerType) {
            if (timerType != inProgressPomodoro?.timerType || inProgressPomodoro == null) {
                _currentPomodoro.value.timerType = timerType
                updateCurrentDuration()
                _currentPomodoro.value.state = PomodoroState.READY
            } else {
                _currentPomodoro.value.timerType = inProgressPomodoro?.timerType ?: timerType
                _currentPomodoro.value.remainingTime =
                    inProgressPomodoro?.remainingTime ?: getCurrentDuration()
                _currentPomodoro.value.state = inProgressPomodoro?.state ?: PomodoroState.READY
            }
        }
    }

    fun start(context: Context) {
        countDownTimer?.cancel()
        _currentPomodoro.value.state = PomodoroState.COUNTING
        if (inProgressPomodoro != null) {
            copyPomodoroValues(inProgressPomodoro!!, _currentPomodoro.value)
        } else {
            inProgressPomodoro = createInProgressPomodoro()
        }

        // Start a new countdown
        countDownTimer =
            object : CountDownTimer(
                inProgressPomodoro!!.remainingTime.toLong() * COUNTDOWN_INTERVAL,
                COUNTDOWN_INTERVAL
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    if (_currentPomodoro.value.state == PomodoroState.COUNTING) {
                        _currentPomodoro.value.remainingTime =
                            (millisUntilFinished / COUNTDOWN_INTERVAL).toInt()
                    }
                }

                override fun onFinish() {
                    _currentPomodoro.value.state = PomodoroState.FINISHED
                    playAlarmSound(context, _currentPomodoroConfig.value.alarmSound.resId)
                    when (_currentPomodoro.value.timerType) {
                        TimerType.POMODORO -> {
                            pomodoroCount++
                            if (pomodoroCount != DEFAULT_POMODORO_INTERVAL) {
                                changeType(TimerType.SHORT_BREAK)
                            } else {
                                changeType(TimerType.LONG_BREAK)
                                pomodoroCount = ZERO_VALUE
                            }
                        }
                        else -> {
                            changeType(TimerType.POMODORO)
                        }
                    }
                }
            }.start()
    }

    fun pause() {
        countDownTimer?.cancel()
        if (inProgressPomodoro != null) {
            copyPomodoroValues(inProgressPomodoro!!, _currentPomodoro.value)
        } else {
            inProgressPomodoro = createInProgressPomodoro()
        }
        _currentPomodoro.value.state = PomodoroState.PAUSED
    }

    fun resume(context: Context) {
        start(context = context)
    }

    fun restart() {
        _currentPomodoro.value.state = PomodoroState.READY
        updateCurrentDuration()
        if (inProgressPomodoro != null) {
            copyPomodoroValues(inProgressPomodoro!!, _currentPomodoro.value)
        } else {
            inProgressPomodoro = createInProgressPomodoro()
        }
    }

    fun next() {
        _currentPomodoro.value.state = PomodoroState.READY
        when (_currentPomodoro.value.timerType) {
            TimerType.POMODORO -> {
                _currentPomodoro.value.timerType = TimerType.SHORT_BREAK
            }
            TimerType.SHORT_BREAK -> {
                _currentPomodoro.value.timerType = TimerType.LONG_BREAK
            }
            TimerType.LONG_BREAK -> {
                _currentPomodoro.value.timerType = TimerType.POMODORO
            }
        }
        updateCurrentDuration()
        if (inProgressPomodoro != null) {
            copyPomodoroValues(inProgressPomodoro!!, _currentPomodoro.value)
        } else {
            inProgressPomodoro = createInProgressPomodoro()
        }
    }

    private fun updateCurrentDuration() {
        when (_currentPomodoro.value.timerType) {
            TimerType.POMODORO -> {
                _currentPomodoro.value.remainingTime =
                    _currentPomodoroConfig.value.pomodoroDuration * SECOND_TO_MINUTE_VALUE
            }
            TimerType.SHORT_BREAK -> {
                _currentPomodoro.value.remainingTime =
                    _currentPomodoroConfig.value.shortBreakDuration * SECOND_TO_MINUTE_VALUE
            }
            TimerType.LONG_BREAK -> {
                _currentPomodoro.value.remainingTime =
                    _currentPomodoroConfig.value.longBreakDuration * SECOND_TO_MINUTE_VALUE
            }
        }
    }

    private fun getCurrentDuration() = when (_currentPomodoro.value.timerType) {
        TimerType.POMODORO -> {
            _currentPomodoroConfig.value.pomodoroDuration * SECOND_TO_MINUTE_VALUE
        }
        TimerType.SHORT_BREAK -> {
            _currentPomodoroConfig.value.shortBreakDuration * SECOND_TO_MINUTE_VALUE
        }
        TimerType.LONG_BREAK -> {
            _currentPomodoroConfig.value.longBreakDuration * SECOND_TO_MINUTE_VALUE
        }
    }

    fun updateConfig(newConfig: PomodoroConfig) {
        viewModelScope.launch {
            pomodoroConfigCache.updateConfig(newConfig)
        }
    }

    private fun copyPomodoroValues(destination: Pomodoro, source: Pomodoro) {
        destination.timerType = source.timerType
        destination.remainingTime = source.remainingTime
        destination.state = source.state
    }

    private fun createInProgressPomodoro(): Pomodoro {
        return Pomodoro().apply {
            copyPomodoroValues(this, _currentPomodoro.value)
        }
    }

    fun playAlarmSound(context: Context, soundId: Int) {
        val mediaPlayer = MediaPlayer.create(context, soundId)
        mediaPlayer?.start()

        // Release the MediaPlayer when playback is completed
        mediaPlayer?.setOnCompletionListener {
            it.release()
        }
    }
}