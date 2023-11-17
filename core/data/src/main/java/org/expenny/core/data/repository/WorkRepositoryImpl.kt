package org.expenny.core.data.repository

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import org.expenny.core.common.utils.Constants
import org.expenny.core.data.work.ReminderWorker
import org.expenny.core.domain.repository.WorkRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkRepositoryImpl @Inject constructor(
    private val workManager: WorkManager
): WorkRepository {

    override fun enqueueReminderWork(delay: Long, unit: TimeUnit) {
        val workBuilder = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)

        if (delay > 0) {
            workBuilder.setInitialDelay(delay, unit)
        }

        workManager.enqueueUniquePeriodicWork(
            Constants.REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workBuilder.build()
        )
    }

    override fun cancelReminderWork() {
        workManager.cancelUniqueWork(Constants.REMINDER_WORK_NAME)
    }
}