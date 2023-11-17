package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.repository.WorkRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SetReminderPreferenceUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val workRepository: WorkRepository,
    private val getReminderTimeDelay: GetReminderTimeDelayInSecondsUseCase,
) {

    suspend operator fun invoke(isEnabled: Boolean) {
        if (isEnabled) {
            val reminderTime = localRepository.getReminderTime().first()
            val reminderDelay = getReminderTimeDelay(reminderTime)
            workRepository.enqueueReminderWork(reminderDelay, TimeUnit.SECONDS)
        } else {
            workRepository.cancelReminderWork()
        }
        localRepository.setReminderEnabled(isEnabled)
    }
}