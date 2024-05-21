package org.expenny.core.domain.usecase.preferences

import org.expenny.core.datastore.ExpennyDataStore
import javax.inject.Inject

class SetPasscodePreferenceUseCase @Inject constructor(
    private val preferences: ExpennyDataStore
) {

    suspend operator fun invoke(passcode: String) {
        require(passcode.isNotBlank())
        preferences.setPasscode(passcode)
    }
}