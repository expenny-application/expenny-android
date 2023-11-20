package org.expenny.core.data.repository

import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_NO_CREATE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED
import android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED
import android.content.pm.PackageManager.DONT_KILL_APP
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import org.expenny.core.data.tasks.receiver.BootReceiver
import org.expenny.core.data.tasks.receiver.ReminderReceiver
import org.expenny.core.domain.repository.AlarmRepository
import timber.log.Timber
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject


class AlarmRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmManager: AlarmManager
): AlarmRepository {

    private val reminderRequestCode = 0

    override fun setReminderAlarm(time: LocalTime) {
        val pendingIntent = Intent(context, ReminderReceiver::class.java).let {
            PendingIntent.getBroadcast(context, reminderRequestCode, it, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)
        }
        val today = LocalDateTime.now()
        val startDateTime = LocalDateTime.now()
            .withHour(time.hour)
            .withMinute(time.minute)
            .withSecond(time.second)
            .withNano(time.nano)
            .let {
                // If the reminder time has already passed for today, set it for the next day
                if (it.isBefore(today)) it.plusDays(1) else it
            }

        val zonedDateTime = startDateTime.atZone(ZoneId.systemDefault())
        val nextReminderDateTimeMillis = zonedDateTime.toInstant().toEpochMilli()

        alarmManager.setRepeating(RTC_WAKEUP, nextReminderDateTimeMillis, INTERVAL_DAY, pendingIntent)
        configureBootReceiver(true)

        Timber.i("ReminderReceiver has been scheduled to start at $zonedDateTime")
    }

    override fun cancelReminderAlarm() {
        val pendingIntent = Intent(context, ReminderReceiver::class.java).let {
            PendingIntent.getBroadcast(context, reminderRequestCode, it, FLAG_NO_CREATE or FLAG_IMMUTABLE)
        }
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            Timber.i("ReminderReceiver has been canceled")
        }
        configureBootReceiver(false)
    }

    override fun canScheduleAlarms(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return alarmManager.canScheduleExactAlarms()
        }
        return true
    }

    private fun configureBootReceiver(shouldEnable: Boolean) {
        val name = ComponentName(context, BootReceiver::class.java)
        val currentSetting = context.packageManager.getComponentEnabledSetting(name)
        val newSetting = if (shouldEnable) COMPONENT_ENABLED_STATE_ENABLED else COMPONENT_ENABLED_STATE_DISABLED

        if (currentSetting != newSetting) {
            context.packageManager.setComponentEnabledSetting(name, newSetting, DONT_KILL_APP)
            Timber.i("BootReceiver enabled setting was changed to $shouldEnable")
        }
    }
}