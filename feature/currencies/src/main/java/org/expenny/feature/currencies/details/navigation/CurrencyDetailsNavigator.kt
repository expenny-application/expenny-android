package org.expenny.feature.currencies.details.navigation

interface CurrencyDetailsNavigator {
    fun navigateToAvailableCurrencyUnitSelectionListScreen(selectedId: Long?)
    fun navigateBack()
}