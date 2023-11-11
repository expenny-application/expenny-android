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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import org.expenny.feature.settings.model.SettingsItemType
import org.expenny.feature.settings.view.SettingsDataSection
import org.expenny.feature.settings.view.SettingsGeneralSection
import org.expenny.feature.settings.view.SettingsLanguageDialog
import org.expenny.feature.settings.view.SettingsMoreSection
import org.expenny.feature.settings.view.SettingsNotificationsSection
import org.expenny.feature.settings.view.SettingsProfileSection
import org.expenny.feature.settings.view.SettingsSecuritySection
import org.expenny.feature.settings.view.SettingsSensitiveSection
import org.expenny.feature.settings.view.SettingsThemeDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsContent(
    state: State,
    onAction: (Action) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollState = rememberScrollState()

    when (state.dialog) {
        is State.Dialog.ThemeDialog -> {
            if (state.selectedTheme != null) {
                SettingsThemeDialog(
                    selectedTheme = state.selectedTheme,
                    onThemeSelect = { onAction(Action.OnThemeSelect(it)) },
                    onDismiss = { onAction(Action.OnThemeDialogDismiss) }
                )
            }
        }
        is State.Dialog.LanguageDialog -> {
            SettingsLanguageDialog(
                locales = state.languages,
                selectedLanguage = state.selectedLanguage,
                onLanguageSelect = { onAction(Action.OnLanguageSelect(it)) },
                onDismiss = { onAction(Action.OnLanguageDialogDismiss) }
            )
        }
        else -> {}
    }

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
                profileName = it.name,
                profileCurrency = it.currency,
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
            onLabelsClick = { onSettingsItemTypeClick(SettingsItemType.Labels) }
        )
        SettingsDataSection(
            onBackupClick = { onSettingsItemTypeClick(SettingsItemType.Backup) },
            onExportClick = { onSettingsItemTypeClick(SettingsItemType.Export) },
            onImportsClick = { onSettingsItemTypeClick(SettingsItemType.Imports) }
        )
        SettingsNotificationsSection(
            isUpdateRatesSelected = false, // TODO
            onReminderClick = { onSettingsItemTypeClick(SettingsItemType.Reminder) },
            onUpdateRatesClick = { onSettingsItemTypeClick(SettingsItemType.UpdateRates) }
        )
        SettingsSecuritySection(
            isUsePasscodeSelected = state.isPasscodeEnabled,
            isUseFingerprintSelected = false, // TODO
            onSetPinCodeClick = { onSettingsItemTypeClick(SettingsItemType.Passcode) },
            onUseFingerprintClick = { onSettingsItemTypeClick(SettingsItemType.Fingerprint) }
        )
        SettingsMoreSection(
            onAboutClick = { onSettingsItemTypeClick(SettingsItemType.About) },
            onRateApplicationClick = { onSettingsItemTypeClick(SettingsItemType.RateApplication) }
        )
        SettingsSensitiveSection(
            onDeleteAllDataClick = { onSettingsItemTypeClick(SettingsItemType.DeleteAllData) }
        )
    }
}
