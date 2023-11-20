package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.repository.AlarmRepository
import org.expenny.core.domain.repository.WorkRepository
import java.time.LocalTime
import javax.inject.Inject

class SetReminderTimePreferenceUseCase  @Inject constructor(
    private val localRepository: LocalRepository,
    private val alarmRepository: AlarmRepository
) {

    suspend operator fun invoke(time: LocalTime) {
        if (localRepository.getReminderEnabled().first()) {
            localRepository.setReminderTime(time)
            alarmRepository.setReminderAlarm(time)
        }
    }
}