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
        SectionSelectionItem(
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
internal fun SettingsReportsSection(
    modifier: Modifier = Modifier,
    isCrashReportsSelected: Boolean,
    isAnalyticsReportsSelected: Boolean,
    onCrashReportsClick: () -> Unit,
    onAnalyticsReportsClick: () -> Unit,
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.reports_label)
    ) {
        SectionSwitchItem(
            title = stringResource(R.string.allow_crash_reports_label),
            icon = painterResource(R.drawable.ic_crash),
            description = stringResource(R.string.crash_reports_paragraph),
            isSelected = isCrashReportsSelected,
            onClick = onCrashReportsClick
        )
        SectionSwitchItem(
            title = stringResource(R.string.allow_analytics_label),
            icon = painterResource(R.drawable.ic_analytics_tracking),
            description = stringResource(R.string.analytics_reports_paragraph),
            isSelected = isAnalyticsReportsSelected,
            onClick = onAnalyticsReportsClick
        )
    }
}

@Composable
internal fun SettingsSecuritySection(
    modifier: Modifier = Modifier,
    isSetPinCodeSelected: Boolean,
    isUseFingerprintSelected: Boolean,
    onSetPinCodeClick: () -> Unit,
    onUseFingerprintClick: () -> Unit,
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.security_label)
    ) {
        SectionSwitchItem(
            title = stringResource(R.string.set_pin_code_label),
            icon = painterResource(R.drawable.ic_pin_code),
            isSelected = isSetPinCodeSelected,
            onClick = onSetPinCodeClick
        )
        SectionSwitchItem(
            title = stringResource(R.string.use_fingerprint_label),
            icon = painterResource(R.drawable.ic_fingerprint),
            isSelected = isUseFingerprintSelected,
            onClick = onUseFingerprintClick
        )
    }
}

@Composable
internal fun SettingsNotificationsSection(
    modifier: Modifier = Modifier,
    isUpdateRatesSelected: Boolean,
    onReminderClick: () -> Unit,
    onUpdateRatesClick: () -> Unit,
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.notifications_label)
    ) {
        SectionSelectionItem(
            title = stringResource(R.string.reminder_label),
            icon = painterResource(R.drawable.ic_reminder),
            onClick = onReminderClick
        )
        SectionSwitchItem(
            title = stringResource(R.string.update_rates_label),
            icon = painterResource(R.drawable.ic_update),
            isSelected = isUpdateRatesSelected,
            onClick = onUpdateRatesClick
        )
    }
}

@Composable
internal fun SettingsDataSection(
    modifier: Modifier = Modifier,
    onBackupClick: () -> Unit,
    onExportClick: () -> Unit,
    onImportsClick: () -> Unit
) {
    SettingsSection(
        modifier = modifier.fillMaxWidth(),
        title = stringResource(R.string.data_label)
    ) {
        SectionSelectionItem(
            title = stringResource(R.string.backup_label),
            icon = painterResource(R.drawable.ic_storage),
            onClick = onBackupClick
        )
        SectionSelectionItem(
            title = stringResource(R.string.export_to_file_label),
            icon = painterResource(R.drawable.ic_export),
            onClick = onExportClick
        )
        SectionSelectionItem(
            title = stringResource(R.string.imports_label),
            icon = painterResource(R.drawable.ic_imports),
            onClick = onImportsClick
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
        SectionSelectionItem(
            title = stringResource(R.string.categorizations_label),
            icon = painterResource(R.drawable.ic_rule),
            onClick = onCategorizationClick
        )
        SectionSelectionItem(
            title = stringResource(R.string.currencies_label),
            icon = painterResource(R.drawable.ic_money),
            onClick = onCurrenciesClick
        )
        SectionSelectionItem(
            title = stringResource(R.string.labels_label),
            icon = painterResource(R.drawable.ic_label_outlined),
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
    title: String,
    icon: Painter,
    value: String = "",
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
    title: String,
    description: String? = null,
    icon: Painter,
    isSelected: Boolean,
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
        },
        description = {
            description?.let {
                ExpennyText(text = description, maxLines = 3)
            }
        },
        trailingContent = {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Switch(
                    checked = isSelected,
                    onCheckedChange = { onClick() }
                )
            }
        }
    )
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

@Composable
private fun SectionItem(
    modifier: Modifier = Modifier,
    leadingContent: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    ExpennyCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
            ) {
                leadingContent()
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface,
                    LocalTextStyle provides MaterialTheme.typography.titleMedium
                ) {
                    title()
                }
                description?.let {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
                        LocalTextStyle provides MaterialTheme.typography.bodyMedium
                    ) {
                        description()
                    }
                }
            }
            trailingContent?.let {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
                    LocalTextStyle provides MaterialTheme.typography.bodyMedium
                ) {
                    trailingContent()
                }
            }
        }
    }
}