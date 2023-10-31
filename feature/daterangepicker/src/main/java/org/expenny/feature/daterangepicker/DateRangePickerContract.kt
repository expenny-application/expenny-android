package org.expenny.feature.daterangepicker

import org.threeten.extra.LocalDateRange

data class State(
    val selectedRange: Pair<String?, String?> = Pair("", ""),
    val showClearButton: Boolean = false,
    val enableApplyButton: Boolean = false,
    val initialSelectedStartDateEpoch: Long? = null,
    val initialSelectedEndDateEpoch: Long? = null,
)

sealed interface Action {
    class OnStartDateSelect(val ms: Long?) : Action
    class OnEndDateSelect(val ms: Long?) : Action
    object OnClearClick : Action
    object OnApplyClick : Action
    object OnCloseClick : Action
}

sealed interface Event {
    class NavigateBackWithResult(val range: LocalDateRange) : Event
    object NavigateBack : Event
    object ClearDateRangeSelection : Event
}