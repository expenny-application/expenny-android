package org.expenny.core.domain.usecase.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.LocalRepository
import javax.inject.Inject

class GetProfileSetUpUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {

    operator fun invoke(): Flow<Boolean> {
        return localRepository.getCurrentProfileId().map { it != null }
    }
}