package org.expenny.feature.categorydetails.view

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.feature.categorydetails.Action
import org.expenny.feature.categorydetails.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryDetailsContent(
    state: State,
    scrollState: ScrollState,
    nameInputFocusRequester: FocusRequester,
    onAction: (Action) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (state.showDeleteDialog) {
        CategoryDetailsDeleteDialog(
            onDismiss = { onAction(Action.OnDeleteRecordDialogDismiss) },
            onConfirm = { onAction(Action.OnDeleteRecordDialogConfirm) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clearFocusOnTapOutside(focusManager)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CategoryDetailsToolbar(
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
        CategoryDetailsInputForm(
            modifier = Modifier.padding(paddingValues),
            state = state,
            scrollState = scrollState,
            nameInputFocusRequester = nameInputFocusRequester,
            onNameChange = { onAction(Action.OnNameChange(it)) },
            onColorChange = { onAction(Action.OnColorChange(it)) },
            onIconChange = { onAction(Action.OnIconChange(it)) }
        )
    }
}