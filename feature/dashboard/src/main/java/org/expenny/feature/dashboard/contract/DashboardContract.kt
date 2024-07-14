package org.expenny.feature.dashboard.contract

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.expenny.core.common.types.PeriodType
import org.expenny.core.common.types.DashboardWidgetType
import org.expenny.core.common.types.RecordType
import org.expenny.core.ui.data.navargs.RecordsListFilterNavArg
import org.expenny.core.ui.data.AccountNameUi
import org.expenny.core.ui.reducers.AccountsFilterStateReducer
import org.expenny.feature.dashboard.model.DashboardBalanceUi
import org.expenny.feature.dashboard.model.DashboardExpensesUi

data class DashboardState(
    val showAddRecordDialog: Boolean = false,
    val displayCurrency: String? = null,
    val periodTypes: ImmutableList<PeriodType> = PeriodType.entries.toImmutableList(),
    val currentPeriodType: PeriodType = periodTypes.first(),
    val widgets: ImmutableList<DashboardWidgetType> = DashboardWidgetType.entries.toImmutableList(),
    val balanceData: DashboardBalanceUi = DashboardBalanceUi(),
    val expensesData: DashboardExpensesUi = DashboardExpensesUi(),
    val accountsFilterState: AccountsFilterStateReducer.State = AccountsFilterStateReducer.State(),
)

sealed interface DashboardAction {
    class OnExpensesPeriodTypeChange(val periodType: PeriodType) : DashboardAction
    class OnCategoryExpensesSelect(val index: Int) : DashboardAction
    class OnWidgetClick(val widget: DashboardWidgetType) : DashboardAction
    class OnAccountSelect(val id: Long) : DashboardAction
    class OnDisplayCurrencySelect(val id: Long) : DashboardAction
    class OnAddRecord(val recordType: RecordType) : DashboardAction
    object OnAddRecordClick : DashboardAction
    object OnAddRecordDialogDismiss : DashboardAction
    object OnShowMoreRecordsClick : DashboardAction
    object OnAllAccountsSelect : DashboardAction
    object OnCategoryExpensesDeselect : DashboardAction
    object OnDisplayCurrencyClick : DashboardAction
}

sealed interface DashboardEvent {
    class NavigateToDisplayCurrencySelection(val selectedId: Long?) : DashboardEvent
    class NavigateToRecords(val filter: RecordsListFilterNavArg? = null) : DashboardEvent
    class NavigateToCreateRecord(val recordType: RecordType) : DashboardEvent
    object NavigateToAccounts : DashboardEvent
    object NavigateToBudgets : DashboardEvent
}