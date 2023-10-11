package chn.phm.pomodoro

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PomodoroApplication : Application(), ImageLoaderFactory, LifecycleObserver {
    override fun onCreate() {
        super.onCreate()
        PomodoroLifecycleObserver.observeAppLifecycle(ProcessLifecycleOwner.get().lifecycle)
    }

    /**
     * Create the singleton [ImageLoader].
     * This is used to display background image in the app.
     */
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .crossfade(true)
            .allowHardware(false)
            .build()
    }

}