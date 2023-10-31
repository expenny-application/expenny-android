package org.expenny.feature.accounts.navigation

interface AccountsListNavigator {
    fun navigateToOverviewAccountScreen(accountId: Long)
    fun navigateToEditAccountScreen(accountId: Long)
    fun navigateToCreateAccountScreen()
    fun navigateBack()
}