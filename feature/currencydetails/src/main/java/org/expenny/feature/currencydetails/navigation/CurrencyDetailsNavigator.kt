package org.expenny.feature.currencydetails.navigation

interface CurrencyDetailsNavigator {
    fun navigateToCurrencyUnitSelectionListScreen(selectedId: Long?)
    fun navigateBack()
}