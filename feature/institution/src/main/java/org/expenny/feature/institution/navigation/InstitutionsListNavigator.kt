package org.expenny.feature.institution.navigation

interface InstitutionsListNavigator {
    fun navigateToInstitutionRequisition(institutionId: String)
    fun navigateBack()
}