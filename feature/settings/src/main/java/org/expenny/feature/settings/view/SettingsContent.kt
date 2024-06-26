package org.expenny.feature.settings.view

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import org.expenny.core.common.types.SettingsItemType
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyToolbar
import org.expenny.feature.settings.contract.SettingsAction
import org.expenny.feature.settings.contract.SettingsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsContent(
    state: SettingsState,
    scope: CoroutineScope,
    profileActionsSheetState: SheetState,
    onAction: (SettingsAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollState = rememberScrollState()

    SettingsDialog(
        state = state,
        scope = scope,
        profileActionsSheetState = profileActionsSheetState,
        onDialogAction = onAction
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExpennyToolbar(
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    ToolbarIcon(
                        painter = painterResource(R.drawable.ic_back),
                        onClick = { onAction(SettingsAction.OnBackClick) }
                    )
                },
                title = {
                    ToolbarTitle(text = stringResource(R.string.settings_label))
                }
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
            onSettingsItemTypeClick = { onAction(SettingsAction.OnSettingsItemTypeClick(it)) }
        )
    }
}

@Composable
internal fun SettingsList(
    modifier: Modifier,
    state: SettingsState,
    onSettingsItemTypeClick: (SettingsItemType) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        SettingsGeneralSection(
            profileName = state.profile?.displayName,
            onProfileClick = { onSettingsItemTypeClick(SettingsItemType.Profile) },
            onCategorizationClick = { onSettingsItemTypeClick(SettingsItemType.Categorization) },
            onCurrenciesClick = { onSettingsItemTypeClick(SettingsItemType.Currencies) },
        )
        SettingsPreferencesSection(
            language = state.language,
            theme = state.theme,
            onLanguageClick = { onSettingsItemTypeClick(SettingsItemType.Language) },
            onThemeClick = { onSettingsItemTypeClick(SettingsItemType.Theme) },
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
            onUseBiometricClick = { onSettingsItemTypeClick(SettingsItemType.Biometric) },
            onBackupClick = { onSettingsItemTypeClick(SettingsItemType.Backup) },
        )
        SettingsMoreSection(
            onAboutClick = { onSettingsItemTypeClick(SettingsItemType.About) },
            onRateApplicationClick = { onSettingsItemTypeClick(SettingsItemType.RateApplication) },
            onClearAllDataClick = { onSettingsItemTypeClick(SettingsItemType.DeleteApplicationData) },
        )
    }
}
