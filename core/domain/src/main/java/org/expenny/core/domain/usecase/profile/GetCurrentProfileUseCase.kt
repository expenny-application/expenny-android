package org.expenny.core.domain.usecase.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.domain.repository.ProfileRepository
import org.expenny.core.model.profile.Profile
import javax.inject.Inject

class GetCurrentProfileUseCase @Inject constructor(
    private val preferences: ExpennyDataStore,
    private val profileRepository: ProfileRepository,
) {

    suspend operator fun invoke(): Flow<Profile?> {
        return preferences.getCurrentProfileId().map { id ->
            id?.let { profileRepository.getProfile(it) }
        }
    }
}