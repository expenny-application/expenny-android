package org.expenny.feature.accounts.details.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.expenny.core.common.types.LocalAccountType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySection
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.data.DecimalInputUi
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.components.ExpennyInputField
import org.expenny.core.ui.components.ExpennyMonetaryInputField
import org.expenny.core.ui.components.ExpennySelectInputField
import org.expenny.feature.accounts.details.contract.AccountDetailsState
import java.math.BigDecimal

@Composable
internal fun AccountDetailsInputForm(
    modifier: Modifier = Modifier,
    state: AccountDetailsState,
    scrollState: ScrollState,
    nameInputFocusRequester: FocusRequester,
    typesListState: LazyListState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onStartBalanceChange: (BigDecimal) -> Unit,
    onSelectCurrencyClick: () -> Unit,
    onTypeSelect: (LocalAccountType) -> Unit,
    onAdditionsSectionVisibilityChange: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .imePadding()
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 56.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AccountDetailsTypes(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            listState = typesListState,
            types = state.types,
            selection = state.selectedType,
            onTypeSelect = onTypeSelect
        )
        NameInputField(
            modifier = Modifier.focusRequester(nameInputFocusRequester),
            state = state.nameInput,
            onValueChange = onNameChange
        )
        SelectCurrencyInputField(
            state = state.currencyInput,
            onClick = onSelectCurrencyClick
        )
        ExpennySection(
            title = stringResource(R.string.additions_label),
            isExpanded = state.showAdditionsSection,
            onClick = {
                onAdditionsSectionVisibilityChange(!state.showAdditionsSection)
            }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StartBalanceInputField(
                    state = state.startBalanceInput,
                    currency = state.selectedCurrency,
                    onValueChange = onStartBalanceChange
                )
                DescriptionInputField(
                    state = state.descriptionInput,
                    onValueChange = onDescriptionChange
                )
            }
        }
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
            modifier = modifier.fillMaxWidth(),
            isRequired = isRequired,
            isEnabled = isEnabled,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.currency_label),
            placeholder = stringResource(R.string.select_currency_label),
            onClick = onClick
        )
    }
}

@Composable
private fun DescriptionInputField(
    modifier: Modifier = Modifier,
    state: InputUi,
    onValueChange: (String) -> Unit
) {
    with(state) {
        ExpennyInputField(
            modifier = modifier.fillMaxWidth(),
            isRequired = isRequired,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.description_label),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
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
            modifier = modifier.fillMaxWidth(),
            isRequired = isRequired,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.title_label),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
    }
}

@Composable
private fun StartBalanceInputField(
    modifier: Modifier = Modifier,
    state: DecimalInputUi,
    currency: String,
    onValueChange: (BigDecimal) -> Unit
) {
    ExpennyMonetaryInputField(
        modifier = modifier.fillMaxWidth(),
        label = stringResource(R.string.start_balance_label),
        currency = currency,
        value = state.value,
        error = state.error?.asRawString(),
        isEnabled = state.isEnabled,
        isRequired = state.isRequired,
        onValueChange = onValueChange,
    )
}