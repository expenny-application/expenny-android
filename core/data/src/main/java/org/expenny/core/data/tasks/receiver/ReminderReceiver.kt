package org.expenny.core.data.tasks.receiver

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import org.expenny.core.common.utils.Constants.PRIMARY_COLOR_ARGB
import org.expenny.core.common.utils.Constants.REMINDER_NOTIFICATION_CHANNEL_ID
import org.expenny.core.common.utils.Constants.REMINDER_NOTIFICATION_ID
import org.expenny.core.resources.R
import timber.log.Timber

class ReminderReceiver: BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent) {
        if (context != null) {
            Timber.i("ReminderReceiver has received the intent: ${intent.action}")

            val activityIntent = Intent(context, Class.forName("org.expenny.main.MainActivity"))
            // Set flags to start the activity or bring it to the front if already running
            activityIntent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT

            val pendingIntentFlags = PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            val pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, pendingIntentFlags)

            val builder = NotificationCompat.Builder(context, REMINDER_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setColor(PRIMARY_COLOR_ARGB.toInt())
                .setContentTitle(context.getString(R.string.reminder_notification_title))
                .setContentText(context.getString(R.string.reminder_notification_body))
                .setStyle(NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            // Using fixed notification id to replace old notification
            NotificationManagerCompat
                .from(context)
                .notify(REMINDER_NOTIFICATION_ID, builder.build())

            Timber.i("ReminderReceiver has sent the notification")
        }
    }
}