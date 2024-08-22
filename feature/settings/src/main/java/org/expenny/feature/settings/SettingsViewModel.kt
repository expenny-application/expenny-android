package org.expenny.feature.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.expenny.core.common.extensions.join
import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.common.models.StringResource.Companion.fromRes
import org.expenny.core.common.models.StringResource.Companion.fromStr
import org.expenny.core.common.types.ProfileActionType
import org.expenny.core.common.types.SettingsItemType
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.core.domain.usecase.preferences.GetApplicationThemeUseCase
import org.expenny.core.domain.usecase.preferences.SetApplicationThemeUseCase
import org.expenny.core.domain.usecase.preferences.GetBiometricStatusUseCase
import org.expenny.core.domain.usecase.preferences.DeletePasscodePreferenceUseCase
import org.expenny.core.domain.usecase.preferences.GetBiometricPreferenceUseCase
import org.expenny.core.domain.usecase.preferences.GetCanSendAlarmsUseCase
import org.expenny.core.domain.usecase.preferences.GetInstallationIdUseCase
import org.expenny.core.domain.usecase.preferences.GetPasscodePreferenceUseCase
import org.expenny.core.domain.usecase.preferences.GetReminderPreferenceUseCase
import org.expenny.core.domain.usecase.preferences.GetReminderTimePreferenceUseCase
import org.expenny.core.domain.usecase.preferences.SetBiometricPreferenceUseCase
import org.expenny.core.domain.usecase.preferences.SetReminderPreferenceUseCase
import org.expenny.core.domain.usecase.preferences.SetReminderTimePreferenceUseCase
import org.expenny.core.domain.usecase.profile.DeleteCurrentProfileDataUseCase
import org.expenny.core.domain.usecase.profile.DeleteCurrentProfileUseCase
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.domain.usecase.profile.GetProfilesUseCase
import org.expenny.core.domain.usecase.profile.SetCurrentProfileUseCase
import org.expenny.core.model.biometric.BiometricStatus.AvailableButNotEnrolled
import org.expenny.core.model.biometric.BiometricStatus.Ready
import org.expenny.core.model.profile.Profile
import org.expenny.core.ui.data.ItemUi
import org.expenny.core.ui.data.SingleSelectionUi
import org.expenny.core.ui.extensions.labelResId
import org.expenny.core.ui.mapper.ProfileMapper
import org.expenny.feature.settings.contract.SettingsAction
import org.expenny.feature.settings.contract.SettingsEvent
import org.expenny.feature.settings.contract.SettingsState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setCurrentProfile: SetCurrentProfileUseCase,
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val deleteCurrentProfile: DeleteCurrentProfileUseCase,
    private val deleteCurrentProfileData: DeleteCurrentProfileDataUseCase,
    private val getProfiles: GetProfilesUseCase,
    private val getBiometricStatus: GetBiometricStatusUseCase,
    private val setBiometricPreference: SetBiometricPreferenceUseCase,
    private val getBiometricPreference: GetBiometricPreferenceUseCase,
    private val deletePasscodePreference: DeletePasscodePreferenceUseCase,
    private val getReminderPreference: GetReminderPreferenceUseCase,
    private val getReminderTimePreference: GetReminderTimePreferenceUseCase,
    private val setReminderPreference: SetReminderPreferenceUseCase,
    private val setReminderTimePreference: SetReminderTimePreferenceUseCase,
    private val getPasscodePreference: GetPasscodePreferenceUseCase,
    private val getCanSendAlarms: GetCanSendAlarmsUseCase,
    private val setApplicationTheme: SetApplicationThemeUseCase,
    private val getApplicationTheme: GetApplicationThemeUseCase,
    private val getInstallationId: GetInstallationIdUseCase,
    private val profileMapper: ProfileMapper,
) : ExpennyViewModel<SettingsAction>(), ContainerHost<SettingsState, SettingsEvent> {

    private var profiles: List<Profile> = emptyList()
    private val languages: List<ApplicationLanguage> = ApplicationLanguage.entries
    private val themes: List<ApplicationTheme> = ApplicationTheme.entries

    override val container = container<SettingsState, SettingsEvent>(
        initialState = SettingsState(),
        buildSettings = { exceptionHandler = defaultCoroutineExceptionHandler() }
    ) {
        coroutineScope {
            setCurrentLanguage()
            launch { subscribeToCurrentProfile() }
            launch { subscribeToProfiles() }
            launch { subscribeToThemePreference() }
            launch { subscribeToPasscodePreference() }
            launch { subscribeToBiometricPreference() }
            launch { subscribeToReminderTimePreference() }
            launch { subscribeToReminderPreference() }
            launch { subscribeToInstallationId() }
        }
    }

    override fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnSettingsItemTypeClick -> handleOnSettingsItemTypeClick(action)
            is SettingsAction.OnBackClick -> handleOnBackClick()
            is SettingsAction.OnCopyInstallationIdClick -> handleOnCopyInstallationIdClick(action)
            is SettingsAction.Dialog.OnDialogDismiss -> handleOnDialogDismiss()
            is SettingsAction.Dialog.OnLanguageSelect -> handleOnLanguageSelect(action)
            is SettingsAction.Dialog.OnThemeSelect -> handleOnThemeSelect(action)
            is SettingsAction.Dialog.OnReminderTimeChange -> handleOnReminderTimeChange(action)
            is SettingsAction.Dialog.OnProfileActionTypeSelect -> handleOnProfileActionTypeSelect(action)
            is SettingsAction.Dialog.OnCreateProfileClick -> handleOnCreateProfileClick()
            is SettingsAction.Dialog.OnSwitchProfileClick -> handleOnSwitchProfileClick(action)
            is SettingsAction.Dialog.OnDeleteApplicationDataDialogConfirm -> handleOnDeleteApplicationDataDialogConfirm()
            is SettingsAction.Dialog.OnDeleteProfileDataDialogConfirm -> handleOnDeleteProfileDataDialogConfirm()
            is SettingsAction.Dialog.OnDeleteProfileDialogConfirm -> handleOnDeleteProfileDialogConfirm()
        }
    }

    private fun handleOnProfileActionTypeSelect(action: SettingsAction.Dialog.OnProfileActionTypeSelect) = intent {
        when (action.type) {
            ProfileActionType.CreateProfile -> {
                handleOnCreateProfileClick()
            }
            ProfileActionType.SwitchProfile -> {
                reduce {
                    state.copy(
                        dialog = SettingsState.Dialog.ProfileSelectionDialog(
                            data = profiles.mapToItemUi(),
                            selection = SingleSelectionUi(state.profile?.id)
                        )
                    )
                }
            }
            ProfileActionType.DeleteProfile -> {
                reduce { state.copy(dialog = SettingsState.Dialog.DeleteProfileDialog) }
            }
            ProfileActionType.DeleteProfileData -> {
                reduce { state.copy(dialog = SettingsState.Dialog.DeleteProfileDataDialog) }
            }
        }
    }

    private fun handleOnDeleteProfileDialogConfirm() = intent {
        val nextProfile = getCurrentProfile().first()!!.let { currentProfile ->
            getProfiles().first().firstOrNull { it.id != currentProfile.id }
        }

        if (nextProfile != null) {
            deleteCurrentProfile()
            setCurrentProfile(SetCurrentProfileUseCase.Params(nextProfile.id))
            postSideEffect(SettingsEvent.RestartApplication())
        } else {
            postSideEffect(SettingsEvent.RestartApplication(isDataCleanupRequested = true))
        }
    }

    private fun handleOnDeleteProfileDataDialogConfirm() = intent {
        deleteCurrentProfileData()
        postSideEffect(SettingsEvent.RestartApplication())
    }

    private fun handleOnDeleteApplicationDataDialogConfirm() = intent {
        postSideEffect(SettingsEvent.RestartApplication(isDataCleanupRequested = true))
    }

    private fun handleOnCreateProfileClick() = intent {
        dismissDialog()
        postSideEffect(SettingsEvent.NavigateToCreateProfile)
    }

    private fun handleOnSwitchProfileClick(action: SettingsAction.Dialog.OnSwitchProfileClick) = intent {
        dismissDialog()
        action.selection.value?.let {
            setCurrentProfile(SetCurrentProfileUseCase.Params(it))
            postSideEffect(SettingsEvent.RestartApplication())
        }
    }

    private fun handleOnReminderTimeChange(action: SettingsAction.Dialog.OnReminderTimeChange) = intent {
        setReminderTimePreference(action.time)
    }

    private fun handleOnBackClick() = intent {
        postSideEffect(SettingsEvent.NavigateBack)
    }

    private fun handleOnCopyInstallationIdClick(action: SettingsAction.OnCopyInstallationIdClick) = intent {
        postSideEffect(SettingsEvent.CopyInstallationId(action.installationId))
    }

    private fun handleOnSettingsItemTypeClick(action: SettingsAction.OnSettingsItemTypeClick) = intent {
        when (action.type) {
            SettingsItemType.Profile -> {
                reduce { state.copy(dialog = SettingsState.Dialog.ProfileActionsDialog) }
            }
            SettingsItemType.Theme -> {
                reduce {
                    state.copy(
                        dialog = SettingsState.Dialog.ThemesSelectionDialog(
                            data = themes.mapToItemUi(),
                            selection = SingleSelectionUi(state.theme)
                        )
                    )
                }
            }
            SettingsItemType.Language -> {
                reduce {
                    state.copy(
                        dialog = SettingsState.Dialog.LanguagesSelectionDialog(
                            data = languages.mapToItemUi(),
                            selection = SingleSelectionUi(state.language)
                        )
                    )
                }
            }
            SettingsItemType.Currencies -> {
                postSideEffect(SettingsEvent.NavigateToCurrencies)
            }
            SettingsItemType.Passcode -> {
                if (state.isUsePasscodeSelected) {
                    deletePasscodePreference()
                    setBiometricPreference(false)
                } else {
                    postSideEffect(SettingsEvent.NavigateToCreatePasscode)
                }
            }
            SettingsItemType.Biometric -> {
                if (state.isUseBiometricSelected) {
                    setBiometricPreference(false)
                } else {
                    val biometricStatus = getBiometricStatus()
                    if (biometricStatus == AvailableButNotEnrolled) {
                        postSideEffect(SettingsEvent.NavigateToSystemSecuritySettings)
                    } else if (biometricStatus == Ready) {
                        setBiometricPreference(true)
                    }
                }
            }
            SettingsItemType.Reminder -> {
                if (getCanSendAlarms()) {
                    setReminderPreference(!state.isReminderSelected)
                } else {
                    postSideEffect(SettingsEvent.NavigateToSystemAlarmSettings)
                }
            }
            SettingsItemType.ReminderTime -> {
                reduce { state.copy(dialog = SettingsState.Dialog.ReminderTimeDialog) }
            }
            SettingsItemType.Categorization -> {
                postSideEffect(SettingsEvent.NavigateToCategoriesList)
            }
            SettingsItemType.DeleteApplicationData -> {
                reduce { state.copy(dialog = SettingsState.Dialog.DeleteApplicationDataDialog) }
            }
            else -> {}
        }
    }

    private fun handleOnLanguageSelect(action: SettingsAction.Dialog.OnLanguageSelect) {
        action.selection.value?.let {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(it.tag))
            intent {
                reduce { state.copy(language = it) }
                dismissDialog()
            }
        }
    }

    private fun handleOnDialogDismiss() = intent {
        dismissDialog()
    }

    private fun handleOnThemeSelect(action: SettingsAction.Dialog.OnThemeSelect) = intent {
        action.selection.value?.let {
            setApplicationTheme(SetApplicationThemeUseCase.Params(it))
            reduce { state.copy(theme = it) }
            dismissDialog()
        }
    }

    private fun subscribeToCurrentProfile() = intent {
        getCurrentProfile().filterNotNull().collect { profile ->
            reduce {
                state.copy(profile = profileMapper(profile))
            }
        }
    }

    private fun subscribeToProfiles() = intent {
        getProfiles().collect { newProfiles ->
            val profileActions = buildList {
                if (newProfiles.size > 1) add(ProfileActionType.SwitchProfile)
                add(ProfileActionType.CreateProfile)
                add(ProfileActionType.DeleteProfileData)
                add(ProfileActionType.DeleteProfile)
            }
            reduce { state.copy(profileActions = profileActions) }
            profiles = newProfiles
        }
    }

    private fun subscribeToPasscodePreference() = intent {
        getPasscodePreference()
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
            getBiometricPreference().collect {
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

    private fun subscribeToReminderTimePreference() = intent {
        getReminderTimePreference().collect {
            reduce {
                state.copy(reminderTime = it)
            }
        }
    }

    private fun subscribeToReminderPreference() = intent {
        getReminderPreference().collect {
            reduce {
                state.copy(isReminderTimeEnabled = it, isReminderSelected = it)
            }
        }
    }

    private fun subscribeToInstallationId() = intent {
        getInstallationId().collect {
            reduce { state.copy(installationId = it) }
        }
    }

    private fun setCurrentLanguage() {
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
        intent {
            reduce { state.copy(language = selectedLanguage) }
        }
    }

    private fun subscribeToThemePreference() = intent {
        getApplicationTheme().collect {
            reduce { state.copy(theme = it) }
        }
    }

    private suspend fun SimpleSyntax<SettingsState, SettingsEvent>.dismissDialog() {
        reduce { state.copy(dialog = null) }
    }

    @JvmName("mapProfileToItemUi")
    private fun List<Profile>.mapToItemUi() = map {
        ItemUi(it.id, provideString(fromStr(it.name.join(it.currencyUnit.code))))
    }

    @JvmName("mapApplicationLanguageToItemUi")
    private fun List<ApplicationLanguage>.mapToItemUi() = map {
        ItemUi(it, provideString(fromRes(it.labelResId)))
    }

    @JvmName("mapApplicationThemeToItemUi")
    private fun List<ApplicationTheme>.mapToItemUi() = map {
        ItemUi(it, provideString(fromRes(it.labelResId)))
    }
}