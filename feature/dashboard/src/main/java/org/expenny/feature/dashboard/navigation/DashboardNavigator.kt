package org.expenny.feature.dashboard.navigation

import org.expenny.core.common.types.RecordType
import org.expenny.core.ui.data.navargs.RecordsListFilterNavArg

interface DashboardNavigator {
    fun navigateToAccountsListScreen()
    fun navigateToRecordsListScreen(filter: RecordsListFilterNavArg? = null)
    fun navigateToCreateRecordScreen(recordType: RecordType)
    fun navigateToCreateAccountScreen()
    fun navigateToCurrencySelectionListScreen(selectedId: Long?)
}