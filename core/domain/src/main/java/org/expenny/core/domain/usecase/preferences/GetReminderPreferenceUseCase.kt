package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.LocalRepository
import javax.inject.Inject

class GetReminderPreferenceUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    operator fun invoke(): Flow<Boolean> {
        return localRepository.isReminderEnabled()
    }
}