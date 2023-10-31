package org.expenny.feature.labeldetails.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.feature.labeldetails.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun LabelDetailsContent(
    state: State,
    scrollState: ScrollState,
    onAction: (Action) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    if (state.showDeleteDialog) {
        LabelDetailsDeleteDialog(
            onDismiss = { onAction(Action.OnDeleteDialogDismiss) },
            onConfirm = { onAction(Action.OnDeleteDialogConfirm) },
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clearFocusOnTapOutside(focusManager)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LabelDetailsToolbar(
                scrollBehavior = scrollBehavior,
                title = state.toolbarTitle.asRawString(),
                showDeleteButton = state.showDeleteButton,
                onBackClick = { onAction(Action.OnBackClick) },
                onDeleteClick = { onAction(Action.OnDeleteClick) }
            )
        },
        floatingActionButton = {
            LabelDetailsActionButton(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LabelDetailsInputForm(
                state = state,
                onNameChange = { onAction(Action.OnNameChange(it)) },
                onColorChange = { onAction(Action.OnColorChange(it)) },
                onGenerateRandomColorClick = { onAction(Action.OnGenerateRandomColorClick) }
            )
        }
    }
}