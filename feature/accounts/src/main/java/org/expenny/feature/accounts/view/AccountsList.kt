package org.expenny.feature.accounts.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.data.selection.Selection
import org.expenny.core.ui.data.selection.SelectionType
import org.expenny.core.ui.data.ui.AccountUi
import org.expenny.core.ui.data.ui.AmountUi
import org.expenny.core.ui.extensions.type
import org.expenny.core.ui.foundation.*
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AccountsList(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    selection: Selection<Long>?,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
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
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ExpennyText(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
                ExpennyText(
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
                    ExpennyText(
                        text = recordsCount.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Icon(
                        modifier = Modifier.fillMaxHeight(),
                        painter = painterResource(R.drawable.ic_records),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
