package chn.phm.pomodoro

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData

object PomodoroLifecycleObserver : LifecycleObserver {
    val isAppForeground = MutableLiveData<Boolean>(true)

    private val lifecycleEventObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                isAppForeground.value = false
            }
            Lifecycle.Event.ON_START -> {
                isAppForeground.value = true
            }
            else -> {}
        }
    }

    fun observeAppLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(lifecycleEventObserver)
    }
}
