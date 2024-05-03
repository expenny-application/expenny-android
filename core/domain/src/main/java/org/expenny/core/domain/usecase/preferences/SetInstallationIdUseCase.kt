package org.expenny.core.domain.usecase.preferences

import org.expenny.core.datastore.ExpennyDataStore
import javax.inject.Inject

class SetInstallationIdUseCase @Inject constructor(
    private val preferences: ExpennyDataStore
) {

    suspend operator fun invoke(params: Params) {
        preferences.setInstallationId(params.installationId)
    }

    data class Params(val installationId: String)
}