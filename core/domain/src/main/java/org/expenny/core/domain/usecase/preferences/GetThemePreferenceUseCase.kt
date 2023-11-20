package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.domain.repository.LocalRepository
import javax.inject.Inject

class GetThemePreferenceUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    operator fun invoke(): Flow<ApplicationTheme> {
        return localRepository.isDarkMode().mapLatest {
            when (it) {
                true -> ApplicationTheme.Dark
                false -> ApplicationTheme.Light
                null -> ApplicationTheme.SystemDefault
            }
        }
    }
}