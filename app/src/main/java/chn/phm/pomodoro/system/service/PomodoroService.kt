package chn.phm.pomodoro.system.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import chn.phm.pomodoro.PomodoroActivity
import chn.phm.pomodoro.R
import chn.phm.pomodoro.ui.PomodoroAction
import chn.phm.pomodoro.utils.PomodoroHelper.convertToMinuteFormat
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

    /**
     * Builder of the current notification
     */
    private lateinit var currentNotification: NotificationCompat.Builder

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        remainingTime = intent?.getIntExtra("remaining_time", 0) ?: 0
        soundId = intent?.getIntExtra("sound_id", -1) ?: -1
        return START_STICKY
    }

    inner class LocalBinder : Binder() {
        fun getService(): PomodoroService = this@PomodoroService
    }

    override fun onCreate() {
        super.onCreate()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val intent = Intent(this, PomodoroActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        currentNotification = NotificationCompat.Builder(this, "TimeCounterChannel")
            .setContentTitle("Time Counting")
            .setSmallIcon(R.drawable.ic_pomodoro)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        startForeground(1, currentNotification.build())

        serviceScope.launch {
            while (remainingTime >= 0) {
                delay(1000)
                remainingTime--
                updateNotification(notificationManager)
                if (remainingTime < 0) {
                    stopSelf()
                    playSound()
                }
            }
        }
    }

    private fun updateNotification(notificationManager: NotificationManager) {
        val notification = currentNotification
            .setContentText("Counter: ${remainingTime.convertToMinuteFormat()}")
        notificationManager.notify(1, notification.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "TimeCounterChannel",
                "Time Counting Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(serviceChannel)
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