package org.expenny.core.data.repository

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt.CryptoObject
import org.expenny.core.domain.repository.BiometricRepository
import org.expenny.core.model.biometric.BiometricStatus
import org.expenny.core.model.biometric.CryptoPurpose
import timber.log.Timber
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject
import kotlin.random.Random

class BiometricRepositoryImpl @Inject constructor(
    private val biometricManager: BiometricManager
): BiometricRepository {

    private val authenticators: Int = BIOMETRIC_STRONG
    private val secretKeyAlias = "expennySecretKeyAlias"
    private val keyStoreProvider = "AndroidKeyStore"

    override fun getBiometricStatus(): BiometricStatus {
        return when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricStatus.Ready
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricStatus.TemporaryNotAvailable
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricStatus.AvailableButNotEnrolled
            else -> BiometricStatus.NotAvailable
        }
    }

    override fun createCryptoObject(purpose: CryptoPurpose, iv: ByteArray?): CryptoObject {
        val cipher = getCipher()
        val secretKey = getSecretKey()
        if (purpose == CryptoPurpose.Decrypt) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        }
        return CryptoObject(cipher)
    }

    override fun generateSecretKey() {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, keyStoreProvider)
        val purposes = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        val params = KeyGenParameterSpec.Builder(secretKeyAlias, purposes)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            // Invalidate the keys if the user has registered a new biometric
            // credential, such as a new fingerprint
            .setInvalidatedByBiometricEnrollment(true)
            .build()

        keyGenerator.init(params)
        keyGenerator.generateKey()
    }

    override fun clearSecretKey() {
        if (isSecretKeyPresent()) {
            getKeystore().deleteEntry(secretKeyAlias)
        }
    }

    override fun isSecretKeyPresent(): Boolean{
        return getKeystore().isKeyEntry(secretKeyAlias)
    }

    private fun getSecretKey(): SecretKey {
        return getKeystore().getKey(secretKeyAlias, null) as SecretKey
    }

    private fun getKeystore(): KeyStore {
        // before the keystore can be accessed, it must be loaded
        return KeyStore.getInstance(keyStoreProvider).apply { load(null) }
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }
}