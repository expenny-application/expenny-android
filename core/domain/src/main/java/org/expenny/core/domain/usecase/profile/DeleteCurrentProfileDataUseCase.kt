package org.expenny.core.domain.usecase.profile

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.repository.ProfileRepository
import javax.inject.Inject

class DeleteCurrentProfileDataUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke() {
        localRepository.getCurrentProfileId().first()!!.also {
            profileRepository.deleteProfileData(it)
        }
    }
}