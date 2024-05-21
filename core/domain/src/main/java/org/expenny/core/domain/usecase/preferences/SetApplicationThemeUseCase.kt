package org.expenny.core.domain.usecase.preferences

import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.datastore.ExpennyDataStore
import javax.inject.Inject

class SetApplicationThemeUseCase @Inject constructor(
    private val preferences: ExpennyDataStore
) {

    suspend operator fun invoke(params: Params) {
        when (params.theme) {
            ApplicationTheme.Dark -> preferences.setIsDarkTheme(true)
            ApplicationTheme.Light -> preferences.setIsDarkTheme(false)
            ApplicationTheme.SystemDefault -> preferences.setIsDarkTheme(null)
        }
    }

    data class Params(
        val theme: ApplicationTheme
    )
}