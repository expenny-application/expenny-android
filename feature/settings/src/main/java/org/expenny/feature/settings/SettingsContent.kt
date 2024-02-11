package org.expenny.feature.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import org.expenny.feature.settings.model.SettingsItemType
import org.expenny.feature.settings.view.SettingsDataSection
import org.expenny.feature.settings.view.SettingsDialog
import org.expenny.feature.settings.view.SettingsGeneralSection
import org.expenny.feature.settings.view.SettingsMoreSection
import org.expenny.feature.settings.view.SettingsNotificationsSection
import org.expenny.feature.settings.view.SettingsProfileSection
import org.expenny.feature.settings.view.SettingsSecuritySection
import org.expenny.feature.settings.view.SettingsSensitiveSection

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsContent(
    state: State,
    scope: CoroutineScope,
    profileActionsSheetState: SheetState,
    onAction: (Action) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollState = rememberScrollState()

    SettingsDialog(
        state = state,
        scope = scope,
        profileActionsSheetState = profileActionsSheetState,
        onDialogAction = { onAction(it) }
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SettingsToolbar(
                scrollBehavior = scrollBehavior,
                onBackClick = { onAction(Action.OnBackClick) }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        SettingsList(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp),
            state = state,
            onSettingsItemTypeClick = { onAction(Action.OnSettingsItemTypeClick(it)) }
        )
    }
}

@Composable
internal fun SettingsList(
    modifier: Modifier,
    state: State,
    onSettingsItemTypeClick: (SettingsItemType) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        state.currentProfile?.let {
            SettingsProfileSection(
                profileName = it.displayName,
                onProfileClick = { onSettingsItemTypeClick(SettingsItemType.Profile) }
            )
        }
        SettingsGeneralSection(
            language = state.selectedLanguage,
            theme = state.selectedTheme,
            onLanguageClick = { onSettingsItemTypeClick(SettingsItemType.Language) },
            onThemeClick = { onSettingsItemTypeClick(SettingsItemType.Theme) },
            onCategorizationClick = { onSettingsItemTypeClick(SettingsItemType.Categorization) },
            onCurrenciesClick = { onSettingsItemTypeClick(SettingsItemType.Currencies) },
        )
        SettingsDataSection(
            onBackupClick = { onSettingsItemTypeClick(SettingsItemType.Backup) },
            onImportsExportsClick = { onSettingsItemTypeClick(SettingsItemType.ImportsExports) },
        )
        SettingsNotificationsSection(
            isReminderSelected = state.isReminderSelected,
            isReminderTimeEnabled = state.isReminderTimeEnabled,
            reminderTime = state.reminderTimeString,
            onReminderClick = { onSettingsItemTypeClick(SettingsItemType.Reminder) },
            onReminderTimeClick = { onSettingsItemTypeClick(SettingsItemType.ReminderTime) },
        )
        SettingsSecuritySection(
            isUsePasscodeSelected = state.isUsePasscodeSelected,
            isUseBiometricSelected = state.isUseBiometricSelected,
            isBiometricEnabled = state.isBiometricEnabled,
            onSetPasscodeClick = { onSettingsItemTypeClick(SettingsItemType.Passcode) },
            onUseBiometricClick = { onSettingsItemTypeClick(SettingsItemType.Biometric) }
        )
        SettingsMoreSection(
            onAboutClick = { onSettingsItemTypeClick(SettingsItemType.About) },
            onRateApplicationClick = { onSettingsItemTypeClick(SettingsItemType.RateApplication) }
        )
        SettingsSensitiveSection(
            onClearAllDataClick = { onSettingsItemTypeClick(SettingsItemType.DeleteApplicationData) },
        )
    }
}
