package ddns.net.muchserver.speedometer.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

const val CHANNEL_SPEEDOMETER = "speedometer_channel"
const val NAME_NOTIFICATION = "Speedometer Notification"

@HiltAndroidApp
class ApplicationSpeedometer: Application() {
    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            CHANNEL_SPEEDOMETER,
            NAME_NOTIFICATION, NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}