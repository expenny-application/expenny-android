package org.expenny.core.domain.usecase.profile

import kotlinx.coroutines.flow.first
import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.domain.repository.ProfileRepository
import javax.inject.Inject

class DeleteCurrentProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val preferences: ExpennyDataStore,
) {

    suspend operator fun invoke() {
        preferences.getCurrentProfileId().first()!!.also {
            profileRepository.deleteProfile(it)
        }
    }
}