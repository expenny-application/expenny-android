package org.expenny.core.data.repository

import androidx.work.WorkManager
import org.expenny.core.domain.repository.WorkRepository
import javax.inject.Inject

class WorkRepositoryImpl @Inject constructor(
    private val workManager: WorkManager
): WorkRepository {

    override fun enqueueCurrencyRateSyncWork() {
        TODO("Not yet implemented")
    }

    override fun cancelCurrencyRateSyncWork() {
        TODO("Not yet implemented")
    }
}