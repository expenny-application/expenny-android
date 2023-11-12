package org.expenny.feature.passcode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.BiometricPromptState
import org.expenny.core.ui.components.ExpennyBiometricPrompt
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.components.ExpennyLogo
import org.expenny.feature.passcode.view.PasscodeFieldsRow
import org.expenny.feature.passcode.view.PasscodeKeyboard
import org.expenny.feature.passcode.view.PasscodeToolbar

@Composable
internal fun PasscodeContent(
    modifier: Modifier = Modifier,
    state: State,
    biometricPromptState: BiometricPromptState,
    onAction: (Action) -> Unit
) {
    ExpennyBiometricPrompt(
        state = biometricPromptState,
        title = stringResource(R.string.authentication_required_label),
        subtitle = stringResource(R.string.prove_identity_login_label),
        onAuthenticationSuccess = { onAction(Action.OnBiometricAuthenticationSuccess) },
        onAuthenticationError = { onAction(Action.OnBiometricAuthenticationError(it)) }
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (state.showToolbar) {
                PasscodeToolbar(onBackClick = { onAction(Action.OnBackClick) })
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 48.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (state.showLogo) {
                    ExpennyLogo()
                }
            }
            Box(contentAlignment = Alignment.Center) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PasscodeFieldsRow(
                        passcodeType = state.passcodeType,
                        passcodeValidationResult = state.passcodeValidationResult,
                        passcodeMaxLength = state.passcodeMaxLength,
                        passcodeLength = state.passcode.length
                    )
                    ExpennyText(
                        text = stringResource(state.passcodeParagraphResId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        align = TextAlign.Center,
                        maxLines = Int.MAX_VALUE
                    )
                }
            }
            Box(contentAlignment = Alignment.BottomCenter) {
                PasscodeKeyboard(
                    isBackspaceEnabled = state.isBackspaceEnabled,
                    isBiometricEnabled = state.isBiometricEnabled,
                    onDigitClick = { onAction(Action.OnDigitClick(it)) },
                    onBackspaceClick = { onAction(Action.OnBackspaceClick) },
                    onFingerprintClick = { onAction(Action.OnBiometricClick) }
                )
            }
        }
    }
}
