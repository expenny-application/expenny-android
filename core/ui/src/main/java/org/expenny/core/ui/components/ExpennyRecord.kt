package org.expenny.core.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.data.ui.*
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.extensions.noRippleClickable


@Composable
fun ExpennyRecord(
    modifier: Modifier = Modifier,
    record: RecordUi.Item,
    onLabelClick: (Long) -> Unit = {}
) {
    when (record) {
        is RecordUi.Item.Transaction -> {
            RecordContent(
                modifier = modifier,
                iconResId = record.category?.icon?.iconResId,
                iconColor = record.category?.icon?.color,
                category = record.category?.name,
                amount = record.postedAmount.displayValue,
                account = record.account,
                description = record.description,
                labels = record.labels,
                receiptsCount = record.receiptsCount,
                onLabelClick = onLabelClick
            )
        }
        is RecordUi.Item.Transfer -> {
            RecordContent(
                modifier = modifier,
                iconResId = R.drawable.ic_swap_horizontal,
                iconColor = MaterialTheme.colorScheme.primary,
                category = stringResource(R.string.transfer_label),
                amount = record.postedAmount.displayValue,
                account = record.account,
                transferAmount = record.clearedAmount.displayValue,
                transferAccount = record.transferAccount,
                description = record.description,
                labels = record.labels,
                receiptsCount = record.receiptsCount,
                onLabelClick = onLabelClick
            )
        }
    }
}

@Composable
private fun RecordContent(
    modifier: Modifier = Modifier,
    iconResId: Int?,
    iconColor: Color?,
    category: String?,
    amount: String,
    account: String,
    transferAmount: String? = null,
    transferAccount: String? = null,
    description: String,
    labels: List<LabelUi>,
    receiptsCount: Int,
    onLabelClick: (Long) -> Unit
) {
    var showTransferAmount by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        ExpennyIconBox(
            icon = iconResId?.let { painterResource(it) } ?: painterResource(R.drawable.ic_unknown),
            color = iconColor ?: MaterialTheme.colorScheme.onSurfaceVariant,
            background = MaterialTheme.colorScheme.surface
        )

        Column {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium,
                LocalContentColor provides MaterialTheme.colorScheme.onSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    ExpennyText(text = category ?: stringResource(R.string.unknown_label))

                    Spacer(modifier = Modifier.width(16.dp))

                    if (transferAmount != null && amount != transferAmount) {
                        AnimatedContent(
                            targetState = when (showTransferAmount) {
                                true -> transferAmount
                                false -> amount
                            },
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "ToggleAmountAnimation"
                        ) { content ->
                            ExpennyText(
                                modifier = Modifier.noRippleClickable {
                                    showTransferAmount = !showTransferAmount
                                },
                                text = content,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        ExpennyText(text = amount)
                    }
                }
            }

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium,
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Spacer(modifier = Modifier.height(2.dp))
                ExpennyText(
                    text = buildString {
                        append(account)
                        if (transferAccount != null) {
                            append(" - ")
                            append(transferAccount)
                        }
                    }
                )
                if (description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    ExpennyText(
                        text = description,
                        maxLines = 2,
                        style = LocalTextStyle.current.copy(
                            fontStyle = FontStyle.Italic
                        )
                    )
                }
                if (labels.isNotEmpty() || receiptsCount > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        receiptsCountItem(receiptsCount = receiptsCount)
                        labelsItems(labels = labels, onLabelClick = onLabelClick)
                    }
                }
            }
        }
    }
}

private fun LazyListScope.labelsItems(
    labels: List<LabelUi>,
    onLabelClick: (Long) -> Unit
) {
    items(
        items = labels,
        key = { it.id }
    ) { label ->
        ExpennyLabel(
            contentColor = label.color,
            onClick = {
                onLabelClick(label.id)
            },
            label = {
                ExpennyText(text = label.name)
            }
        )
    }
}

private fun LazyListScope.receiptsCountItem(receiptsCount: Int) {
    if (receiptsCount > 0) {
        item {
            ExpennyLabel(
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = null
                    )
                },
                label = {
                    ExpennyText(text = receiptsCount.toString())
                }
            )
        }
    }
}
