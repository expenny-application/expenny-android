package org.expenny.feature.categories.details.view

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyFab
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.feature.categories.details.contract.CategoryDetailsAction
import org.expenny.feature.categories.details.contract.CategoryDetailsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoryDetailsContent(
    state: CategoryDetailsState,
    scrollState: ScrollState,
    nameInputFocusRequester: FocusRequester,
    onAction: (CategoryDetailsAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (state.showDeleteDialog) {
        CategoryDetailsDeleteDialog(
            onDismiss = { onAction(CategoryDetailsAction.OnDeleteRecordDialogDismiss) },
            onConfirm = { onAction(CategoryDetailsAction.OnDeleteRecordDialogConfirm) }
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
                onBackClick = { onAction(CategoryDetailsAction.OnBackClick) },
                onDeleteClick = { onAction(CategoryDetailsAction.OnDeleteClick) }
            )
        },
        floatingActionButton = {
            ExpennyFab(
                modifier = Modifier.navigationBarsPadding(),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(CategoryDetailsAction.OnSaveClick)
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
        CategoryDetailsInputForm(
            modifier = Modifier.padding(paddingValues),
            state = state,
            scrollState = scrollState,
            nameInputFocusRequester = nameInputFocusRequester,
            onNameChange = { onAction(CategoryDetailsAction.OnNameChange(it)) },
            onColorChange = { onAction(CategoryDetailsAction.OnColorChange(it)) },
            onIconChange = { onAction(CategoryDetailsAction.OnIconChange(it)) }
        )
    }
}