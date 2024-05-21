package org.expenny.feature.accounts.list.navigation

interface AccountsListNavigator {
    fun navigateToOverviewAccountScreen(accountId: Long)
    fun navigateToEditAccountScreen(accountId: Long)
    fun navigateToAccountTypeScreen()
    fun navigateToCreateAccountScreen()
    fun navigateBack()
}