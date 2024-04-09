package com.example.lethireheisenbergcompose.ui.profile

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.lethireheisenbergcompose.MainActivity
import com.example.lethireheisenbergcompose.R
import kotlin.random.Random

class NotificationResolver(private val context: Context) {

    private val CHANNEL_ID = "my_channel_id"
    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel description"
                lightColor = Color.Blue.toArgb()
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, content: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.cook_hat_svgrepo_com)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return
        }
        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), notification.build())
        }
    }
}