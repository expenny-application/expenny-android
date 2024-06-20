package org.expenny.feature.records.details.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.common.extensions.toLocalDateTime
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyFab
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.core.ui.extensions.floatingActionButtonPadding
import org.expenny.feature.records.details.contract.RecordDetailsAction
import org.expenny.feature.records.details.contract.RecordDetailsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecordDetailsContent(
    state: RecordDetailsState,
    scrollState: ScrollState,
    amountInputFocusRequester: FocusRequester,
    onAction: (RecordDetailsAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    RecordDetailsDialog(
        dialog = state.dialog,
        dateTimePickerValue = state.dateTimeInput.value.toLocalDateTime(),
        onAction = onAction
    )

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
                showInfoButton = state.showTransferDisclaimerButton,
                onBackClick = { onAction(RecordDetailsAction.OnBackClick) },
                onDeleteClick = { onAction(RecordDetailsAction.OnDeleteClick) },
                onInfoClick = { onAction(RecordDetailsAction.OnTransferDisclaimerClick) }
            )
        },
        floatingActionButton = {
            ExpennyFab(
                modifier = Modifier.navigationBarsPadding(),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(RecordDetailsAction.OnSaveClick)
                },
                icon = {
                    FabIcon(painter = painterResource(R.drawable.ic_check))
                },
                label = {
                    FabLabel(text = stringResource(R.string.save_button))
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
                    onTypeChange = { onAction(RecordDetailsAction.OnTypeChange(it)) },
                    onAmountChange = { onAction(RecordDetailsAction.OnAmountChange(it)) },
                    onConversionRateChange = { onAction(RecordDetailsAction.OnConversionRateChange(it)) },
                    onSelectAccountClick = { onAction(RecordDetailsAction.OnSelectAccountClick) },
                    onSelectTransferAccountClick = { onAction(RecordDetailsAction.OnSelectTransferAccountClick) },
                    onSelectCategoryClick = { onAction(RecordDetailsAction.OnSelectCategoryClick) },
                    onSelectDateTimeClick = { onAction(RecordDetailsAction.OnSelectDateTimeClick) },
                )
                RecordDetailsAdditionsSection(
                    modifier = Modifier.fillMaxWidth(),
                    labels = state.labels,
                    descriptionState = state.descriptionInput,
                    showSection = state.showAdditionsSection,
                    receipts = state.receipts,
                    onAddReceiptClick = { onAction(RecordDetailsAction.OnAddReceiptClick) },
                    onViewReceiptClick = { onAction(RecordDetailsAction.OnViewReceiptClick(it)) },
                    onDeleteReceiptClick = { onAction(RecordDetailsAction.OnDeleteReceiptClick(it)) },
                    onDescriptionChange = { onAction(RecordDetailsAction.OnDescriptionChange(it)) },
                    onSelectLabelClick = { onAction(RecordDetailsAction.OnSelectLabelsClick) },
                    onLabelRemove = { onAction(RecordDetailsAction.OnLabelRemove(it)) },
                    onVisibilityChange = { onAction(RecordDetailsAction.OnAdditionsSectionVisibilityChange(it)) },
                )
            }
        }
    }
}

