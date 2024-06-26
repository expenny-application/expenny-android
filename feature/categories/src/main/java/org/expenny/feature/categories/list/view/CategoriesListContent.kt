package org.expenny.feature.categories.list.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennyIconContainer
import org.expenny.core.ui.components.ExpennySelectionButton
import org.expenny.core.ui.components.ExpennyVerticalList
import org.expenny.core.ui.data.CategoryUi
import org.expenny.core.ui.data.SelectionType
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.type
import org.expenny.feature.categories.list.contract.CategoriesListAction
import org.expenny.feature.categories.list.contract.CategoriesListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CategoriesListContent(
    state: CategoriesListState,
    onAction: (CategoriesListAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CategoriesListToolbar(
                scrollBehavior = scrollBehavior,
                title = state.toolbarTitle.asRawString(),
                onAddClick = { onAction(CategoriesListAction.OnAddCategoryClick) },
                onBackClick = { onAction(CategoriesListAction.OnBackClick) }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        CategoriesList(
            modifier = Modifier.padding(paddingValues),
            categories = state.categories,
            selection = state.selection,
            onCategoryClick = { onAction(CategoriesListAction.OnCategoryClick(it)) },
        )
    }
}

@Composable
private fun CategoriesList(
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
            ExpennyIconContainer(
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