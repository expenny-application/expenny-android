package org.expenny.feature.currencies.details.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyFab
import org.expenny.core.ui.components.ExpennyLoadingDialog
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.core.ui.extensions.floatingActionButtonPadding
import org.expenny.feature.currencies.details.contract.CurrencyDetailsAction
import org.expenny.feature.currencies.details.contract.CurrencyDetailsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CurrencyDetailsContent(
    state: CurrencyDetailsState,
    scrollState: ScrollState,
    onAction: (CurrencyDetailsAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    when(state.dialog) {
        is CurrencyDetailsState.Dialog.InfoDialog -> {
            CurrencyDetailsInfoDialog(
                onDismiss = { onAction(CurrencyDetailsAction.OnDialogDismiss) }
            )
        }
        is CurrencyDetailsState.Dialog.DeleteDialog -> {
            CurrencyDetailsDeleteDialog(
                onConfirm = { onAction(CurrencyDetailsAction.OnDeleteCurrencyDialogConfirm) },
                onDismiss = { onAction(CurrencyDetailsAction.OnDialogDismiss) }
            )
        }
        is CurrencyDetailsState.Dialog.LoadingDialog -> {
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
                onBackClick = { onAction(CurrencyDetailsAction.OnBackClick) },
                onInfoClick = { onAction(CurrencyDetailsAction.OnInfoClick) },
                onDeleteClick = { onAction(CurrencyDetailsAction.OnDeleteClick) }
            )
        },
        floatingActionButton = {
            ExpennyFab(
                modifier = Modifier.navigationBarsPadding(),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(CurrencyDetailsAction.OnSaveClick)
                },
                icon = {
                    FabIcon(painter = painterResource(R.drawable.ic_check))
                },
                label = {
                    FabText(text = stringResource(R.string.save_button))
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
            onBaseToQuoteRateChange = { onAction(CurrencyDetailsAction.OnBaseToQuoteRateChange(it)) },
            onQuoteToBaseRateChange = { onAction(CurrencyDetailsAction.OnQuoteToBaseRateChange(it)) },
            onSelectCurrencyUnitClick = { onAction(CurrencyDetailsAction.OnSelectCurrencyUnitClick) },
            onSubscribeToUpdatesCheckboxChange = { onAction(CurrencyDetailsAction.OnSubscribeToUpdatesCheckboxChange(it)) },
            onUpdateClick = { onAction(CurrencyDetailsAction.OnUpdateClick) }
        )
    }
}