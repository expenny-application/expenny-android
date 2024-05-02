package org.expenny.core.domain.usecase.preferences

import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.domain.repository.BiometricRepository
import javax.inject.Inject

class SetBiometricPreferenceUseCase @Inject constructor(
    private val preferences: ExpennyDataStore,
    private val biometricRepository: BiometricRepository
) {

    suspend operator fun invoke(isEnrolled: Boolean) {
        if (isEnrolled) {
            biometricRepository.generateSecretKey()
        } else {
            biometricRepository.clearSecretKey()
        }
        preferences.setIsBiometricEnrolled(isEnrolled)
    }
}