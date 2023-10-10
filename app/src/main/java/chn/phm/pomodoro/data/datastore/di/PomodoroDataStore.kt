package chn.phm.pomodoro.data.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import chn.phm.pomodoro.data.datastore.serializer.PomodoroConfigSerializer.pomodoroConfigDataStore
import chn.phm.pomodoro.domain.model.PomodoroConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PomodoroDataStore {

    @Provides
    @Singleton
    fun provideAppConfigDataStore(@ApplicationContext context: Context): DataStore<PomodoroConfig> =
        context.pomodoroConfigDataStore

}