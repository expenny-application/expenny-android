package org.expenny.feature.getstarted.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyExpandableContent
import org.expenny.core.ui.foundation.ExpennyCheckBoxGroup
import org.expenny.core.ui.foundation.ExpennyInputField
import org.expenny.core.ui.foundation.ExpennyMonetaryInputField
import org.expenny.core.ui.foundation.ExpennySelectInputField
import org.expenny.core.ui.data.field.CheckBoxField
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.field.MonetaryInputField
import org.expenny.feature.getstarted.State
import java.math.BigDecimal

@Composable
internal fun GetStartedInputForm(
    modifier: Modifier = Modifier,
    state: State,
    onNameChange: (String) -> Unit,
    onAccountNameChange: (String) -> Unit,
    onAccountBalanceChange: (BigDecimal) -> Unit,
    onSelectCurrencyClick: () -> Unit,
    onSetupCashBalanceCheckBoxChange: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameInputField(
            modifier = Modifier.fillMaxWidth(),
            state = state.nameInput,
            onValueChange = onNameChange
        )

        SelectCurrencyInputField(
            modifier = Modifier.fillMaxWidth(),
            state = state.selectCurrencyInput,
            onClick = onSelectCurrencyClick
        )

        ExpennyExpandableContent(
            isExpanded = state.showSetupCashBalanceInputFields,
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AccountNameInputField(
                        modifier = Modifier.fillMaxWidth(),
                        state = state.accountNameInput,
                        onValueChange = onAccountNameChange
                    )
                    AccountBalanceInputField(
                        modifier = Modifier.fillMaxWidth(),
                        state = state.accountBalanceInput,
                        currency = state.selectedCurrency,
                        onValueChange = onAccountBalanceChange
                    )
                }
            }
        )
        if (state.showSetupCashBalanceCheckBox) {
            SetupCashBalanceCheckBox(
                modifier = Modifier.fillMaxWidth(),
                state = state.setupCashBalanceCheckBox,
                onValueChange = onSetupCashBalanceCheckBoxChange
            )
        }
    }
}

@Composable
private fun SetupCashBalanceCheckBox(
    modifier: Modifier = Modifier,
    state: CheckBoxField,
    onValueChange: (Boolean) -> Unit
) {
    with(state) {
        ExpennyCheckBoxGroup(
            modifier = modifier,
            label = stringResource(R.string.set_cash_balance_paragraph),
            error = error?.asRawString(),
            isChecked = value,
            isRequired = required,
            onClick = onValueChange
        )
    }
}

@Composable
private fun NameInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onValueChange: (String) -> Unit
) {
    with(state) {
        ExpennyInputField(
            modifier = modifier,
            isRequired = isRequired,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.your_name_label),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
    }
}

@Composable
private fun SelectCurrencyInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onClick: () -> Unit
) {
    with(state) {
        ExpennySelectInputField(
            modifier = modifier,
            isRequired = isRequired,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.currency_label),
            placeholder = stringResource(R.string.select_currency_label),
            onClick = onClick
        )
    }
}

@Composable
private fun AccountNameInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onValueChange: (String) -> Unit
) {
    with(state) {
        ExpennyInputField(
            modifier = modifier,
            isRequired = isRequired,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.account_name_label),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
    }
}

@Composable
private fun AccountBalanceInputField(
    modifier: Modifier = Modifier,
    state: MonetaryInputField,
    currency: String,
    onValueChange: (BigDecimal) -> Unit
) {
    ExpennyMonetaryInputField(
        modifier = modifier.fillMaxWidth(),
        label = stringResource(R.string.balance_label),
        state = state,
        currency = currency,
        onValueChange = onValueChange,
        imeAction = ImeAction.Next,
    )
}
