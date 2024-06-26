package org.expenny.feature.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyCard
import org.expenny.core.ui.components.ExpennySection
import org.expenny.core.ui.components.ExpennySwitch
import org.expenny.core.ui.extensions.label

@Composable
internal fun SettingsMoreSection(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit,
    onRateApplicationClick: () -> Unit,
    onClearAllDataClick: () -> Unit,
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.more_label)
    ) {
        SectionItem(
            title = stringResource(R.string.about_label),
            icon = painterResource(R.drawable.ic_smile),
            onClick = onAboutClick
        )
        SectionItem(
            title = stringResource(R.string.rate_application_label),
            icon = painterResource(R.drawable.ic_rate),
            onClick = onRateApplicationClick
        )
        SectionActionItem(
            title = stringResource(R.string.delete_application_data_label),
            icon = painterResource(R.drawable.ic_delete),
            isSensitive = true,
            onClick = onClearAllDataClick
        )
    }
}

@Composable
internal fun SettingsSecuritySection(
    modifier: Modifier = Modifier,
    isUsePasscodeSelected: Boolean,
    isUseBiometricSelected: Boolean,
    isBiometricEnabled: Boolean,
    onSetPasscodeClick: () -> Unit,
    onUseBiometricClick: () -> Unit,
    onBackupClick: () -> Unit,
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.security_label)
    ) {
        SectionSwitchItem(
            title = stringResource(R.string.use_passcode_label),
            icon = painterResource(R.drawable.ic_passcode),
            isSelected = isUsePasscodeSelected,
            onClick = onSetPasscodeClick
        )
        SectionSwitchItem(
            isEnabled = isBiometricEnabled,
            isSelected = isUseBiometricSelected,
            title = stringResource(R.string.use_bioemtric_label),
            icon = painterResource(R.drawable.ic_biometric),
            onClick = onUseBiometricClick
        )
        SectionItem(
            title = stringResource(R.string.backup_label),
            icon = painterResource(R.drawable.ic_storage),
            onClick = onBackupClick
        )
    }
}

@Composable
internal fun SettingsNotificationsSection(
    modifier: Modifier = Modifier,
    isReminderSelected: Boolean,
    isReminderTimeEnabled: Boolean,
    reminderTime: String,
    onReminderClick: () -> Unit,
    onReminderTimeClick: () -> Unit,
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.notifications_label)
    ) {
        SectionSwitchItem(
            title = stringResource(R.string.reminder_label),
            icon = painterResource(R.drawable.ic_bell),
            isSelected = isReminderSelected,
            onClick = onReminderClick
        )
        SectionSelectionItem(
            isEnabled = isReminderTimeEnabled,
            title = stringResource(R.string.reminder_time_label),
            icon = painterResource(R.drawable.ic_time),
            value = reminderTime,
            onClick = onReminderTimeClick
        )
    }
}

@Composable
internal fun SettingsPreferencesSection(
    modifier: Modifier = Modifier,
    language: ApplicationLanguage?,
    theme: ApplicationTheme?,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.preferences_label)
    ) {
        SectionSelectionItem(
            title = stringResource(R.string.language_label),
            icon = painterResource(R.drawable.ic_language),
            value = language?.label.orEmpty(),
            onClick = onLanguageClick
        )
        SectionSelectionItem(
            title = stringResource(R.string.theme_label),
            icon = painterResource(R.drawable.ic_theme),
            value = theme?.label.orEmpty(),
            onClick = onThemeClick
        )
    }
}

