package org.expenny.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.usecase.preferences.GetBiometricInvalidatedUseCase
import org.expenny.core.domain.usecase.preferences.GetCanSendAlarmsUseCase
import org.expenny.core.domain.usecase.preferences.GetPasscodePreferenceUseCase
import org.expenny.core.domain.usecase.preferences.GetReminderPreferenceUseCase
import org.expenny.core.domain.usecase.preferences.GetThemePreferenceUseCase
import org.expenny.core.domain.usecase.preferences.SetBiometricPreferenceUseCase
import org.expenny.core.domain.usecase.preferences.SetReminderPreferenceUseCase
import org.expenny.core.domain.usecase.profile.GetProfileSetUpUseCase
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getPasscodePreference: GetPasscodePreferenceUseCase,
    getThemePreference: GetThemePreferenceUseCase,
    getProfileSetUp: GetProfileSetUpUseCase,
    private val getBiometricInvalidated: GetBiometricInvalidatedUseCase,
    private val setBiometricEnrolled: SetBiometricPreferenceUseCase,
    private val getCanSendAlarms: GetCanSendAlarmsUseCase,
    private val getReminderPreference: GetReminderPreferenceUseCase,
    private val setReminderPreference: SetReminderPreferenceUseCase
) : ViewModel() {

    val isProfileSetUp: StateFlow<Boolean?> = getProfileSetUp()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    val isPasscodeSetUp: StateFlow<Boolean?> = getPasscodePreference()
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    val theme: StateFlow<ApplicationTheme> = getThemePreference()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ApplicationTheme.SystemDefault
        )

    fun verifyBiometricInvalidationStatus() {
        if (getBiometricInvalidated()) {
            viewModelScope.launch {
                Timber.i("Biometric key was invalidated")
                setBiometricEnrolled(false)
            }
        }
    }

    fun verifyAlarmsRevokeStatus() {
        // https://developer.android.com/about/versions/14/changes/schedule-exact-alarms#gracefully-degrade
        viewModelScope.launch {
            if (getReminderPreference().first()) {
                if (!getCanSendAlarms()) {
                    Timber.i("Alarm setting was revoked")
                    setReminderPreference(false)
                }
            }
        }
    }
}
