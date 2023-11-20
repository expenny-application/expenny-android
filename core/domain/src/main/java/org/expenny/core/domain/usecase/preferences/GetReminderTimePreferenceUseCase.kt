package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.LocalRepository
import java.time.LocalTime
import javax.inject.Inject

class GetReminderTimePreferenceUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    operator fun invoke(): Flow<LocalTime> {
        return localRepository.getReminderTime()
    }
}