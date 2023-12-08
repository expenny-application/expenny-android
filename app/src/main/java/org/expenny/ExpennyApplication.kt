package org.expenny

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import org.expenny.core.common.utils.Constants.REMINDER_NOTIFICATION_CHANNEL_ID
import timber.log.Timber
import javax.inject.Inject
import org.expenny.core.resources.R

@HiltAndroidApp
class ExpennyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    // https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        // ExpennyExceptionHandler.init(this, ExpennyExceptionActivity::class.java)

        Timber.plant(object : Timber.DebugTree() {
            // override createStackElementTag to include a add a "method name" to the tag.
            override fun createStackElementTag(element: StackTraceElement): String {
                return String.format(
                    "%s:%s",
                    element.methodName,
                    super.createStackElementTag(element)
                )
            }
        })

        registerNotificationChannels()
    }

    private fun registerNotificationChannels() {
        val reminderChannel = NotificationChannel(
            REMINDER_NOTIFICATION_CHANNEL_ID,
            getString(R.string.reminder_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            this.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            this.description = getString(R.string.reminder_channel_description)
            this.enableVibration(true)
            this.enableLights(true)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(reminderChannel)
    }
}