package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.repository.WorkRepository
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SetReminderTimePreferenceUseCase  @Inject constructor(
    private val localRepository: LocalRepository,
    private val workRepository: WorkRepository,
    private val getReminderTimeDelay: GetReminderTimeDelayInSecondsUseCase,
) {

    suspend operator fun invoke(time: LocalTime) {
        if (localRepository.getReminderEnabled().first()) {
            localRepository.setReminderTime(time)
            workRepository.enqueueReminderWork(getReminderTimeDelay(time), TimeUnit.SECONDS)
        }
    }
}