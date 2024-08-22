package org.expenny.feature.budgets.overview.contract

import org.expenny.core.common.models.StringResource
import org.expenny.core.common.types.BudgetType
import org.expenny.core.common.types.IntervalType
import org.expenny.core.resources.R
import org.expenny.core.ui.data.AccountNameUi
import org.expenny.core.ui.data.BudgetGroupUi
import org.expenny.core.ui.reducers.AccountsFilterStateReducer
import org.expenny.core.ui.reducers.IntervalTypeStateReducer
import java.time.LocalDate

data class BudgetOverviewState(
    val selectedAccounts: List<AccountNameUi> = emptyList(),
    val selectAllAccounts: Boolean = true,
    val accounts: List<AccountNameUi> = emptyList(),
    val displayCurrency: String? = null,
    val isReadonly: Boolean = false,
    val overview: BudgetGroupUi = BudgetGroupUi(),
    val intervalTypeState: IntervalTypeStateReducer.State = IntervalTypeStateReducer.State(),
    val accountsFilterState: AccountsFilterStateReducer.State = AccountsFilterStateReducer.State(),
)

sealed interface BudgetOverviewAction {
    class OnDisplayCurrencySelect(val id: Long) : BudgetOverviewAction
    class OnAccountSelect(val id: Long) : BudgetOverviewAction
    class OnBudgetLimitClick(val budgetId: Long) : BudgetOverviewAction
    data object OnAllAccountsSelect : BudgetOverviewAction
    data object OnDisplayCurrencyClick : BudgetOverviewAction
    data object OnNextIntervalClick : BudgetOverviewAction
    data object OnPreviousIntervalClick : BudgetOverviewAction
    data object OnAddBudgetLimitClick : BudgetOverviewAction
    data object OnBackClick : BudgetOverviewAction
}

sealed interface BudgetOverviewEvent {
    class NavigateToDisplayCurrencySelection(val selectedId: Long?) : BudgetOverviewEvent
    class NavigateToBudgetLimitDetails(
        val budgetId: Long? = null,
        val budgetGroupId: Long,
        val budgetType: BudgetType,
        val startDate: LocalDate,
        val endDate: LocalDate,
        val excludeCategoriesIds: LongArray,
    ) : BudgetOverviewEvent
    class ShowError(val error: StringResource) : BudgetOverviewEvent
    data object NavigateBack : BudgetOverviewEvent
}