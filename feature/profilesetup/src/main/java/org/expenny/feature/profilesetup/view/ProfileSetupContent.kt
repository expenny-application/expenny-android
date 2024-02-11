package org.expenny.feature.profilesetup.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.feature.profilesetup.Action
import org.expenny.feature.profilesetup.State

@Composable
internal fun ProfileSetupContent(
    state: State,
    scrollState: ScrollState,
    onAction: (Action) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (state.showAbortDialog) {
        ProfileSetupAbortDialog(
            onConfirm = { onAction(Action.OnAbortDialogConfirm) },
            onDismiss = { onAction(Action.OnAbortDialogDismiss) }
        )
    }

    if (state.showConfirmationDialog) {
        ProfileSetupConfirmationDialog(
            onConfirm = { onAction(Action.OnConfirmationDialogConfirm) },
            onDismiss = { onAction(Action.OnConfirmationDialogDismiss) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clearFocusOnTapOutside(focusManager),
        topBar = {
            ProfileSetupToolbar(
                onBackClick = { onAction(Action.OnBackClick) }
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
                    bottom = 24.dp,
                ),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileSetupCaption(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )
            ProfileSetupInputForm(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onNameChange = { onAction(Action.OnNameChange(it)) },
                onAccountNameChange = { onAction(Action.OnAccountNameChange(it)) },
                onAccountBalanceChange = { onAction(Action.OnAccountBalanceChange(it)) },
                onSelectCurrencyClick = { onAction(Action.OnSelectCurrencyUnitClick) },
                onSetupCashBalanceCheckBoxChange = { onAction(Action.OnSetupCashBalanceCheckBoxChange(it)) }
            )
            ProfileSetupCta(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                isEnabled = state.isCtaEnabled,
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(Action.OnCtaClick)
                }
            )
        }
    }
}