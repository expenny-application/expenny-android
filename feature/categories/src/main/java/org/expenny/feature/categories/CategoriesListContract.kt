package org.expenny.feature.categories

import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.selection.SingleSelection
import org.expenny.core.ui.data.ui.CategoryUi

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.categories_label),
    val selection: SingleSelection<Long>? = null,
    val categories: List<CategoryUi> = emptyList(),
)

sealed interface Action {
    class OnCategoryClick(val id: Long) : Action
    object OnBackClick : Action
}

sealed interface Event {
    class NavigateBackWithResult(val selection: LongNavArg) : Event
    object NavigateBack : Event
}