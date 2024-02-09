package org.expenny.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.data.ui.RecordUi
import org.expenny.core.ui.extensions.noRippleClickable

@Composable
fun ExpennyRecord(
    modifier: Modifier = Modifier,
    record: RecordUi.Item,
    onLabelClick: (String) -> Unit = {}
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
    labels: List<String>,
    receiptsCount: Int,
    onLabelClick: (String) -> Unit
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
                    Text(text = category ?: stringResource(R.string.unknown_label))

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
                            Text(
                                modifier = Modifier.noRippleClickable {
                                    showTransferAmount = !showTransferAmount
                                },
                                text = content,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Text(text = amount)
                    }
                }
            }

            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium,
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
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
                    Text(
                        text = description,
                        maxLines = 2,
                        fontStyle = FontStyle.Italic
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
    labels: List<String>,
    onLabelClick: (String) -> Unit
) {
    items(items = labels) {
        ExpennyLabel(
            onClick = { onLabelClick(it) },
            label = {
                Text(text = it)
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
                    Text(text = receiptsCount.toString())
                }
            )
        }
    }
}
