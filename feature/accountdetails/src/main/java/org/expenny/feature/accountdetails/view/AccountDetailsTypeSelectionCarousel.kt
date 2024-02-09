package org.expenny.feature.accountdetails.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.expenny.core.common.types.AccountType
import org.expenny.core.ui.extensions.icon
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.foundation.ExpennySelectableChip

@Composable
internal fun AccountDetailsTypeSelectionCarousel(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    types: List<AccountType>,
    selection: AccountType,
    onTypeSelect: (AccountType) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(types) { type ->
            ExpennySelectableChip(
                modifier = Modifier.height(48.dp),
                isSelected = type == selection,
                onClick = {
                    onTypeSelect(type)
                },
                label = {
                    Text(text = type.label)
                },
                leadingIcon = {
                    Icon(
                        painter = type.icon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}
