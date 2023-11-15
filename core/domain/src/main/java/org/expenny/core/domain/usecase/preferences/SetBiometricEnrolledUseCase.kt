package org.expenny.core.domain.usecase.preferences

import org.expenny.core.domain.repository.BiometricRepository
import org.expenny.core.domain.repository.LocalRepository
import javax.inject.Inject

class SetBiometricEnrolledUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val biometricRepository: BiometricRepository
) {

    suspend operator fun invoke(isEnrolled: Boolean) {
        if (isEnrolled) {
            biometricRepository.generateSecretKey()
        } else {
            biometricRepository.clearSecretKey()
        }
        localRepository.setBiometricEnrolled(isEnrolled)
    }
}