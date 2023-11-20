package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.repository.AlarmRepository
import org.expenny.core.domain.repository.WorkRepository
import javax.inject.Inject

class SetReminderPreferenceUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val alarmRepository: AlarmRepository,
) {

    suspend operator fun invoke(isEnabled: Boolean) {
        if (isEnabled) {
            val reminderTime = localRepository.getReminderTime().first()
            alarmRepository.setReminderAlarm(reminderTime)
        } else {
            alarmRepository.cancelReminderAlarm()
        }
        localRepository.setReminderEnabled(isEnabled)
    }
}