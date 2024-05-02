package org.expenny.core.domain.usecase.preferences

import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.domain.repository.ProfileRepository
import javax.inject.Inject

class DeleteApplicationDataUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val preferences: ExpennyDataStore,
) {

    suspend operator fun invoke() {
        profileRepository.deleteAllProfiles()
        preferences.clear()
    }
}