package chn.phm.pomodoro.data.datastore.serializer

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import chn.phm.pomodoro.domain.model.PomodoroConfig
import chn.phm.pomodoro.utils.Const.POMODORO_DATA_STORE_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PomodoroConfigSerializer : Serializer<PomodoroConfig> {

    override val defaultValue: PomodoroConfig = PomodoroConfig()

    override suspend fun readFrom(input: InputStream): PomodoroConfig {
        try {
            return Json.decodeFromString(
                deserializer = PomodoroConfig.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            throw CorruptionException("Error occurred during decoding the storage", exception)
        }
    }

    override suspend fun writeTo(t: PomodoroConfig, output: OutputStream) =
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(serializer = PomodoroConfig.serializer(), value = t)
                    .toByteArray()
            )
        }

    val Context.pomodoroConfigDataStore: DataStore<PomodoroConfig> by dataStore(
        fileName = POMODORO_DATA_STORE_NAME,
        serializer = PomodoroConfigSerializer
    )
}