package org.expenny.feature.accounts.list.navigation

interface AccountsListNavigator {
    fun navigateToEditAccountScreen(accountId: Long)
    fun navigateToAccountTypeScreen()
    fun navigateToCreateAccountScreen()
    fun navigateBack()
}