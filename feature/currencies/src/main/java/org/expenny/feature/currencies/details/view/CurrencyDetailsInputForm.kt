package org.expenny.feature.currencies.details.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySection
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.DecimalInputUi
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.components.ExpennyCheckboxGroup
import org.expenny.core.ui.components.ExpennyMonetaryInputField
import org.expenny.core.ui.components.ExpennyReadonlyInputField
import org.expenny.core.ui.components.ExpennySelectInputField
import org.expenny.feature.currencies.details.contract.CurrencyDetailsState
import java.math.BigDecimal

@Composable
internal fun CurrencyDetailsInputForm(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    state: CurrencyDetailsState,
    onBaseToQuoteRateChange: (BigDecimal) -> Unit,
    onQuoteToBaseRateChange: (BigDecimal) -> Unit,
    onSelectCurrencyUnitClick: () -> Unit,
    onSubscribeToUpdatesCheckboxChange: (Boolean) -> Unit,
    onUpdateClick: () -> Unit,
) {
    with(state) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SelectCurrencyUnitInputField(
                modifier = Modifier.fillMaxWidth(),
                state = currencyUnitInput,
                onClick = onSelectCurrencyUnitClick
            )
            if (showCurrencyRatesSection) {
                ExpennySection(
                    title = stringResource(R.string.currency_rates_label)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            RateInputField(
                                modifier = Modifier.weight(1f),
                                state = quoteToBaseRateInput,
                                currency = baseCurrency,
                                label = "1 $quoteCurrency",
                                onValueChange = onQuoteToBaseRateChange
                            )
                            RateInputField(
                                modifier = Modifier.weight(1f),
                                state = baseToQuoteRateInput,
                                currency = quoteCurrency,
                                label = "1 $baseCurrency",
                                onValueChange = onBaseToQuoteRateChange
                            )
                        }
                        if (isSubscribableToUpdates) {
                            LastUpdateInputField(
                                modifier = Modifier.fillMaxWidth(),
                                state = lastUpdateInput,
                                isUpdatable = isUpdatable,
                                onUpdateClick = onUpdateClick
                            )
                            ExpennyCheckboxGroup(
                                isChecked = isSubscribedToUpdates,
                                onClick = onSubscribeToUpdatesCheckboxChange,
                                caption = {
                                    CheckboxGroupCaption(text = stringResource(R.string.subscribe_to_rates_updates_message))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LastUpdateInputField(
    modifier: Modifier = Modifier,
    state: InputUi,
    isUpdatable: Boolean,
    onUpdateClick: () -> Unit
) {
    with(state) {
        ExpennyReadonlyInputField(
            modifier = modifier,
            label = stringResource(R.string.last_updated_label),
            error = error?.asRawString(),
            value = value,
            isEnabled = isEnabled,
            isRequired = isRequired,
            trailingContent = {
                if (isUpdatable) {
                    IconButton(
                        onClick = onUpdateClick,
                        enabled = isEnabled
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_refresh),
                            contentDescription = null,
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun SelectCurrencyUnitInputField(
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
            label = stringResource(R.string.currency_code_label),
            placeholder = stringResource(R.string.select_currency_code_label),
            onClick = onClick
        )
    }
}

@Composable
private fun RateInputField(
    modifier: Modifier = Modifier,
    state: DecimalInputUi,
    currency: String,
    label: String,
    onValueChange: (BigDecimal) -> Unit
) {
    ExpennyMonetaryInputField(
        modifier = modifier.fillMaxWidth(),
        label = label,
        value = state.value,
        error = state.error?.asRawString(),
        isEnabled = state.isEnabled,
        isRequired = state.isRequired,
        currency = currency,
        onValueChange = onValueChange
    )
}
