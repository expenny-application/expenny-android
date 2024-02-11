package org.expenny.feature.currencydetails.navigation

interface CurrencyDetailsNavigator {
    fun navigateToAvailableCurrencyUnitSelectionListScreen(selectedId: Long?)
    fun navigateBack()
}