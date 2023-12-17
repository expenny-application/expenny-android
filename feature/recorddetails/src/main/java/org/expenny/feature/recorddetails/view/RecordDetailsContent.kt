package org.expenny.feature.recorddetails.view

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import org.expenny.core.common.extensions.toLocalDate
import org.expenny.core.common.extensions.toLocalTime
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.components.ExpennyDatePicker
import org.expenny.core.ui.components.ExpennyTimePicker
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.core.ui.extensions.floatingActionButtonPadding
import org.expenny.feature.recorddetails.Action
import org.expenny.feature.recorddetails.State

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordDetailsContent(
    state: State,
    scrollState: ScrollState,
    amountInputFocusRequester: FocusRequester,
    onAction: (Action) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    if (state.showDatePicker) {
        ExpennyDatePicker(
            currentDate = state.dateInput.value.toLocalDate(),
            onSelect = { onAction(Action.OnDateChange(it)) },
            onDismiss = { onAction(Action.OnDialogDismiss) }
        )
    }

    if (state.showTimePicker) {
        ExpennyTimePicker(
            currentTime = state.timeInput.value.toLocalTime(),
            onSelect = { onAction(Action.OnTimeChange(it)) },
            onDismiss = { onAction(Action.OnDialogDismiss) }
        )
    }

    if (state.showDeleteDialog) {
        RecordDetailsDeleteDialog(
            onConfirm = { onAction(Action.OnDeleteRecordDialogConfirm) },
            onDismiss = { onAction(Action.OnDialogDismiss) }
        )
    }

    if (state.showAmountConversionDialog) {
        RecordDetailsAmountConversionDialog(
            onDismiss = { onAction(Action.OnDialogDismiss) }
        )
    }

    if (state.showResetTransferDialog) {
        RecordDetailsChangeTypeDialog(
            onConfirm = { onAction(Action.OnResetTransferDialogConfirm) },
            onDismiss = { onAction(Action.OnDialogDismiss) }
        )
    }

    if (state.showReceiptSourceDialog) {
        RecordDetailsReceiptSourceDialog(
            onGalleryClick = { onAction(Action.OnReceiptSourceDialogGallerySelect) },
            onCameraClick = { onAction(Action.OnReceiptSourceDialogCameraSelect) },
            onDismiss = { onAction(Action.OnDialogDismiss) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clearFocusOnTapOutside(focusManager)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            RecordDetailsToolbar(
                title = state.toolbarTitle.asRawString(),
                scrollBehavior = scrollBehavior,
                showDeleteButton = state.showDeleteButton,
                onBackClick = { onAction(Action.OnBackClick) },
                onDeleteClick = { onAction(Action.OnDeleteClick) }
            )
        },
        floatingActionButton = {
            RecordDetailsActionButton(
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
                .padding(16.dp)
                .navigationBarsPadding()
                .floatingActionButtonPadding(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                RecordDetailsMainSection(
                    state = state,
                    amountInputFocusRequester = amountInputFocusRequester,
                    onTypeChange = { onAction(Action.OnTypeChange(it)) },
                    onAmountChange = { onAction(Action.OnAmountChange(it)) },
                    onTransferAmountChange = { onAction(Action.OnTransferAmountChange(it)) },
                    onSelectAccountClick = { onAction(Action.OnSelectAccountClick) },
                    onSelectTransferAccountClick = { onAction(Action.OnSelectTransferAccountClick) },
                    onSelectCategoryClick = { onAction(Action.OnSelectCategoryClick) },
                    onSelectDateClick = { onAction(Action.OnSelectDateClick) },
                    onSelectTimeClick = { onAction(Action.OnSelectTimeClick) }
                )
                RecordDetailsAdditionsSection(
                    modifier = Modifier.fillMaxWidth(),
                    labelsInputFieldState = state.labelsInput,
                    descriptionState = state.descriptionInput,
                    showSection = state.showAdditionsSection,
                    receipts = state.receipts,
                    onAddLabel = { onAction(Action.OnLabelAdd(it)) },
                    onRemoveLabelAtIndex = { onAction(Action.OnLabelRemove(it)) },
                    onAddReceiptClick = { onAction(Action.OnAddReceiptClick) },
                    onViewReceiptClick = { onAction(Action.OnViewReceiptClick(it)) },
                    onDeleteReceiptClick = { onAction(Action.OnDeleteReceiptClick(it)) },
                    onDescriptionChange = { onAction(Action.OnDescriptionChange(it)) },
                    onLabelChange = { onAction(Action.OnLabelChange(it)) },
                    onVisibilityChange = { onAction(Action.OnAdditionsSectionVisibilityChange(it)) },
                )
            }
        }
    }
}