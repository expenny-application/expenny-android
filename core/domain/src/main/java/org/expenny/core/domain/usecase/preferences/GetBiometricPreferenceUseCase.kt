package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.Flow
import org.expenny.core.datastore.ExpennyDataStore
import javax.inject.Inject

class GetBiometricPreferenceUseCase @Inject constructor(
    private val preferences: ExpennyDataStore,
) {

    operator fun invoke(): Flow<Boolean> {
        return preferences.isBiometricEnrolled()
    }
}