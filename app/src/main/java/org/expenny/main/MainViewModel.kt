package org.expenny.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
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

    val profileState: StateFlow<ProfileState> = getProfileSetUp()
        .map { ProfileState.Loaded }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            ProfileState.Loading
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

    val locale: StateFlow<String> = localRepository.getLocale()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = "en"
        )
}

sealed class ProfileState {
    data object Loading: ProfileState()
    data object Loaded: ProfileState()
}