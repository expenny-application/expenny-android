package org.expenny.feature.categories.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.components.ExpennyIconBox
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.data.ui.SelectionType
import org.expenny.core.ui.data.ui.SingleSelectionUi
import org.expenny.core.ui.data.ui.CategoryUi
import org.expenny.core.ui.extensions.type
import org.expenny.core.ui.foundation.ExpennyCard
import org.expenny.core.ui.foundation.ExpennyVerticalList


@Composable
internal fun CategoriesList(
    modifier: Modifier = Modifier,
    selection: SingleSelectionUi<Long>?,
    categories: List<CategoryUi>,
    onCategoryClick: (Long) -> Unit,
) {
    ExpennyVerticalList(
        modifier = modifier,
        list = categories,
        listItemKey = CategoryUi::id,
        listItem = { item ->
            CategoryItem(
                modifier = Modifier.fillMaxWidth(),
                selectionType = selection?.type,
                selected = selection?.contains(item.id) ?: false,
                category = item,
                onClick = {
                    onCategoryClick(item.id)
                }
            )
        }
    )
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    selectionType: SelectionType?,
    selected: Boolean,
    category: CategoryUi,
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
            ExpennyIconBox(
                icon = painterResource(category.icon.iconResId),
                color = category.icon.color,
                background = MaterialTheme.colorScheme.surface
            )
            Text(
                modifier = Modifier.weight(1f),
                text = category.name,
                style = MaterialTheme.typography.titleMedium
            )
            if (selectionType != null) {
                ExpennySelectionButton(
                    isSelected = selected,
                    type = selectionType,
                    onClick = {
                        onClick()
                    }
                )
            }
        }
    }
}