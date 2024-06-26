package org.expenny.feature.profilesetup.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyButton
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.feature.profilesetup.contract.ProfileSetupAction
import org.expenny.feature.profilesetup.contract.ProfileSetupState

@Composable
internal fun ProfileSetupContent(
    state: ProfileSetupState,
    scrollState: ScrollState,
    onAction: (ProfileSetupAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (state.showAbortDialog) {
        ProfileSetupAbortDialog(
            onConfirm = { onAction(ProfileSetupAction.OnAbortDialogConfirm) },
            onDismiss = { onAction(ProfileSetupAction.OnAbortDialogDismiss) }
        )
    }

    if (state.showConfirmationDialog) {
        ProfileSetupConfirmationDialog(
            onConfirm = { onAction(ProfileSetupAction.OnConfirmationDialogConfirm) },
            onDismiss = { onAction(ProfileSetupAction.OnConfirmationDialogDismiss) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clearFocusOnTapOutside(focusManager),
        topBar = {
            ProfileSetupToolbar(
                onBackClick = { onAction(ProfileSetupAction.OnBackClick) }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 24.dp,
                ),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.setup_profile_label),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.get_started_paragraph),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            ProfileSetupInputForm(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onNameChange = { onAction(ProfileSetupAction.OnNameChange(it)) },
                onAccountNameChange = { onAction(ProfileSetupAction.OnAccountNameChange(it)) },
                onAccountBalanceChange = { onAction(ProfileSetupAction.OnAccountBalanceChange(it)) },
                onSelectCurrencyClick = { onAction(ProfileSetupAction.OnSelectCurrencyUnitClick) },
                onSetupCashBalanceCheckboxChange = { onAction(ProfileSetupAction.OnSetupCashBalanceCheckboxChange(it)) }
            )
            ExpennyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(ProfileSetupAction.OnCtaClick)
                },
                isEnabled = state.isCtaEnabled,
                content = {
                    ButtonText(text = stringResource(R.string.continue_button))
                }
            )
        }
    }
}