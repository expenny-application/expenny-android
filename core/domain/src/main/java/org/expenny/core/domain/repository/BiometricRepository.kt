package org.expenny.core.domain.repository

import androidx.biometric.BiometricPrompt.CryptoObject
import org.expenny.core.model.biometric.BiometricStatus
import org.expenny.core.model.biometric.CryptoPurpose

interface BiometricRepository {
    fun getBiometricStatus(): BiometricStatus
    fun isBiometricInvalidated(): Boolean
    fun createCryptoObject(purpose: CryptoPurpose, iv: ByteArray? = null): CryptoObject
    fun generateSecretKey()
    fun clearSecretKey()
}