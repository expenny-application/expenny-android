package org.expenny.feature.accounts.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.data.SelectionUi
import org.expenny.core.ui.data.SelectionType
import org.expenny.core.ui.data.AccountUi
import org.expenny.core.ui.data.AmountUi
import org.expenny.core.ui.extensions.type
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyVerticalList

@Composable
internal fun AccountsList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    selection: SelectionUi<Long>?,
    accounts: List<AccountUi>,
    onAccountClick: (Long) -> Unit,
) {
    ExpennyVerticalList(
        modifier = modifier,
        state = lazyListState,
        list = accounts,
        listItemKey = AccountUi::id,
        listItem = { item ->
            AccountItem(
                modifier = Modifier.fillMaxWidth(),
                selectionType = selection?.type,
                selected = selection?.contains(item.id) ?: false,
                title = item.name,
                balance = item.balance,
                recordsCount = item.recordsCount,
                onClick = {
                    onAccountClick(item.id)
                }
            )
        }
    )
}

@Composable
private fun AccountItem(
    modifier: Modifier = Modifier,
    selectionType: SelectionType?,
    selected: Boolean,
    title: String,
    balance: AmountUi,
    recordsCount: Int,
    onClick: () -> Unit
) {
    ExpennyCard(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = balance.displayValue,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            if (selectionType != null) {
                ExpennySelectionButton(
                    type = selectionType,
                    isSelected = selected,
                    onClick = {
                        onClick()
                    }
                )
            } else {
                Row(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = recordsCount.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Icon(
                        modifier = Modifier.fillMaxHeight(),
                        painter = painterResource(R.drawable.ic_list),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
