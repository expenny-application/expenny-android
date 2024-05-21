package org.expenny.core.domain.usecase.preferences

import org.expenny.core.datastore.ExpennyDataStore
import javax.inject.Inject

class DeletePasscodePreferenceUseCase @Inject constructor(
    private val preferences: ExpennyDataStore
) {

    suspend operator fun invoke() {
        preferences.setPasscode(null)
    }
}