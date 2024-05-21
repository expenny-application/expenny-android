package org.expenny.core.domain.usecase.profile

import org.expenny.core.datastore.ExpennyDataStore
import javax.inject.Inject

class SetCurrentProfileUseCase @Inject constructor(
    private val preferences: ExpennyDataStore
) {

    suspend operator fun invoke(params: Params) {
        preferences.setCurrentProfileId(params.profileId)
    }

    data class Params(
        val profileId: Long
    )
}