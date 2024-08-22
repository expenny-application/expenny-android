package org.expenny.feature.records.details.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.expenny.core.common.extensions.toMonetaryString
import org.expenny.core.common.types.RecordType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyInputField
import org.expenny.core.ui.components.ExpennyMonetaryInputField
import org.expenny.core.ui.components.ExpennySegmentedSurfaceTabs
import org.expenny.core.ui.components.ExpennySelectInputField
import org.expenny.core.ui.data.DecimalInputUi
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.transformations.ExpennyDecimalVisualTransformation
import org.expenny.feature.records.details.contract.RecordDetailsState
import java.math.BigDecimal

@Composable
internal fun RecordDetailsMainSection(
    modifier: Modifier = Modifier,
    state: RecordDetailsState,
    amountInputFocusRequester: FocusRequester,
    onTypeChange: (RecordType) -> Unit,
    onAmountChange: (BigDecimal) -> Unit,
    onConversionRateChange: (BigDecimal) -> Unit,
    onSelectAccountClick: () -> Unit,
    onSelectTransferAccountClick: () -> Unit,
    onSelectCategoryClick: () -> Unit,
    onSelectDateTimeClick: () -> Unit,
) {
    Column(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!state.types.isNullOrEmpty()) {
            RecordTypesTabRow(
                modifier = Modifier.fillMaxWidth(),
                types = state.types,
                selectedType = state.selectedType,
                onChange = onTypeChange
            )
        }
        AmountInputField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(amountInputFocusRequester),
            state = state.amountInput,
            convertedAmount = state.convertedAmount,
            convertedAmountCurrency = state.convertedAmountCurrency,
            currency = state.amountCurrency.orEmpty(),
            onValueChange = onAmountChange,
        )
        if (state.showConversionRateInput) {
            ConversionRateInputField(
                modifier = Modifier.fillMaxWidth(),
                state = state.conversionRateInput,
                onValueChange = onConversionRateChange
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SelectAccountInputField(
                modifier = Modifier.weight(1f),
                state = state.accountInput,
                onClick = onSelectAccountClick
            )
            if (state.showTransferAccountInput) {
                SelectTransferAccountInputField(
                    modifier = Modifier.weight(1f),
                    state = state.transferAccountInput,
                    onClick = onSelectTransferAccountClick
                )
            }
        }
        if (!state.hideCategoryInput) {
            SelectCategoryInputField(
                state = state.categoryInput,
                onClick = onSelectCategoryClick
            )
        }
        SelectDateTimeInputField(
            state = state.dateTimeInput,
            onClick = onSelectDateTimeClick
        )
    }
}

@Composable
private fun RecordTypesTabRow(
    modifier: Modifier = Modifier,
    types: ImmutableList<RecordType>,
    selectedType: RecordType,
    onChange: (RecordType) -> Unit
) {
    ExpennySegmentedSurfaceTabs(
        modifier = modifier,
        tabs = types.map { it.label },
        selectedTabIndex = types.indexOf(selectedType),
        onTabSelect = {
            onChange(types[it])
        }
    )
}

@Composable
private fun AmountInputField(
    modifier: Modifier = Modifier,
    state: DecimalInputUi,
    convertedAmount: BigDecimal?,
    convertedAmountCurrency: String?,
    currency: String,
    onValueChange: (BigDecimal) -> Unit,
) {
    val description = buildString {
        convertedAmount?.let {
            append(convertedAmount.toMonetaryString())
            convertedAmountCurrency?.let {
                append(" ")
                append(convertedAmountCurrency)
            }
        }
    }.takeIf { it.isNotBlank() }?.let {
        stringResource(R.string.amount_to_receive_label, it)
    }

    ExpennyMonetaryInputField(
        modifier = modifier.fillMaxWidth(),
        description = description,
        label = stringResource(R.string.amount_label),
        currency = currency,
        value = state.value,
        error = state.error?.asRawString(),
        isEnabled = state.isEnabled,
        isRequired = state.isRequired,
        onValueChange = onValueChange
    )
}

@Composable
private fun ConversionRateInputField(
    modifier: Modifier = Modifier,
    state: DecimalInputUi,
    onValueChange: (BigDecimal) -> Unit,
) {
    ExpennyInputField(
        modifier = modifier.fillMaxWidth(),
        label = stringResource(R.string.conversion_rate_label),
        value = ExpennyDecimalVisualTransformation.formatToInput(state.value),
        error = state.error?.asRawString(),
        isRequired = state.isRequired,
        isEnabled = state.isEnabled,
        onValueChange = {
            onValueChange(ExpennyDecimalVisualTransformation.formatToOutput(it, state.value.scale()))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Next
        ),
        visualTransformation = ExpennyDecimalVisualTransformation(state.value.scale())
    )
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
    state: InputUi,
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
    state: InputUi,
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
private fun SelectDateTimeInputField(
    modifier: Modifier = Modifier,
    state: InputUi,
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
            placeholder = stringResource(R.string.select_datetime_label),
            onClick = onClick
        )
    }
}
