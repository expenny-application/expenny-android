package org.expenny.core.domain.usecase.profile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.datastore.ExpennyDataStore
import javax.inject.Inject

class GetProfileSetUpUseCase @Inject constructor(
    private val preferences: ExpennyDataStore,
) {

    operator fun invoke(): Flow<Boolean> {
        return preferences.getCurrentProfileId().map { it != null }
    }
}