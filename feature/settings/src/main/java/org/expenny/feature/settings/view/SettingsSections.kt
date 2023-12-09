package org.expenny.feature.settings.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.expenny.core.common.extensions.capitalCase
import org.expenny.core.common.types.ApplicationLanguage
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennySection
import org.expenny.core.ui.extensions.label
import org.expenny.core.ui.foundation.ExpennyCard
import org.expenny.core.ui.foundation.ExpennyText

@Composable
internal fun SettingsSensitiveSection(
    modifier: Modifier = Modifier,
    onDeleteAllDataClick: () -> Unit
) {
    Box(modifier = modifier) {
        SectionActionItem(
            title = stringResource(R.string.delete_all_data_label),
            icon = painterResource(R.drawable.ic_delete),
            isSensitive = true,
            onClick = onDeleteAllDataClick
        )
    }
}

@Composable
internal fun SettingsMoreSection(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit,
    onRateApplicationClick: () -> Unit,
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
        SectionActionItem(
            title = stringResource(R.string.rate_application_label),
            icon = painterResource(R.drawable.ic_rate),
            onClick = onRateApplicationClick
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
            description = stringResource(R.string.reminder_notification_description_label),
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
internal fun SettingsDataSection(
    modifier: Modifier = Modifier,
    onBackupClick: () -> Unit,
    onImportsExportsClick: () -> Unit,
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.data_label)
    ) {
        SectionItem(
            title = stringResource(R.string.backup_label),
            icon = painterResource(R.drawable.ic_storage),
            onClick = onBackupClick
        )
        SectionItem(
            title = stringResource(R.string.import_and_export_label),
            icon = painterResource(R.drawable.ic_import_export),
            onClick = onImportsExportsClick
        )
    }
}

@Composable
internal fun SettingsGeneralSection(
    modifier: Modifier = Modifier,
    language: ApplicationLanguage,
    theme: ApplicationTheme?,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onCategorizationClick: () -> Unit,
    onCurrenciesClick: () -> Unit,
    onLabelsClick: () -> Unit
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.general_label)
    ) {
        SectionSelectionItem(
            title = stringResource(R.string.language_label),
            icon = painterResource(R.drawable.ic_language),
            value = language.label,
            onClick = onLanguageClick
        )
        SectionSelectionItem(
            title = stringResource(R.string.theme_label),
            icon = painterResource(R.drawable.ic_theme),
            value = theme?.label.orEmpty(),
            onClick = onThemeClick
        )
        SectionItem(
            title = stringResource(R.string.categorizations_label),
            icon = painterResource(R.drawable.ic_category),
            onClick = onCategorizationClick
        )
        SectionItem(
            title = stringResource(R.string.currencies_label),
            icon = painterResource(R.drawable.ic_currency),
            onClick = onCurrenciesClick
        )
        SectionItem(
            title = stringResource(R.string.labels_label),
            icon = painterResource(R.drawable.ic_labels),
            onClick = onLabelsClick
        )
    }
}

@Composable
internal fun SettingsProfileSection(
    modifier: Modifier = Modifier,
    profileName: String,
    profileCurrency: String,
    onProfileClick: () -> Unit,
) {
    val circleColor = MaterialTheme.colorScheme.primaryContainer

    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.profile_label)
    ) {
        SectionItem(
            modifier = modifier,
            onClick = onProfileClick,
            leadingContent = {
                ExpennyText(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .drawBehind {
                            drawCircle(
                                color = circleColor,
                                radius = size.maxDimension
                            )
                        },
                    text = profileName.first().toString().capitalCase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            title = {
                ExpennyText(text = profileName)
            },
            trailingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExpennyText(text = profileCurrency)
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                    )
                }
            }
        )
    }
}

@Composable
private fun SectionActionItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Painter,
    isSensitive: Boolean = false,
    onClick: () -> Unit
) {
    val labelColor =
        if (isSensitive) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.onSurface

    val iconColor =
        if (isSensitive) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.onSurfaceVariant

    SectionItem(
        modifier = modifier,
        onClick = onClick,
        leadingContent = {
            Icon(
                painter = icon,
                tint = iconColor,
                contentDescription = null
            )
        },
        title = {
            ExpennyText(
                text = title,
                color = labelColor,
            )
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
            ExpennyText(text = title)
        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExpennyText(text = value)
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
            ExpennyText(text = title)
        },
        description = {
            description?.let {
                ExpennyText(text = description, maxLines = 3)
            }
        },
        trailingContent = {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Switch(
                    enabled = isEnabled,
                    checked = isSelected,
                    onCheckedChange = { onClick() }
                )
            }
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
            ExpennyText(text = title)
        }
    )
}

@Composable
private fun SectionItem(
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    leadingContent: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    val primaryContentColor =
        MaterialTheme.colorScheme.onSurface.copy(alpha = if (isEnabled) 1f else 0.38f)

    val secondaryContentColor =
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isEnabled) 1f else 0.38f)

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
                    LocalTextStyle provides MaterialTheme.typography.titleMedium
                ) {
                    title()
                }
                description?.let {
                    CompositionLocalProvider(
                        LocalContentColor provides secondaryContentColor,
                        LocalTextStyle provides MaterialTheme.typography.bodyMedium
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