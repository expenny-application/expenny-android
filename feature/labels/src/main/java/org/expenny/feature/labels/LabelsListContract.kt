package org.expenny.feature.labels

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.expenny.core.common.utils.StringResource
import org.expenny.core.resources.R
import org.expenny.core.ui.data.navargs.LongArrayNavArg
import org.expenny.core.ui.data.selection.MultiSelection
import org.expenny.core.ui.data.ui.LabelUi

data class State(
    val toolbarTitle: StringResource = StringResource.fromRes(R.string.labels_label),
    val labels: ImmutableList<LabelUi> = persistentListOf(),
    val selection: MultiSelection<Long>? = null,
    val showConfirmButton: Boolean = false,
)

sealed interface Action {
    class OnLabelClick(val id: Long) : Action
    object OnAddLabelClick : Action
    object OnConfirmClick : Action
    object OnBackClick : Action
}

sealed interface Event {
    class ShowMessage(val message: String) : Event
    class NavigateToEditLabel(val id: Long) : Event
    class NavigateBackWithResult(val selection: LongArrayNavArg) : Event
    object NavigateToCreateLabel : Event
    object NavigateBack : Event
}