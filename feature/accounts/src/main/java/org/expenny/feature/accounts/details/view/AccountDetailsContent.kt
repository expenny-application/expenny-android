package org.expenny.feature.accounts.details.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyFab
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.feature.accounts.details.contract.AccountDetailsAction
import org.expenny.feature.accounts.details.contract.AccountDetailsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountDetailsContent(
    state: AccountDetailsState,
    scrollState: ScrollState,
    typesListState: LazyListState,
    nameInputFocusRequester: FocusRequester,
    onAction: (AccountDetailsAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (state.showDeleteDialog) {
        AccountDetailsDeleteDialog(
            onConfirm = { onAction(AccountDetailsAction.OnDeleteAccountDialogConfirm) },
            onDismiss = { onAction(AccountDetailsAction.OnDeleteAccountDialogDismiss) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clearFocusOnTapOutside(focusManager)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AccountDetailsToolbar(
                title = state.toolbarTitle.asRawString(),
                showDeleteButton = state.showDeleteButton,
                scrollBehavior = scrollBehavior,
                onBackClick = { onAction(AccountDetailsAction.OnBackClick) },
                onDeleteClick = { onAction(AccountDetailsAction.OnDeleteClick) }
            )
        },
        floatingActionButton = {
            ExpennyFab(
                modifier = Modifier.navigationBarsPadding(),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(AccountDetailsAction.OnSaveClick)
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
        AccountDetailsInputForm(
            modifier = Modifier.padding(paddingValues),
            state = state,
            scrollState = scrollState,
            typesListState = typesListState,
            nameInputFocusRequester = nameInputFocusRequester,
            onNameChange = { onAction(AccountDetailsAction.OnNameChange(it)) },
            onDescriptionChange = { onAction(AccountDetailsAction.OnDescriptionChange(it)) },
            onStartBalanceChange = { onAction(AccountDetailsAction.OnStartBalanceChange(it)) },
            onSelectCurrencyClick = { onAction(AccountDetailsAction.OnSelectCurrencyClick) },
            onTypeSelect = { onAction(AccountDetailsAction.OnTypeChange(it)) },
            onAdditionsSectionVisibilityChange = { onAction(AccountDetailsAction.OnAdditionsSectionVisibilityChange(it)) }
        )
    }
}