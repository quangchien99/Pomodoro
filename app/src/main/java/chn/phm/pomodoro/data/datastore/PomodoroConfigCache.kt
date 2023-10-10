package chn.phm.pomodoro.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import chn.phm.pomodoro.domain.model.PomodoroConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.SerializationException
import javax.inject.Inject

class PomodoroConfigCache @Inject constructor(
    private val pomodoroConfigDataStore: DataStore<PomodoroConfig>
) {

    private val _cacheState = MutableStateFlow<PomodoroConfig>(PomodoroConfig())
    val cacheState = _cacheState.asStateFlow()

    suspend fun fetchConfig() {
        try {
            pomodoroConfigDataStore.data.collect {
                _cacheState.value = it
            }
        } catch (error: SerializationException) {
            Log.e("PomodoroCache", "${error.message}")
        }
    }

    suspend fun updateConfig(newConfig: PomodoroConfig) {
        pomodoroConfigDataStore.updateData { currentConfig: PomodoroConfig ->
            currentConfig.copy(
                pomodoroDuration = newConfig.pomodoroDuration,
                shortBreakDuration = newConfig.shortBreakDuration,
                longBreakDuration = newConfig.longBreakDuration,
                longBreakInterval = newConfig.longBreakInterval,
                background = newConfig.background,
                alarmSound = newConfig.alarmSound

            )
        }
    }
}