package org.expenny.core.domain.usecase.preferences

import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.repository.ProfileRepository
import javax.inject.Inject

class DeleteApplicationDataUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() {
        profileRepository.deleteAllProfiles()
        localRepository.clear()
    }
}