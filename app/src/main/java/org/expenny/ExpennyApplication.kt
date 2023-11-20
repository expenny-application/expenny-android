package org.expenny

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import org.expenny.core.common.utils.Constants
import org.expenny.core.common.utils.Constants.REMINDER_NOTIFICATION_CHANNEL_ID
import timber.log.Timber
import javax.inject.Inject
import org.expenny.core.resources.R

@HiltAndroidApp
class ExpennyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

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

    // https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun registerNotificationChannels() {
        val name = getString(R.string.reminder_channel_name)
        val description = getString(R.string.reminder_channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(REMINDER_NOTIFICATION_CHANNEL_ID, name, importance).apply {
            this.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            this.description = description
            this.enableVibration(true)
            this.enableLights(true)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}