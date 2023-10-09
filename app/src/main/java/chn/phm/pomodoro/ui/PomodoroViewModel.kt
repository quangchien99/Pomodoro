package chn.phm.pomodoro.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import chn.phm.pomodoro.R
import chn.phm.pomodoro.domain.model.Pomodoro
import chn.phm.pomodoro.domain.model.PomodoroState
import chn.phm.pomodoro.domain.model.TimerType
import chn.phm.pomodoro.utils.Const.DEFAULT_POMODORO_INTERVAL
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor() : ViewModel() {
    private val _currentPomodoro = mutableStateOf(Pomodoro())
    val currentPomodoro: State<Pomodoro> = _currentPomodoro

    private var inProgressPomodoro: Pomodoro? = null

    private var countDownTimer: CountDownTimer? = null

    private var pomodoroCount = 0

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
                _currentPomodoro.value.remainingTime = timerType.duration * 60
                _currentPomodoro.value.state = PomodoroState.READY
            } else {
                _currentPomodoro.value.timerType = inProgressPomodoro?.timerType ?: timerType
                _currentPomodoro.value.remainingTime =
                    inProgressPomodoro?.remainingTime ?: (timerType.duration * 60)
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
            object : CountDownTimer(inProgressPomodoro!!.remainingTime.toLong() * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (_currentPomodoro.value.state == PomodoroState.COUNTING) {
                        _currentPomodoro.value.remainingTime = (millisUntilFinished / 1000).toInt()
                    }
                }

                override fun onFinish() {
                    _currentPomodoro.value.state = PomodoroState.FINISHED
                    playFinishSound(context)
                    when (_currentPomodoro.value.timerType) {
                        TimerType.POMODORO -> {
                            pomodoroCount++
                            if (pomodoroCount != DEFAULT_POMODORO_INTERVAL) {
                                changeType(TimerType.SHORT_BREAK)
                            } else {
                                changeType(TimerType.LONG_BREAK)
                                pomodoroCount = 0
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
        _currentPomodoro.value.remainingTime = currentPomodoro.value.timerType.duration * 60
        if (inProgressPomodoro != null) {
            copyPomodoroValues(inProgressPomodoro!!, _currentPomodoro.value)
        } else {
            inProgressPomodoro = createInProgressPomodoro()
        }
    }

    fun next() {
        _currentPomodoro.value.state = PomodoroState.READY
        when (currentPomodoro.value.timerType) {
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
        _currentPomodoro.value.remainingTime = currentPomodoro.value.timerType.duration * 60
        if (inProgressPomodoro != null) {
            copyPomodoroValues(inProgressPomodoro!!, _currentPomodoro.value)
        } else {
            inProgressPomodoro = createInProgressPomodoro()
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

    // Add a method to play the sound
    private fun playFinishSound(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.digital)
        mediaPlayer?.start()

        // Release the MediaPlayer when playback is completed
        mediaPlayer?.setOnCompletionListener {
            it.release()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}