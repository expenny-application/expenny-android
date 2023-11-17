package org.expenny.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.usecase.preferences.GetBiometricInvalidatedUseCase
import org.expenny.core.domain.usecase.preferences.SetBiometricPreferenceUseCase
import org.expenny.core.domain.usecase.profile.GetProfileSetUpUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val getProfileSetUp: GetProfileSetUpUseCase,
    private val getBiometricInvalidated: GetBiometricInvalidatedUseCase,
    private val setBiometricEnrolled: SetBiometricPreferenceUseCase,
) : ViewModel() {

    val isProfileSetUp: StateFlow<Boolean?> = getProfileSetUp()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )

    val isPasscodeSetUp: StateFlow<Boolean?> = localRepository.getPasscode()
        .map { it != null }
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

    fun verifyBiometricKeyInvalidationStatus() {
        if (getBiometricInvalidated()) {
            viewModelScope.launch {
                setBiometricEnrolled(false)
            }
        }
    }
}
