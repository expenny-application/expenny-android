package org.expenny.feature.profilesetup.view

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
import org.expenny.core.ui.components.ExpennyCheckboxInput
import org.expenny.core.ui.components.ExpennyInputField
import org.expenny.core.ui.components.ExpennyMonetaryInputField
import org.expenny.core.ui.components.ExpennySelectInputField
import org.expenny.core.ui.data.CheckboxInputUi
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.DecimalInputUi
import org.expenny.feature.profilesetup.contract.ProfileSetupState
import java.math.BigDecimal

@Composable
internal fun ProfileSetupInputForm(
    modifier: Modifier = Modifier,
    state: ProfileSetupState,
    onNameChange: (String) -> Unit,
    onAccountNameChange: (String) -> Unit,
    onAccountBalanceChange: (BigDecimal) -> Unit,
    onSelectCurrencyClick: () -> Unit,
    onSetupCashBalanceCheckboxChange: (Boolean) -> Unit,
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
            isExpanded = state.showSetupCashBalanceInputs,
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
        if (state.showSetupCashBalanceCheckbox) {
            SetupCashBalanceCheckbox(
                modifier = Modifier.fillMaxWidth(),
                state = state.setupCashBalanceCheckbox,
                onValueChange = onSetupCashBalanceCheckboxChange
            )
        }
    }
}

@Composable
private fun SetupCashBalanceCheckbox(
    modifier: Modifier = Modifier,
    state: CheckboxInputUi,
    onValueChange: (Boolean) -> Unit
) {
    with(state) {
        ExpennyCheckboxInput(
            modifier = modifier,
            error = error?.asRawString(),
            isChecked = value,
            isRequired = isRequired,
            onClick = onValueChange,
            caption = {
                Caption(text = stringResource(R.string.set_cash_balance_paragraph))
            }
        )
    }
}

@Composable
private fun NameInputField(
    modifier: Modifier = Modifier,
    state: InputUi,
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
    state: InputUi,
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
    state: InputUi,
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
    state: DecimalInputUi,
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
