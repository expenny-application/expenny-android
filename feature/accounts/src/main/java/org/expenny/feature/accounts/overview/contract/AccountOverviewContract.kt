package org.expenny.feature.accounts.overview.contract

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
import org.expenny.feature.accounts.overview.model.AccountOverviewChartUi

@Stable
data class AccountOverviewState(
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

sealed interface AccountOverviewAction {
    sealed interface Dialog : AccountOverviewAction {
        class OnIntervalTypeSelect(val selection: SingleSelectionUi<IntervalType>) : Dialog
        data object OnDialogDismiss : Dialog
    }
    class OnTrendTypeChange(val type: AccountTrendType) : AccountOverviewAction
    data object OnSelectIntervalClick : AccountOverviewAction
    data object OnNextIntervalClick : AccountOverviewAction
    data object OnPreviousIntervalClick : AccountOverviewAction
}

sealed interface AccountOverviewEvent {

}