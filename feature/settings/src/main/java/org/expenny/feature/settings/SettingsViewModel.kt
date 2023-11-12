package org.expenny.feature.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.expenny.core.common.utils.StringResourceProvider
import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.common.utils.StringResource
import org.expenny.core.common.viewmodel.ExpennyActionViewModel
import org.expenny.core.domain.repository.BiometricRepository
import org.expenny.core.domain.repository.LocalRepository
import org.expenny.core.domain.usecase.GetBiometricStatusUseCase
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.model.biometric.BiometricStatus.AvailableButNotEnrolled
import org.expenny.core.model.biometric.BiometricStatus.Ready
import org.expenny.core.model.biometric.CryptoPurpose
import org.expenny.core.ui.mapper.ProfileMapper
import org.expenny.feature.settings.model.SettingsItemType
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val biometricRepository: BiometricRepository,
    private val getBiometricStatus: GetBiometricStatusUseCase,
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val profileMapper: ProfileMapper,
    private val stringProvider: StringResourceProvider,
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
            is Action.OnBackClick -> {
                intent {
                    postSideEffect(Event.NavigateBack)
                }
            }
            is Action.OnSettingsItemTypeClick -> {
                when (action.type) {
                    SettingsItemType.Theme -> {
                        intent {
                            reduce { state.copy(dialog = State.Dialog.ThemeDialog) }
                        }
                    }
                    SettingsItemType.Language -> {
                        intent {
                            reduce { state.copy(dialog = State.Dialog.LanguageDialog) }
                        }
                    }
                    SettingsItemType.Currencies -> {
                        intent {
                            postSideEffect(Event.NavigateToCurrencies)
                        }
                    }
                    SettingsItemType.Labels -> {
                        intent {
                            postSideEffect(Event.NavigateToLabels)
                        }
                    }
                    SettingsItemType.Passcode -> {
                        intent {
                            if (state.isUsePasscodeSelected) {
                                localRepository.setPasscode(null)
                                localRepository.setBiometricEnrolled(false)
                                biometricRepository.clearSecretKey()
                            } else {
                                postSideEffect(Event.NavigateToCreatePasscode)
                            }
                        }
                    }
                    SettingsItemType.Biometric -> {
                        intent {
                            if (state.isUseBiometricSelected) {
                                localRepository.setBiometricEnrolled(false)
                                biometricRepository.clearSecretKey()
                            } else {
                                val biometricStatus = getBiometricStatus()
                                if (biometricStatus == AvailableButNotEnrolled) {
                                    postSideEffect(Event.NavigateToSystemSecuritySettings)
                                } else if (biometricStatus == Ready) {
                                    biometricRepository.generateSecretKey()
                                    biometricRepository.createCryptoObject(CryptoPurpose.Encrypt).also {
                                        postSideEffect(Event.ShowBiometricPrompt(it))
                                    }
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
            is Action.OnThemeDialogDismiss -> {
                hideDialogs()
            }
            is Action.OnLanguageDialogDismiss -> {
                hideDialogs()
            }
            is Action.OnLanguageSelect -> {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(action.language.tag))
                intent {
                    reduce {
                        state.copy(
                            selectedLanguage = action.language,
                            dialog = null
                        )
                    }
                }
            }
            is Action.OnThemeSelect -> {
                intent {
                    when (action.theme) {
                        ApplicationTheme.Dark -> localRepository.setThemeDarkMode(true)
                        ApplicationTheme.Light -> localRepository.setThemeDarkMode(false)
                        ApplicationTheme.SystemDefault -> localRepository.setThemeSystemDefault()
                    }
                    reduce {
                        state.copy(
                            selectedTheme = action.theme,
                            dialog = null
                        )
                    }
                }
            }
            is Action.OnBiometricAuthenticationSuccess -> {
                intent {
                    localRepository.setBiometricEnrolled(true)
                }
            }
            is Action.OnBiometricAuthenticationError -> {
                intent {
                    postSideEffect(Event.ShowMessage(StringResource.fromStr(action.error)))
                }
            }
            else -> {}
        }
    }

    private fun hideDialogs() = intent {
        reduce { state.copy(dialog = null) }
    }

    private fun subscribeToCurrentProfile() = intent {
        getCurrentProfile().first()!!.also {
            reduce {
                state.copy(currentProfile = profileMapper(it))
            }
        }
    }

    private fun subscribeToPasscodePreference() = intent {
        repeatOnSubscription {
            localRepository.getPasscode()
                .map { it != null }
                .collect { isPasscodeSetUp ->
                    reduce {
                        state.copy(isUsePasscodeSelected = isPasscodeSetUp)
                    }
                }
        }
    }

    private fun subscribeToBiometricPreference() = intent {
        val biometricStatus = getBiometricStatus()
        if (biometricStatus == Ready || biometricStatus == AvailableButNotEnrolled) {
            repeatOnSubscription {
                localRepository.isBiometricEnrolled().collect {
                    reduce {
                        state.copy(
                            isBiometricAvailable = true,
                            isUseBiometricSelected = it
                        )
                    }
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
                        // Couldn't parse locale tag, fallback to system default
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