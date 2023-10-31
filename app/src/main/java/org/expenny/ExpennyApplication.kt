package org.expenny

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ExpennyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        // ExpennyExceptionHandler.init(this, ExpennyExceptionActivity::class.java)

        Timber.plant(object : Timber.DebugTree() {
            /**
             * Override [createStackElementTag] to include a add a "method name" to the tag.
             */
            override fun createStackElementTag(element: StackTraceElement): String {
                return String.format(
                    "%s:%s",
                    element.methodName,
                    super.createStackElementTag(element)
                )
            }
        })
    }

    // https://developer.android.com/training/dependency-injection/hilt-jetpack#workmanager
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}