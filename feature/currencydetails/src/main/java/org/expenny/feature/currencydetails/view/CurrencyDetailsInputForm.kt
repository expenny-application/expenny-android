package org.expenny.feature.currencydetails.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyExpandableContent
import org.expenny.core.ui.components.ExpennyMessage
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.field.MonetaryInputField
import org.expenny.core.ui.foundation.ExpennyCheckBoxGroup
import org.expenny.core.ui.foundation.ExpennyMonetaryInputField
import org.expenny.core.ui.foundation.ExpennySelectInputField
import java.math.BigDecimal

@Composable
internal fun AccountDetailsInputForm(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    showRatesDisclaimerMessage: Boolean,
    showRatesInputFields: Boolean,
    showEnableRatesUpdateCheckbox: Boolean,
    enableRatesUpdates: Boolean,
    currencyUnitInputField: InputField,
    baseToQuoteRateInputField: MonetaryInputField,
    quoteToBaseRateInputField: MonetaryInputField,
    baseCurrency: String,
    quoteCurrency: String,
    onBaseToQuoteRateChange: (BigDecimal) -> Unit,
    onQuoteToBaseRateChange: (BigDecimal) -> Unit,
    onSelectCurrencyUnitClick: () -> Unit,
    onEnableRatesUpdateCheckboxChange: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (showRatesDisclaimerMessage) {
            ExpennyMessage(message = stringResource(R.string.currency_rates_info_message))
        }
        SelectCurrencyUnitInputField(
            modifier = Modifier.fillMaxWidth(),
            state = currencyUnitInputField,
            onClick = onSelectCurrencyUnitClick
        )
        ExpennyExpandableContent(isExpanded = showRatesInputFields) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RateInputField(
                    modifier = Modifier.weight(1f),
                    state = baseToQuoteRateInputField,
                    currency = quoteCurrency,
                    label = "1 $baseCurrency",
                    onValueChange = onBaseToQuoteRateChange
                )
                RateInputField(
                    modifier = Modifier.weight(1f),
                    state = quoteToBaseRateInputField,
                    currency = baseCurrency,
                    label = "1 $quoteCurrency",
                    onValueChange = onQuoteToBaseRateChange
                )
            }
        }
        if (showEnableRatesUpdateCheckbox) {
            ExpennyCheckBoxGroup(
                label = stringResource(R.string.enable_currency_rates_updates_message),
                isChecked = enableRatesUpdates,
                onClick = onEnableRatesUpdateCheckboxChange
            )
        }
    }
}

@Composable
private fun SelectCurrencyUnitInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onClick: () -> Unit
) {
    with(state) {
        ExpennySelectInputField(
            modifier = modifier,
            isRequired = required,
            isEnabled = enabled,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.currency_code_label),
            placeholder = stringResource(R.string.select_currency_code_label),
            onClick = onClick
        )
    }
}

@Composable
private fun RateInputField(
    modifier: Modifier = Modifier,
    state: MonetaryInputField,
    currency: String,
    label: String,
    onValueChange: (BigDecimal) -> Unit
) {
    ExpennyMonetaryInputField(
        modifier = modifier.fillMaxWidth(),
        label = label,
        state = state,
        currency = currency,
        onValueChange = onValueChange,
        imeAction = ImeAction.Next,
    )
}
