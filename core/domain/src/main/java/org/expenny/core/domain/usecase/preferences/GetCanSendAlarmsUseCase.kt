package org.expenny.core.domain.usecase.preferences

import org.expenny.core.domain.repository.AlarmRepository
import javax.inject.Inject

class GetCanSendAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {

    operator fun invoke(): Boolean {
        return alarmRepository.canScheduleAlarms()
    }
}