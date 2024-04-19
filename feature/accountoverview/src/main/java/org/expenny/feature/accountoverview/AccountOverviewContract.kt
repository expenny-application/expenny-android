package org.expenny.feature.accountoverview

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.expenny.core.common.types.AccountTrendType
import org.expenny.core.common.types.DateRangeSpan
import org.expenny.core.ui.data.ui.AmountUi
import org.expenny.core.ui.data.ui.CategoryStatementUi
import org.expenny.core.ui.data.ui.ItemUi
import org.expenny.core.ui.data.ui.SingleSelectionUi
import org.expenny.core.ui.reducers.DateRangeSpanStateReducer
import org.expenny.feature.accountoverview.model.AccountOverviewChartUi

@Stable
data class State(
    val overviewChart: AccountOverviewChartUi = AccountOverviewChartUi(),
    val totalValue: AmountUi? = null,
    val trendType: AccountTrendType = AccountTrendType.Balance,
    val trendTypes: ImmutableList<AccountTrendType> = AccountTrendType.values().toList().toImmutableList(),
    val dateRangeSpans: List<DateRangeSpan> = listOf(DateRangeSpan.Week(), DateRangeSpan.Month(), DateRangeSpan.Year()),
    val dateRangeSpanState: DateRangeSpanStateReducer.State = DateRangeSpanStateReducer.State(),
    val statements: List<CategoryStatementUi> = emptyList(),
    val dialog: Dialog? = null,
) {
    sealed interface Dialog {
        data class DateRangeSpanDialog(
            val data: List<ItemUi>,
            val selection: SingleSelectionUi<Long>
        ) : Dialog
    }
}

sealed interface Action {
    sealed interface Dialog : Action {
        class OnDateRangeSpanSelect(val selection: SingleSelectionUi<Long>) : Dialog
        data object OnDialogDismiss : Dialog
    }
    class OnTrendTypeChange(val type: AccountTrendType) : Action
    data object OnSelectDateRangeSpanClick : Action
    data object OnNextDateRangeClick : Action
    data object OnPreviousDateRangeClick : Action
}

sealed interface Event {

}