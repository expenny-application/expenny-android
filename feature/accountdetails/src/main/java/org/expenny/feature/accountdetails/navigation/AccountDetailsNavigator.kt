package org.expenny.feature.accountdetails.navigation

interface AccountDetailsNavigator {
    fun navigateToCurrencySelectionListScreen(selectedId: Long?)
    fun navigateBack()
}