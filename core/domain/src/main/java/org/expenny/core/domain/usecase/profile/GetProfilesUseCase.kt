package org.expenny.core.domain.usecase.profile

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.ProfileRepository
import org.expenny.core.model.profile.Profile
import javax.inject.Inject

class GetProfilesUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {

    operator fun invoke(): Flow<List<Profile>> {
        return profileRepository.getProfiles()
    }
}