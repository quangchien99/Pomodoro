package chn.phm.pomodoro

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver

object PomodoroLifecycleObserver : LifecycleObserver {
    var isAppForeground = false

    private val lifecycleEventObserver = LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                isAppForeground = false
            }
            Lifecycle.Event.ON_START -> {
                isAppForeground = true
            }
            else -> {}
        }
    }

    fun observeAppLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(lifecycleEventObserver)
    }
}