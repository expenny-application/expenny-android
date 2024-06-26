package org.expenny.feature.passcode.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.BiometricPromptState
import org.expenny.core.ui.components.ExpennyBiometricPrompt
import org.expenny.core.ui.components.ExpennyLogo
import org.expenny.feature.passcode.contract.PasscodeAction
import org.expenny.feature.passcode.contract.PasscodeState

@Composable
internal fun PasscodeContent(
    modifier: Modifier = Modifier,
    state: PasscodeState,
    biometricPromptState: BiometricPromptState,
    onAction: (PasscodeAction) -> Unit
) {
    ExpennyBiometricPrompt(
        state = biometricPromptState,
        title = stringResource(R.string.authentication_required_label),
        subtitle = stringResource(R.string.prove_identity_login_label),
        onAuthenticationSuccess = { onAction(PasscodeAction.OnBiometricAuthenticationSuccess) },
        onAuthenticationError = { onAction(PasscodeAction.OnBiometricAuthenticationError(it)) }
    )

    if (state.showInfoDialog) {
        PasscodeInfoDialog(
            onDismiss = { onAction(PasscodeAction.OnInfoDialogDismiss) }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (state.showToolbar) {
                PasscodeToolbar(
                    onInfoClick = { onAction(PasscodeAction.OnInfoClick) },
                    onBackClick = { onAction(PasscodeAction.OnBackClick) }
                )
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
                    PasscodeInputsRow(
                        passcodeType = state.passcodeType,
                        passcodeValidationResult = state.passcodeValidationResult,
                        passcodeMaxLength = state.passcodeMaxLength,
                        passcodeLength = state.passcode.length
                    )
                    Text(
                        text = stringResource(state.passcodeParagraphResId),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Box(contentAlignment = Alignment.BottomCenter) {
                PasscodeKeyboard(
                    isBackspaceEnabled = state.isBackspaceEnabled,
                    isBiometricEnabled = state.isBiometricEnabled,
                    onDigitClick = { onAction(PasscodeAction.OnDigitClick(it)) },
                    onBackspaceClick = { onAction(PasscodeAction.OnBackspaceClick) },
                    onFingerprintClick = { onAction(PasscodeAction.OnBiometricClick) }
                )
            }
        }
    }
}
