package org.expenny.feature.categories.list.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.data.CategoryUi

data class CategoriesListState(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.categories_label),
    val selection: SingleSelectionUi<Long>? = null,
    val categories: List<CategoryUi> = emptyList(),
)

sealed interface CategoriesListAction {
    class OnCategoryClick(val id: Long) : CategoriesListAction
    data object OnAddCategoryClick : CategoriesListAction
    data object OnBackClick : CategoriesListAction
}

sealed interface CategoriesListEvent {
    class NavigateToEditCategory(val id: Long) : CategoriesListEvent
    class NavigateBackWithResult(val selection: LongNavArg) : CategoriesListEvent
    data object NavigateToCreateCategory : CategoriesListEvent
    data object NavigateBack : CategoriesListEvent
}