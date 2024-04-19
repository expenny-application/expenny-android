package org.expenny.feature.categories

import org.expenny.core.common.models.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.ui.SingleSelectionUi
import org.expenny.core.ui.data.ui.CategoryUi

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.categories_label),
    val selection: SingleSelectionUi<Long>? = null,
    val categories: List<CategoryUi> = emptyList(),
)

sealed interface Action {
    class OnCategoryClick(val id: Long) : Action
    data object OnAddCategoryClick : Action
    data object OnBackClick : Action
}

sealed interface Event {
    class NavigateToEditCategory(val id: Long) : Event
    class NavigateBackWithResult(val selection: LongNavArg) : Event
    data object NavigateToCreateCategory : Event
    data object NavigateBack : Event
}