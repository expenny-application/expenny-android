package org.expenny.feature.accountdetails.navigation

interface AccountDetailsNavigator {
    fun navigateBackToAccountsListScreen()
    fun navigateToCurrencySelectionListScreen(selectedId: Long?)
    fun navigateBack()
}