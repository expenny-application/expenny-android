package org.expenny.feature.institution.navigation

interface InstitutionCountriesListNavigator {
    fun navigateToInstitutionsListScreen(countryCode: String)
    fun navigateBack()
}