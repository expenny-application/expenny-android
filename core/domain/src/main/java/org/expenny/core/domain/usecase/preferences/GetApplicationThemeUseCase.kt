package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.datastore.ExpennyDataStore
import javax.inject.Inject

class GetApplicationThemeUseCase @Inject constructor(
    private val preferences: ExpennyDataStore
) {

    operator fun invoke(): Flow<ApplicationTheme> {
        return preferences.isDarkTheme().mapLatest {
            when (it) {
                true -> ApplicationTheme.Dark
                false -> ApplicationTheme.Light
                null -> ApplicationTheme.SystemDefault
            }
        }
    }
}