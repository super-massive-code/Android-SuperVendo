package com.supermassivecode.supervendo.sensors

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.supermassivecode.supervendo.R

class LocationTrackingService : Service() {

    private lateinit var recorder: LocationRecorder

    override fun onCreate() {
        super.onCreate()
        recorder = LocationRecorder(applicationContext)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        recorder.startTracking()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        recorder.stopTracking()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        val channelId = "location_channel"

        // Only create the channel once
        val channel = NotificationChannel(
            channelId,
            "Location Tracking",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("SuperVendo is tracking location")
            .setContentText("Location tracking in progress")
            .setSmallIcon(R.drawable.ic_location)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }
}
