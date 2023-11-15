package org.expenny.feature.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.common.viewmodel.ExpennyActionViewModel
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.usecase.preferences.GetBiometricStatusUseCase
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.domain.usecase.preferences.DeletePasscodeUseCase
import org.expenny.core.domain.usecase.preferences.GetBiometricEnrolledUseCase
import org.expenny.core.domain.usecase.preferences.SetBiometricEnrolledUseCase
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.model.biometric.BiometricStatus.AvailableButNotEnrolled
import org.expenny.core.model.biometric.BiometricStatus.Ready
import org.expenny.core.ui.mapper.ProfileMapper
import org.expenny.feature.settings.model.SettingsItemType
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val getBiometricStatus: GetBiometricStatusUseCase,
    private val setBiometricEnrolled: SetBiometricEnrolledUseCase,
    private val getBiometricEnrolled: GetBiometricEnrolledUseCase,
    private val deletePasscode: DeletePasscodeUseCase,
    private val profileMapper: ProfileMapper,
) : ExpennyActionViewModel<Action>(), ContainerHost<State, Event> {

    override val container = container<State, Event>(
        initialState = State(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setSelectedLanguage()
            launch { subscribeToCurrentProfile() }
            launch { subscribeToThemePreference() }
            launch { subscribeToPasscodePreference() }
            launch { subscribeToBiometricPreference() }
        }
    }

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnSettingsItemTypeClick -> handleOnSettingsItemTypeClick(action)
            is Action.OnDialogDismiss -> handleOnDialogDismiss()
            is Action.OnLanguageSelect -> handleOnLanguageSelect(action)
            is Action.OnThemeSelect -> handleOnThemeSelect(action)
            is Action.OnBackClick -> handleOnBackClick()
        }
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(Event.NavigateBack)
    }

    private fun handleOnSettingsItemTypeClick(action: Action.OnSettingsItemTypeClick) = intent {
        when (action.type) {
            SettingsItemType.Theme -> {
                reduce { state.copy(dialog = State.Dialog.ThemeDialog) }
            }
            SettingsItemType.Language -> {
                reduce { state.copy(dialog = State.Dialog.LanguageDialog) }
            }
            SettingsItemType.Currencies -> {
                postSideEffect(Event.NavigateToCurrencies)
            }
            SettingsItemType.Labels -> {
                postSideEffect(Event.NavigateToLabels)
            }
            SettingsItemType.Passcode -> {
                if (state.isUsePasscodeSelected) {
                    deletePasscode()
                    setBiometricEnrolled(false)
                } else {
                    postSideEffect(Event.NavigateToCreatePasscode)
                }
            }
            SettingsItemType.Biometric -> {
                if (state.isUseBiometricSelected) {
                    setBiometricEnrolled(false)
                } else {
                    val biometricStatus = getBiometricStatus()
                    if (biometricStatus == AvailableButNotEnrolled) {
                        postSideEffect(Event.NavigateToSystemSecuritySettings)
                    } else if (biometricStatus == Ready) {
                        setBiometricEnrolled(true)
                    }
                }
            }
            else -> {}
        }
    }

    private fun handleOnLanguageSelect(action: Action.OnLanguageSelect): Job {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(action.language.tag))
        return intent {
            reduce {
                state.copy(
                    selectedLanguage = action.language,
                    dialog = null
                )
            }
        }
    }

    private fun handleOnDialogDismiss() = intent {
        reduce { state.copy(dialog = null) }
    }

    private fun handleOnThemeSelect(action: Action.OnThemeSelect) = intent {
        when (action.theme) {
            ApplicationTheme.Dark -> localRepository.setThemeDarkMode(true)
            ApplicationTheme.Light -> localRepository.setThemeDarkMode(false)
            ApplicationTheme.SystemDefault -> localRepository.setThemeSystemDefault()
        }
        reduce { state.copy(selectedTheme = action.theme) }
        handleOnDialogDismiss()
    }

    private fun subscribeToCurrentProfile() = intent {
        getCurrentProfile().first()!!.also {
            reduce {
                state.copy(currentProfile = profileMapper(it))
            }
        }
    }

    private fun subscribeToPasscodePreference() = intent {
        localRepository.getPasscode()
            .map { it != null }
            .collect { isPasscodeSetUp ->
                reduce {
                    state.copy(isUsePasscodeSelected = isPasscodeSetUp)
                }
            }
    }

    private fun subscribeToBiometricPreference() = intent {
        val biometricStatus = getBiometricStatus()
        if (biometricStatus == Ready || biometricStatus == AvailableButNotEnrolled) {
            getBiometricEnrolled().collect {
                reduce {
                    state.copy(
                        isBiometricAvailable = true,
                        isUseBiometricSelected = it
                    )
                }
            }
        } else {
            reduce { state.copy(isBiometricAvailable = false) }
        }
    }

    private fun setSelectedLanguage() = intent {
        reduce {
            val localeTag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
            val selectedLanguage = ApplicationLanguage.tagOf(localeTag).let {
                when (it) {
                    null -> {
                        // couldn't parse locale tag, fallback to system default
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
                        ApplicationLanguage.SystemDefault
                    }
                    else -> it
                }
            }
            state.copy(selectedLanguage = selectedLanguage)
        }
    }

    private fun subscribeToThemePreference() = intent {
        localRepository.isDarkMode()
            .mapLatest {
                when (it) {
                    true -> ApplicationTheme.Dark
                    false -> ApplicationTheme.Light
                    null -> ApplicationTheme.SystemDefault
                }
            }.collect {
                reduce {
                    state.copy(selectedTheme = it)
                }
            }
    }
}