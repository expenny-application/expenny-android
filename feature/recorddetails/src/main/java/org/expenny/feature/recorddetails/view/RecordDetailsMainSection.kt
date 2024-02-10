package org.expenny.feature.recorddetails.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.expenny.core.common.types.RecordType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySegmentedTabRow
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.field.MonetaryInputField
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.foundation.ExpennyMonetaryInputField
import org.expenny.core.ui.foundation.ExpennySelectInputField
import org.expenny.core.ui.theme.surfaceInput
import org.expenny.feature.recorddetails.State
import java.math.BigDecimal

@Composable
internal fun RecordDetailsMainSection(
    modifier: Modifier = Modifier,
    state: State,
    amountInputFocusRequester: FocusRequester,
    onTypeChange: (RecordType) -> Unit,
    onAmountChange: (BigDecimal) -> Unit,
    onTransferAmountChange: (BigDecimal) -> Unit,
    onSelectAccountClick: () -> Unit,
    onSelectTransferAccountClick: () -> Unit,
    onSelectCategoryClick: () -> Unit,
    onSelectDateClick: () -> Unit,
    onSelectTimeClick: () -> Unit,
) {
    Column(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RecordTypeTabRow(
            modifier = Modifier.fillMaxWidth(),
            types = state.types,
            selectedType = state.selectedType,
            onChange = onTypeChange
        )
        AmountInputField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(amountInputFocusRequester),
            state = state.amountInput,
            currency = state.amountCurrency,
            onValueChange = onAmountChange
        )
        if (state.showTransferAmountInput) {
            TransferAmountInputField(
                modifier = Modifier.fillMaxWidth(),
                state = state.transferAmountInput,
                currency = state.transferAmountCurrency,
                onValueChange = onTransferAmountChange
            )
        }
        SelectAccountInputField(
            state = state.accountInput,
            onClick = onSelectAccountClick
        )
        if (state.showTransferAccountInput) {
            SelectTransferAccountInputField(
                state = state.transferAccountInput,
                onClick = onSelectTransferAccountClick
            )
        }
        if (state.showCategoryInput) {
            SelectCategoryInputField(
                state = state.categoryInput,
                onClick = onSelectCategoryClick
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SelectDateInputField(
                modifier = Modifier.weight(1f),
                state = state.dateInput,
                onClick = onSelectDateClick
            )
            SelectTimeInputField(
                modifier = Modifier.weight(1f),
                state = state.timeInput,
                onClick = onSelectTimeClick
            )
        }
    }
}

@Composable
private fun RecordTypeTabRow(
    modifier: Modifier = Modifier,
    types: ImmutableList<RecordType>,
    selectedType: RecordType,
    onChange: (RecordType) -> Unit
) {
    ExpennySegmentedTabRow(
        modifier = modifier.height(48.dp),
        tabs = types.map { it.label },
        textStyle = MaterialTheme.typography.bodyLarge,
        containerColor = MaterialTheme.colorScheme.surfaceInput,
        selectedTabIndex = types.indexOf(selectedType),
        onTabSelect = {
            onChange(types[it])
        }
    )
}

@Composable
private fun AmountInputField(
    modifier: Modifier = Modifier,
    state: MonetaryInputField,
    currency: String,
    onValueChange: (BigDecimal) -> Unit
) {
    ExpennyMonetaryInputField(
        modifier = modifier.fillMaxWidth(),
        label = stringResource(R.string.amount_label),
        state = state,
        currency = currency,
        onValueChange = onValueChange,
        imeAction = ImeAction.Next,
    )
}

@Composable
private fun TransferAmountInputField(
    modifier: Modifier = Modifier,
    state: MonetaryInputField,
    currency: String,
    onValueChange: (BigDecimal) -> Unit
) {
    ExpennyMonetaryInputField(
        modifier = modifier.fillMaxWidth(),
        label = stringResource(R.string.final_amount_label),
        state = state,
        currency = currency,
        onValueChange = onValueChange,
        imeAction = ImeAction.Next,
    )
}

@Composable
private fun SelectCategoryInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onClick: () -> Unit
) {
    with(state) {
        ExpennySelectInputField(
            modifier = modifier,
            isRequired = isRequired,
            value = value,
            isEnabled = isEnabled,
            error = error?.asRawString(),
            label = stringResource(R.string.category_label),
            placeholder = stringResource(R.string.select_category_label),
            onClick = onClick
        )
    }
}

@Composable
private fun SelectAccountInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onClick: () -> Unit
) {
    with(state) {
        ExpennySelectInputField(
            modifier = modifier,
            isRequired = isRequired,
            value = value,
            isEnabled = isEnabled,
            error = error?.asRawString(),
            label = stringResource(R.string.account_label),
            placeholder = stringResource(R.string.select_account_label),
            onClick = onClick
        )
    }
}

@Composable
private fun SelectTransferAccountInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onClick: () -> Unit
) {
    with(state) {
        ExpennySelectInputField(
            modifier = modifier,
            isRequired = isRequired,
            value = value,
            isEnabled = isEnabled,
            error = error?.asRawString(),
            label = stringResource(R.string.transfer_account_label),
            placeholder = stringResource(R.string.select_transfer_account_label),
            onClick = onClick
        )
    }
}

@Composable
private fun SelectDateInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onClick: () -> Unit
) {
    with(state) {
        ExpennySelectInputField(
            modifier = modifier,
            isRequired = isRequired,
            value = value,
            isEnabled = isEnabled,
            error = error?.asRawString(),
            label = stringResource(R.string.date_label),
            placeholder = stringResource(R.string.select_date_label),
            onClick = onClick
        )
    }
}

@Composable
private fun SelectTimeInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onClick: () -> Unit
) {
    with(state) {
        ExpennySelectInputField(
            modifier = modifier,
            isRequired = isRequired,
            value = value,
            isEnabled = isEnabled,
            error = error?.asRawString(),
            label = stringResource(R.string.time_label),
            placeholder = stringResource(R.string.select_time_label),
            onClick = onClick
        )
    }
}