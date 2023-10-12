package chn.phm.pomodoro.system.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import chn.phm.pomodoro.R
import chn.phm.pomodoro.ui.PomodoroAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

class PomodoroService : Service() {
    private val binder = LocalBinder()
    private var remainingTime = 0
    private var soundId: Int = -1
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        remainingTime = intent?.getIntExtra("remainingTime", 0) ?: 0
        soundId = intent?.getIntExtra("sound_id", -1) ?: -1
        return START_STICKY
    }

    inner class LocalBinder : Binder() {
        fun getService(): PomodoroService = this@PomodoroService
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        val notification = buildNotification()
        startForeground(1, notification)

        serviceScope.launch {
            while (remainingTime >= 0) {
                delay(1000)
                remainingTime--
                updateNotification()
                if (remainingTime < 0) {
                    stopSelf()
                    playSound()
                }
            }
        }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, "TimeCounterChannel")
            .setContentTitle("Time Counting")
            .setContentText("Counter: $remainingTime")
            .setSmallIcon(R.drawable.ic_timer)
            .build()
    }

    private fun updateNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, "TimeCounterChannel")
            .setContentTitle("Time Counting")
            .setContentText("Counter: $remainingTime")
            .setSmallIcon(R.drawable.ic_timer)
            .build()
        notificationManager.notify(1, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "TimeCounterChannel",
                "Time Counting Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        EventBus.getDefault().post(PomodoroAction.CountingServiceDestroyed(remainingTime))
    }

    private fun playSound() {
        if (soundId != -1) {
            val mediaPlayer: MediaPlayer = MediaPlayer.create(this, soundId)
            mediaPlayer.setOnCompletionListener { it.release() }
            mediaPlayer.start()
        }
    }
}