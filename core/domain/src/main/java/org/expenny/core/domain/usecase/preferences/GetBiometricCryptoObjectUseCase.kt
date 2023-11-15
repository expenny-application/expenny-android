package org.expenny.core.domain.usecase.preferences

import androidx.biometric.BiometricPrompt.CryptoObject
import org.expenny.core.domain.repository.BiometricRepository
import org.expenny.core.model.biometric.CryptoPurpose
import javax.inject.Inject

class GetBiometricCryptoObjectUseCase @Inject constructor(
    private val biometricRepository: BiometricRepository
) {

    operator fun invoke(purpose: CryptoPurpose): CryptoObject {
        if (!biometricRepository.isSecretKeyPresent()) {
            biometricRepository.generateSecretKey()
        }
        return biometricRepository.createCryptoObject(purpose)
    }
}