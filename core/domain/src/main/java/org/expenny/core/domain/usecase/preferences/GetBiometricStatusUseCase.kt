package org.expenny.core.domain.usecase.preferences

import org.expenny.core.domain.repository.BiometricRepository
import org.expenny.core.model.biometric.BiometricStatus
import javax.inject.Inject

class GetBiometricStatusUseCase @Inject constructor(
    private val biometricRepository: BiometricRepository
) {

    operator fun invoke(): BiometricStatus {
        return biometricRepository.getBiometricStatus()
    }
}