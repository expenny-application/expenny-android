package org.expenny.core.domain.usecase.preferences

import org.expenny.core.domain.repository.LocalRepository
import javax.inject.Inject

class SetPasscodePreferenceUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    suspend operator fun invoke(passcode: String) {
        require(passcode.isNotBlank())
        localRepository.setPasscode(passcode)
    }
}