package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.first
import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.domain.repository.AlarmRepository
import javax.inject.Inject

class SetReminderPreferenceUseCase @Inject constructor(
    private val preferences: ExpennyDataStore,
    private val alarmRepository: AlarmRepository,
) {

    suspend operator fun invoke(isEnabled: Boolean) {
        if (isEnabled) {
            val reminderTime = preferences.getReminderTime().first()
            alarmRepository.setReminderAlarm(reminderTime)
        } else {
            alarmRepository.cancelReminderAlarm()
        }
        preferences.setIsReminderEnabled(isEnabled)
    }
}