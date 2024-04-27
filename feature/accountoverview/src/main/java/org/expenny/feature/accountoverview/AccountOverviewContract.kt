package org.expenny.feature.accountoverview

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.expenny.core.common.types.AccountTrendType
import org.expenny.core.common.types.IntervalType
import org.expenny.core.ui.data.AmountUi
import org.expenny.core.ui.data.CategoryStatementUi
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.reducers.IntervalTypeStateReducer
import org.expenny.feature.accountoverview.model.AccountOverviewChartUi

@Stable
data class State(
    val overviewChart: AccountOverviewChartUi = AccountOverviewChartUi(),
    val totalValue: AmountUi? = null,
    val trendType: AccountTrendType = AccountTrendType.Balance,
    val trendTypes: ImmutableList<AccountTrendType> = AccountTrendType.values().toList().toImmutableList(),
    val intervalState: IntervalTypeStateReducer.State = IntervalTypeStateReducer.State(),
    val statements: List<CategoryStatementUi> = emptyList(),
    val dialog: Dialog? = null,
) {
    sealed interface Dialog {
        data class IntervalTypesDialog(
            val data: List<ItemUi<IntervalType>>,
            val selection: SingleSelectionUi<IntervalType>
        ) : Dialog
    }
}

sealed interface Action {
    sealed interface Dialog : Action {
        class OnIntervalTypeSelect(val selection: SingleSelectionUi<IntervalType>) : Dialog
        data object OnDialogDismiss : Dialog
    }
    class OnTrendTypeChange(val type: AccountTrendType) : Action
    data object OnSelectIntervalClick : Action
    data object OnNextIntervalClick : Action
    data object OnPreviousIntervalClick : Action
}

sealed interface Event {

}