package org.expenny.core.domain.usecase.profile

import org.expenny.core.domain.repository.LocalRepository
import javax.inject.Inject

class SwitchProfileUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {

    suspend operator fun invoke(params: Params) {
        localRepository.setCurrentProfile(params.profileId)
    }

    data class Params(
        val profileId: Long
    )
}