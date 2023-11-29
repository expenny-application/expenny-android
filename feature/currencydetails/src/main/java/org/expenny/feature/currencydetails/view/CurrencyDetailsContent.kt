package org.expenny.feature.currencydetails.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.components.ExpennyLoadingDialog
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.core.ui.extensions.floatingActionButtonPadding
import org.expenny.feature.currencydetails.Action
import org.expenny.feature.currencydetails.State

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun CurrencyDetailsContent(
    state: State,
    scrollState: ScrollState,
    onAction: (Action) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    if (state.showDeleteDialog) {
        CurrencyDetailsDeleteDialog(
            onConfirm = { onAction(Action.OnDeleteCurrencyDialogConfirm) },
            onDismiss = { onAction(Action.OnDeleteCurrencyDialogDismiss) }
        )
    }

    if (state.isLoading) {
        ExpennyLoadingDialog()
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
                showDeleteButton = state.showDeleteButton,
                onBackClick = { onAction(Action.OnBackClick) },
                onDeleteClick = { onAction(Action.OnDeleteClick) }
            )
        },
        floatingActionButton = {
            CurrencyDetailsActionButton(
                modifier = Modifier.navigationBarsPadding(),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(Action.OnSaveClick)
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        AccountDetailsInputForm(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .navigationBarsPadding()
                .floatingActionButtonPadding(),
            scrollState = scrollState,
            showRatesDisclaimerMessage = state.showRatesDisclaimerMessage,
            showRatesInputFields = state.showRatesInputFields,
            showEnableRatesUpdateCheckbox = state.showSubscribeToRatesUpdatesCheckbox,
            enableRatesUpdates = state.subscribeToRatesUpdates,
            currencyUnitInputField = state.currencyUnitInput,
            baseToQuoteRateInputField = state.baseToQuoteRateInput,
            quoteToBaseRateInputField = state.quoteToBaseRateInputField,
            baseCurrency = state.baseCurrency,
            quoteCurrency = state.quoteCurrency,
            onBaseToQuoteRateChange = { onAction(Action.OnBaseToQuoteRateChange(it)) },
            onQuoteToBaseRateChange = { onAction(Action.OnQuoteToBaseRateChange(it)) },
            onSelectCurrencyUnitClick = { onAction(Action.OnSelectCurrencyUnitClick) },
            onEnableRatesUpdateCheckboxChange = { onAction(Action.OnSubscribeToRatesUpdateCheckboxChange(it)) }
        )
    }
}