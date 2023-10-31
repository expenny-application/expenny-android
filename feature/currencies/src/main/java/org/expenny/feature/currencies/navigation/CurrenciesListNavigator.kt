package org.expenny.feature.currencies.navigation

interface CurrenciesListNavigator {
    fun navigateToEditCurrencyScreen(currencyId: Long)
    fun navigateToCreateCurrencyScreen()
    fun navigateBack()
}