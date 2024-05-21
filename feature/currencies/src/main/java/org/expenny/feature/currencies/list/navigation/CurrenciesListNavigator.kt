package org.expenny.feature.currencies.list.navigation

interface CurrenciesListNavigator {
    fun navigateToEditCurrencyScreen(currencyId: Long)
    fun navigateToCreateCurrencyScreen()
    fun navigateBack()
}