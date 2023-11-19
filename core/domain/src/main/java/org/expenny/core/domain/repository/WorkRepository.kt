package org.expenny.core.domain.repository

interface WorkRepository {

    fun enqueueCurrencyRateSyncWork()

    fun cancelCurrencyRateSyncWork()
}