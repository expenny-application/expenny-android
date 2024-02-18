package org.expenny.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.data.ui.IconUi
import org.expenny.core.ui.data.ui.RecordUi

@Composable
fun ExpennyRecord(
    modifier: Modifier = Modifier,
    record: RecordUi.Item,
) {
    val icon = record.transferAmount?.let { IconUi.transfer }
        ?: record.category?.icon
        ?: IconUi.unknown

    val title = record.title ?: stringResource(R.string.unknown_label)

    RecordContent(
        modifier = modifier,
        iconResId = icon.iconResId,
        iconColor = icon.color,
        title = title,
        subtitle = record.subtitle,
        amount = record.amount.displayValue,
        transferAmount = record.transferAmount?.displayValue,
        description = record.description,
        labels = record.labels,
        attachmentsCount = record.attachmentsCount,
    )
}

@Composable
private fun RecordContent(
    modifier: Modifier = Modifier,
    iconResId: Int,
    iconColor: Color,
    title: String,
    subtitle: String,
    amount: String,
    transferAmount: String?,
    description: String,
    labels: List<String>,
    attachmentsCount: Int,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        ExpennyIconBox(
            icon = painterResource(iconResId),
            color = iconColor,
            background = MaterialTheme.colorScheme.surface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Title(title)
                    Subtitle(subtitle)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Amount(amount)
                    TransferAmount(transferAmount)
                }
            }
            Description(description)
            Labels(
                modifier = Modifier.fillMaxWidth(),
                attachmentsCount = attachmentsCount,
                labels = labels,
            )
        }
    }
}

@Composable
private fun Title(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Composable
private fun Subtitle(subtitle: String) {
    Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Composable
private fun Amount(amount: String) {
    Text(
        text = amount,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1
    )
}

@Composable
private fun TransferAmount(transferAmount: String?) {
    if (transferAmount != null) {
        Text(
            text = transferAmount,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

@Composable
private fun Description(description: String) {
    if (description.isNotBlank()) {
        Text(
            text = description,
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
        )
    }
}

@Composable
private fun Labels(
    modifier: Modifier = Modifier,
    attachmentsCount: Int,
    labels: List<String>,
) {
    if (labels.isNotEmpty() || attachmentsCount > 0) {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (attachmentsCount > 0) {
                item {
                    ExpennyLabel(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_camera),
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = attachmentsCount.toString())
                        }
                    )
                }
            }
            items(items = labels) {
                ExpennyLabel(
                    label = {
                        Text(text = it)
                    }
                )
            }
        }
    }
}
