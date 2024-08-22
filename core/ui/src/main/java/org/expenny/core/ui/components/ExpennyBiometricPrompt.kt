package org.expenny.core.ui.components

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R

@Composable
fun ExpennyBiometricPrompt(
    state: BiometricPromptState,
    title: String,
    subtitle: String,
    onAuthenticationSuccess: () -> Unit = {},
    onAuthenticationError: (error: String) -> Unit = {}
) {
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setSubtitle(subtitle)
        .setNegativeButtonText(stringResource(R.string.cancel_button))
        .build()

    val context = LocalContext.current as AppCompatActivity

    val promptCallback = remember(state) {
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errorString: CharSequence) {
                if (errorCode !in biometricsErrorsToIgnore) {
                    onAuthenticationError(errorString.toString())
                }
                state.hidePrompt()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                state.hidePrompt()
                onAuthenticationSuccess()
            }
        }
    }

    if (state.showPrompt.value) {
        LaunchedEffect(state.cryptoObject) {
            val prompt = BiometricPrompt(context, promptCallback)
            prompt.authenticate(promptInfo, state.cryptoObject)
        }
    }
}

@Composable
fun rememberBiometricPromptState() = remember { BiometricPromptState() }

class BiometricPromptState {
    private lateinit var _cryptoObject: CryptoObject
    private val _showPrompt = mutableStateOf(false)
    val showPrompt: State<Boolean> = _showPrompt
    val cryptoObject: CryptoObject by lazy { _cryptoObject }

    fun showPrompt(cryptoObject: CryptoObject) {
        _cryptoObject = cryptoObject
        _showPrompt.value = true
    }

    fun hidePrompt() {
        _showPrompt.value = false
    }
}

private val biometricsErrorsToIgnore = listOf(
    BiometricPrompt.ERROR_NEGATIVE_BUTTON,
    BiometricPrompt.ERROR_CANCELED,
    BiometricPrompt.ERROR_USER_CANCELED,
)