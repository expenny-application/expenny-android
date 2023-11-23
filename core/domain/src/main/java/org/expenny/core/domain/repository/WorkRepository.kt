package org.expenny.core.domain.repository

interface WorkRepository {
    fun enqueueCurrencySyncWork()
    fun cancelCurrencySyncWork()
    fun isCurrencySyncWorkEnqueued(): Boolean
}