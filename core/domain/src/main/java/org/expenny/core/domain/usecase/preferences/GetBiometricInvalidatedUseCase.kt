package org.expenny.core.domain.usecase.preferences

import android.security.keystore.KeyPermanentlyInvalidatedException
import org.expenny.core.domain.repository.BiometricRepository
import org.expenny.core.model.biometric.CryptoPurpose
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class GetBiometricInvalidatedUseCase @Inject constructor(
    private val biometricRepository: BiometricRepository
) {

    operator fun invoke(): Boolean {
        // To check if any new biometric has been enrolled or removed since last creation of secretKeyAlias
        // create a cipher with that key and try to init the cipher.
        // The init call should trigger a KeyPermanentlyInvalidatedException.
        return if (biometricRepository.isSecretKeyPresent()) {
            try {
                val bytes = ByteArray(16)
                Random.nextBytes(bytes)
                biometricRepository.createCryptoObject(CryptoPurpose.Decrypt, bytes)
                false
            } catch (e: KeyPermanentlyInvalidatedException) {
                Timber.i(e)
                true
            }
        } else {
            false
        }
    }
}