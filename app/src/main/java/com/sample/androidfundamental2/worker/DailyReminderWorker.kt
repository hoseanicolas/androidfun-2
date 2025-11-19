package com.sample.androidfundamental2.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sample.androidfundamental2.MainActivity
import com.sample.androidfundamental2.R
import com.sample.androidfundamental2.data.remote.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class DailyReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.getEvents(active = -1, limit = 1)

                if (!response.error && response.listEvents.isNotEmpty()) {
                    val event = response.listEvents[0]
                    showNotification(event.name, event.beginTime)
                    Result.success()
                } else {
                    Result.failure()
                }
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    private fun showNotification(eventName: String, eventTime: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val formattedTime = try {
            val date = inputFormat.parse(eventTime)
            date?.let { outputFormat.format(it) } ?: eventTime
        } catch (e: Exception) {
            eventTime
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.outline_calendar_month_24)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText("$eventName - $formattedTime")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val WORK_NAME = "DailyReminderWorker"
        private const val CHANNEL_ID = "daily_reminder_channel"
        private const val CHANNEL_NAME = "Daily Reminder"
        private const val NOTIFICATION_ID = 1
    }
}
