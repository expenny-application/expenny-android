package org.expenny.feature.accountdetails.navigation

interface AccountDetailsNavigator {
    fun navigateBackToAccountsList()
    fun navigateToCurrencySelectionListScreen(selectedId: Long?)
    fun navigateBack()
}