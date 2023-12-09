package org.expenny.feature.accountdetails.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
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
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.feature.accountdetails.Action
import org.expenny.feature.accountdetails.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountDetailsContent(
    state: State,
    scrollState: ScrollState,
    typesListState: LazyListState,
    nameInputFocusRequester: FocusRequester,
    onAction: (Action) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (state.showDeleteDialog) {
        AccountDetailsDeleteDialog(
            onConfirm = { onAction(Action.OnDeleteAccountDialogConfirm) },
            onDismiss = { onAction(Action.OnDeleteAccountDialogDismiss) }
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
                onBackClick = { onAction(Action.OnBackClick) },
                onDeleteClick = { onAction(Action.OnDeleteClick) }
            )
        },
        floatingActionButton = {
            AccountDetailsActionButton(
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
            modifier = Modifier.padding(paddingValues),
            state = state,
            scrollState = scrollState,
            typesListState = typesListState,
            nameInputFocusRequester = nameInputFocusRequester,
            onNameChange = { onAction(Action.OnNameChange(it)) },
            onDescriptionChange = { onAction(Action.OnDescriptionChange(it)) },
            onStartBalanceChange = { onAction(Action.OnStartBalanceChange(it)) },
            onSelectCurrencyClick = { onAction(Action.OnSelectCurrencyClick) },
            onTypeSelect = { onAction(Action.OnTypeChange(it)) },
            onAdditionsSectionVisibilityChange = { onAction(Action.OnAdditionsSectionVisibilityChange(it)) }
        )
    }
}