@Composable
internal fun SettingsGeneralSection(
    modifier: Modifier = Modifier,
    profileName: String?,
    onProfileClick: () -> Unit,
    onCategorizationClick: () -> Unit,
    onCurrenciesClick: () -> Unit,
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.general_label)
    ) {
        profileName?.let {
            SectionSelectionItem(
                title = profileName,
                icon = painterResource(R.drawable.ic_profile),
                onClick = onProfileClick
            )
        }
        SectionItem(
            title = stringResource(R.string.currencies_label),
            icon = painterResource(R.drawable.ic_currency),
            onClick = onCurrenciesClick
        )
        SectionItem(
            title = stringResource(R.string.categorizations_label),
            icon = painterResource(R.drawable.ic_category),
            onClick = onCategorizationClick
        )
    }
}

@Composable
private fun SectionActionItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Painter,
    isEnabled: Boolean = true,
    isSensitive: Boolean = false,
    onClick: () -> Unit
) {
    SectionItem(
        modifier = modifier,
        isEnabled = isEnabled,
        isSensitive = isSensitive,
        onClick = onClick,
        leadingContent = {
            Icon(
                painter = icon,
                contentDescription = null
            )
        },
        title = {
            Text(text = title)
        }
    )
}

@Composable
private fun SectionSelectionItem(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    title: String,
    icon: Painter,
    value: String = "",
    onClick: () -> Unit
) {
    SectionItem(
        modifier = modifier,
        isEnabled = isEnabled,
        onClick = onClick,
        leadingContent = {
            Icon(
                painter = icon,
                contentDescription = null
            )
        },
        title = {
            Text(text = title)
        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = value)
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                )
            }
        }
    )
}

@Composable
private fun SectionSwitchItem(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isSelected: Boolean,
    title: String,
    description: String? = null,
    icon: Painter,
    onClick: () -> Unit
) {
    SectionItem(
        modifier = modifier,
        isEnabled = isEnabled,
        onClick = onClick,
        leadingContent = {
            Icon(
                painter = icon,
                contentDescription = null
            )
        },
        title = {
            Text(text = title)
        },
        description = {
            description?.let {
                Text(text = description, maxLines = 3)
            }
        },
        trailingContent = {
            ExpennySwitch(
                isEnabled = isEnabled,
                isSelected = isSelected,
                onClick = { onClick() }
            )
        }
    )
}

@Composable
private fun SectionItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Painter,
    onClick: () -> Unit
) {
    SectionItem(
        modifier = modifier,
        onClick = onClick,
        leadingContent = {
            Icon(
                painter = icon,
                contentDescription = null
            )
        },
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    )
}

@Composable
private fun SectionItem(
    modifier: Modifier = Modifier,
    isSensitive: Boolean = false,
    isEnabled: Boolean = true,
    title: @Composable () -> Unit,
    description: (@Composable () -> Unit)? = null,
    leadingContent: @Composable () -> Unit,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    val primaryContentColor = when {
        isEnabled && !isSensitive -> MaterialTheme.colorScheme.onSurface
        isEnabled && isSensitive -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface.copy(0.38f)
    }
    val secondaryContentColor = when {
        isEnabled && !isSensitive -> MaterialTheme.colorScheme.onSurfaceVariant
        isEnabled && isSensitive -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(0.38f)
    }

    ExpennyCard(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            if (isEnabled) onClick()
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalContentColor provides secondaryContentColor,
            ) {
                leadingContent()
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides primaryContentColor,
                    LocalTextStyle provides MaterialTheme.typography.bodyLarge
                ) {
                    title()
                }
                description?.let {
                    CompositionLocalProvider(
                        LocalContentColor provides secondaryContentColor,
                        LocalTextStyle provides MaterialTheme.typography.bodySmall
                    ) {
                        description()
                    }
                }
            }
            trailingContent?.let {
                CompositionLocalProvider(
                    LocalContentColor provides secondaryContentColor,
                    LocalTextStyle provides MaterialTheme.typography.bodyMedium
                ) {
                    trailingContent()
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    modifier: Modifier = Modifier,
    title: String,
    sectionItems: @Composable ColumnScope.() -> Unit
) {
    ExpennySection(
        modifier = modifier,
        title = title
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            sectionItems()
        }
    }
}