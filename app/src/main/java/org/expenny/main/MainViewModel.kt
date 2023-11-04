package org.expenny.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.usecase.profile.GetProfileSetUpUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val getProfileSetUp: GetProfileSetUpUseCase,
) : ViewModel() {

    val isProfileSetUp: StateFlow<Boolean?> = getProfileSetUp()
        .onStart { delay(500) } // delay splash screen appearance
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )

    val theme: StateFlow<ApplicationTheme> = localRepository.isDarkMode()
        .mapLatest {
            when (it) {
                true -> ApplicationTheme.Dark
                false -> ApplicationTheme.Light
                null -> ApplicationTheme.SystemDefault
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ApplicationTheme.SystemDefault
        )
}
