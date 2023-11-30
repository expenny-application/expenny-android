package org.expenny.core.data.repository

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import org.expenny.core.common.utils.Constants
import org.expenny.core.data.tasks.worker.CurrencySyncWorker
import org.expenny.core.domain.repository.WorkRepository
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class WorkRepositoryImpl @Inject constructor(
    private val workManager: WorkManager
): WorkRepository {

    override fun enqueueCurrencySyncWork() {
        val centralEuropeanTime = ZoneId.of("Europe/Warsaw")
        val startTime = LocalTime.of(18, 0, 0, 0)
        val zonedStartTime = startTime.atDate(LocalDate.now()).atZone(centralEuropeanTime)
        val now = LocalDateTime.now().atZone(centralEuropeanTime)

        val initialDelaySec = if (zonedStartTime.isAfter(now)) {
            Duration.between(now, zonedStartTime).toSeconds()
        } else {
            Duration.between(now, zonedStartTime.plusDays(1)).toSeconds()
        }

        PeriodicWorkRequestBuilder<CurrencySyncWorker>(24, TimeUnit.HOURS).apply {
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            setConstraints(workConstraints)

            if (initialDelaySec > 0) {
                setInitialDelay(initialDelaySec, TimeUnit.SECONDS)
            }

            workManager.enqueueUniquePeriodicWork(
                Constants.CURRENCY_SYNC_WORK_NAME,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                this.build()
            )
        }
    }

    override fun cancelCurrencySyncWork() {
        workManager.cancelUniqueWork(Constants.CURRENCY_SYNC_WORK_NAME)
    }

    override fun isCurrencySyncWorkEnqueued(): Boolean {
        return workManager.getWorkInfosForUniqueWork(Constants.CURRENCY_SYNC_WORK_NAME).get().any {
            it.state == WorkInfo.State.RUNNING || it.state === WorkInfo.State.ENQUEUED
        }
    }
}