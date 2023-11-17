package org.expenny.core.data.work

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import org.expenny.core.common.utils.Constants
import org.expenny.core.common.utils.Constants.REMINDER_NOTIFICATION_CHANNEL_ID
import org.expenny.core.common.utils.Constants.REMINDER_NOTIFICATION_ID
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.resources.R
import kotlin.random.Random

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val localRepository: LocalRepository
) : CoroutineWorker(context, workerParams) {

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        // double check because the user can switch off notifications in settings
        // after it has been scheduled to show for the next day
        if (isReminderEnabled()) {
            val intent = Intent(applicationContext, Class.forName("org.expenny.main.MainActivity"))
            // set flags to start the activity or bring it to the front if already running
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT

            val pendingIntentFlags = FLAG_CANCEL_CURRENT or FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, pendingIntentFlags)

            val builder = NotificationCompat.Builder(applicationContext, REMINDER_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(context.getString(R.string.reminder_notification_title))
                .setContentText(context.getString(R.string.reminder_notification_body))
                .setStyle(NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            // using fixed notification id to replace old notification
            NotificationManagerCompat
                .from(applicationContext)
                .notify(REMINDER_NOTIFICATION_ID, builder.build())
        }

        return Result.success()
    }

    private suspend fun isReminderEnabled(): Boolean {
        return localRepository.getReminderEnabled().first()
    }
}