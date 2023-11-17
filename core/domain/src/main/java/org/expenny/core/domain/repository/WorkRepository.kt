package org.expenny.core.domain.repository

import java.util.concurrent.TimeUnit

interface WorkRepository {

    fun enqueueReminderWork(delay: Long, unit: TimeUnit)

    fun cancelReminderWork()
}