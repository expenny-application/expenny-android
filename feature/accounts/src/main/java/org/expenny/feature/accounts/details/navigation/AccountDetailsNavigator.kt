package org.expenny.feature.accounts.details.navigation

interface AccountDetailsNavigator {
    fun navigateBackToAccountsListScreen()
    fun navigateToCurrencySelectionListScreen(selectedId: Long?)
    fun navigateBack()
}