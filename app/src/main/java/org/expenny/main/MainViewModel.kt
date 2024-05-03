package org.expenny.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.common.utils.Constants.IS_GOCARDLESS_SDK_ENABLED_CONFIG_KEY
import org.expenny.core.domain.usecase.preferences.DeleteApplicationDataUseCase
import org.expenny.core.domain.usecase.preferences.GetBiometricInvalidatedUseCase
import org.expenny.core.domain.usecase.preferences.GetCanSendAlarmsUseCase
import org.expenny.core.domain.usecase.preferences.GetPasscodePreferenceUseCase
import org.expenny.core.domain.usecase.preferences.GetReminderPreferenceUseCase
import org.expenny.core.domain.usecase.preferences.GetThemePreferenceUseCase
import org.expenny.core.domain.usecase.preferences.SetBiometricPreferenceUseCase
import org.expenny.core.domain.usecase.preferences.SetInstallationIdUseCase
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
    private val setReminderPreference: SetReminderPreferenceUseCase,
    private val deleteApplicationData: DeleteApplicationDataUseCase,
    private val setInstallationId: SetInstallationIdUseCase,
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

    init {
        subscribeToRemoteConfigChanges()
        storeInstallationId()
    }

    suspend fun onDataCleanupRequest() {
        viewModelScope.async { deleteApplicationData() }.await()
    }

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

    private fun subscribeToRemoteConfigChanges() {
        Firebase.remoteConfig.apply {
            addOnConfigUpdateListener(
                object : ConfigUpdateListener {
                    override fun onUpdate(configUpdate : ConfigUpdate) {
                        if (configUpdate.updatedKeys.contains(IS_GOCARDLESS_SDK_ENABLED_CONFIG_KEY)) {
                            activate().addOnSuccessListener {
                                Timber.tag("Firebase").i("Config updated keys: ${configUpdate.updatedKeys}")
                            }
                        }
                    }

                    override fun onError(error : FirebaseRemoteConfigException) {
                        Timber.tag("Firebase").w(error, "Config update error with code: ${error.code}")
                    }
                }
            )
        }
    }

    private fun storeInstallationId() {
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Timber.tag("Firebase").i("Installation ID: ${task.result}")

                viewModelScope.launch {
                    setInstallationId(SetInstallationIdUseCase.Params(task.result))
                }
            } else {
                Timber.tag("Firebase").i("Unable to get Installation ID")
            }
        }
    }
}
