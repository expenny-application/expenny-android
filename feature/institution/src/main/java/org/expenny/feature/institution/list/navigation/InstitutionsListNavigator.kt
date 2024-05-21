package org.expenny.feature.institution.list.navigation

interface InstitutionsListNavigator {
    fun navigateToInstitutionRequisition(institutionId: String)
    fun navigateBack()
}