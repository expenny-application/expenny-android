package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.first
import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.domain.repository.AlarmRepository
import java.time.LocalTime
import javax.inject.Inject

class SetReminderTimePreferenceUseCase  @Inject constructor(
    private val preferences: ExpennyDataStore,
    private val alarmRepository: AlarmRepository
) {

    suspend operator fun invoke(time: LocalTime) {
        if (preferences.isReminderEnabled().first()) {
            preferences.setReminderTimeUtc(time)
            alarmRepository.setReminderAlarm(time)
        }
    }
}