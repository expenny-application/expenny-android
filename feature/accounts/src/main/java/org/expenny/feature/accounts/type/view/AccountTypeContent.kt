package org.expenny.feature.accounts.type.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.common.types.AccountType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyButton
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyRadioButton
import org.expenny.core.ui.extensions.benefits
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.extensions.message
import org.expenny.feature.accounts.type.contract.AccountTypeAction
import org.expenny.feature.accounts.type.contract.AccountTypeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountTypeContent(
    state: AccountTypeState,
    onAction: (AccountTypeAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AccountTypeToolbar(
                scrollBehavior = scrollBehavior,
                onBackClick = { onAction(AccountTypeAction.OnBackClick) }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 24.dp,
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                state.accountTypes.forEach {
                    AccountTypeCard(
                        accountType = it,
                        isSelected = it == state.selectedType,
                        onClick = { onAction(AccountTypeAction.OnAccountTypeSelect(it)) }
                    )
                }
            }
            ExpennyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                onClick = { onAction(AccountTypeAction.OnCtaButtonClick) },
                isEnabled = state.isCtaButtonEnabled,
                label = {
                    ButtonLabel(text = stringResource(R.string.confirm_button))
                }
            )
        }
    }
}

@Composable
private fun AccountTypeCard(
    modifier: Modifier = Modifier,
    accountType: AccountType,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    ExpennyCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = accountType.label,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                ExpennyRadioButton(
                    isChecked = isSelected,
                    onClick = onClick
                )
            }
            Text(
                text = accountType.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Column(
                modifier = Modifier.padding(start = 4.dp)
            ) {
                accountType.benefits.forEach {
                    Text(
                        text = "• $it",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}