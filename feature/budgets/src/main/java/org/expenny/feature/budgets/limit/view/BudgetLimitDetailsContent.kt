package org.expenny.feature.budgets.limit.view

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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyCheckboxGroup
import org.expenny.core.ui.components.ExpennyFab
import org.expenny.core.ui.components.ExpennyMonetaryInputField
import org.expenny.core.ui.components.ExpennySelectInputField
import org.expenny.core.ui.data.CheckboxInputUi
import org.expenny.core.ui.data.DecimalInputUi
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.clearFocusOnTapOutside
import org.expenny.core.ui.extensions.floatingActionButtonPadding
import org.expenny.feature.budgets.limit.contract.BudgetLimitDetailsAction
import org.expenny.feature.budgets.limit.contract.BudgetLimitDetailsState
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetLimitDetailsContent(
    state: BudgetLimitDetailsState,
    scrollState: ScrollState,
    limitInputFocusRequester: FocusRequester,
    onAction: (BudgetLimitDetailsAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    if (state.showDeleteDialog) {
        BudgetLimitDetailsDeleteDialog(
            onConfirm = { onAction(BudgetLimitDetailsAction.OnDeleteDialogConfirm) },
            onDismiss = { onAction(BudgetLimitDetailsAction.OnDeleteDialogDismiss) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .clearFocusOnTapOutside(focusManager)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BudgetLimitDetailsToolbar(
                scrollBehavior = scrollBehavior,
                title = state.toolbarTitle.asRawString(),
                showDeleteButton = state.showDeleteButton,
                onBackClick = { onAction(BudgetLimitDetailsAction.OnBackClick) },
                onDeleteClick = { onAction(BudgetLimitDetailsAction.OnDeleteClick) }
            )
        },
        floatingActionButton = {
            ExpennyFab(
                modifier = Modifier.navigationBarsPadding(),
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onAction(BudgetLimitDetailsAction.OnSaveClick)
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
        BudgetDetailsInputForm(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .navigationBarsPadding()
                .floatingActionButtonPadding(),
            scrollState = scrollState,
            state = state,
            limitInputFocusRequester = limitInputFocusRequester,
            onLimitChange = { onAction(BudgetLimitDetailsAction.OnLimitChange(it)) },
            onEnablePeriodicBudgetCheckboxChange = { onAction(BudgetLimitDetailsAction.OnEnablePeriodicLimitCheckboxChange(it)) },
            onSelectCategoryClick = { onAction(BudgetLimitDetailsAction.OnSelectCategoryClick) }
        )
    }
}

@Composable
internal fun BudgetDetailsInputForm(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    state: BudgetLimitDetailsState,
    limitInputFocusRequester: FocusRequester,
    onLimitChange: (BigDecimal) -> Unit,
    onEnablePeriodicBudgetCheckboxChange: (Boolean) -> Unit,
    onSelectCategoryClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SelectCategoryInputField(
            modifier = Modifier.fillMaxWidth(),
            state = state.categoryInput,
            onClick = onSelectCategoryClick
        )
        LimitInputField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(limitInputFocusRequester),
            state = state.limitInput,
            currency = state.currency,
            onValueChange = onLimitChange
        )
        if (state.showEnablePeriodicLimitCheckbox) {
            EnablePeriodicLimitCheckbox(
                modifier = Modifier.fillMaxWidth(),
                state = state.enablePeriodicLimitCheckboxInput,
                onValueChange = onEnablePeriodicBudgetCheckboxChange
            )
        }
    }
}

@Composable
private fun EnablePeriodicLimitCheckbox(
    modifier: Modifier = Modifier,
    state: CheckboxInputUi,
    onValueChange: (Boolean) -> Unit
) {
    with(state) {
        ExpennyCheckboxGroup(
            modifier = modifier,
            error = error?.asRawString(),
            isChecked = value,
            isRequired = isRequired,
            onClick = onValueChange,
            caption = {
                CheckboxGroupCaption(text = stringResource(R.string.enable_periodic_budget_limit_paragraph))
            }
        )
    }
}

@Composable
private fun SelectCategoryInputField(
    modifier: Modifier = Modifier,
    state: InputUi,
    onClick: () -> Unit
) {
    with(state) {
        ExpennySelectInputField(
            modifier = modifier,
            isRequired = isRequired,
            isEnabled = isEnabled,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.category_label),
            placeholder = stringResource(R.string.select_category_label),
            onClick = onClick
        )
    }
}

@Composable
private fun LimitInputField(
    modifier: Modifier = Modifier,
    state: DecimalInputUi,
    currency: String,
    onValueChange: (BigDecimal) -> Unit
) {
    ExpennyMonetaryInputField(
        modifier = modifier.fillMaxWidth(),
        label = stringResource(R.string.limit_label),
        value = state.value,
        error = state.error?.asRawString(),
        isEnabled = state.isEnabled,
        isRequired = state.isRequired,
        currency = currency,
        onValueChange = onValueChange
    )
}