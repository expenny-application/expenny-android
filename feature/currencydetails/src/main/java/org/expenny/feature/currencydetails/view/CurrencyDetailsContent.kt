package org.expenny.feature.currencydetails.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.components.ExpennyLoadingDialog
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.core.ui.extensions.floatingActionButtonPadding
import org.expenny.feature.currencydetails.Action
import org.expenny.feature.currencydetails.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrencyDetailsContent(
    state: State,
    scrollState: ScrollState,
    onAction: (Action) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    when(state.dialog) {
        is State.Dialog.InfoDialog -> {
            CurrencyDetailsInfoDialog(
                onDismiss = { onAction(Action.OnDialogDismiss) }
            )
        }
        is State.Dialog.DeleteDialog -> {
            CurrencyDetailsDeleteDialog(
                onConfirm = { onAction(Action.OnDeleteCurrencyDialogConfirm) },
                onDismiss = { onAction(Action.OnDialogDismiss) }
            )
        }
        is State.Dialog.LoadingDialog -> {
            ExpennyLoadingDialog()
        }
        else -> {}
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clearFocusOnTapOutside(focusManager)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CurrencyDetailsToolbar(
                scrollBehavior = scrollBehavior,
                title = state.toolbarTitle.asRawString(),
                showInfoButton = state.showInfoButton,
                showDeleteButton = state.showDeleteButton,
                onBackClick = { onAction(Action.OnBackClick) },
                onInfoClick = { onAction(Action.OnInfoClick) },
                onDeleteClick = { onAction(Action.OnDeleteClick) }
            )
        },
        floatingActionButton = {
            CurrencyDetailsActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onSaveClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(Action.OnSaveClick)
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        CurrencyDetailsInputForm(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .navigationBarsPadding()
                .floatingActionButtonPadding(),
            scrollState = scrollState,
            state = state,
            onBaseToQuoteRateChange = { onAction(Action.OnBaseToQuoteRateChange(it)) },
            onQuoteToBaseRateChange = { onAction(Action.OnQuoteToBaseRateChange(it)) },
            onSelectCurrencyUnitClick = { onAction(Action.OnSelectCurrencyUnitClick) },
            onSubscribeToUpdatesCheckboxChange = { onAction(Action.OnSubscribeToUpdatesCheckboxChange(it)) },
            onUpdateClick = { onAction(Action.OnUpdateClick) }
        )
    }
}