package org.expenny.feature.records.details.view

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyChip
import org.expenny.core.ui.components.ExpennyInputField
import org.expenny.core.ui.components.ExpennySection
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.extensions.asRawString

@Composable
internal fun RecordDetailsAdditionsSection(
    modifier: Modifier = Modifier,
    labels: List<String>,
    descriptionState: InputUi,
    showSection: Boolean,
    receipts: List<Uri>,
    onLabelRemove: (String) -> Unit,
    onSelectLabelClick: () -> Unit,
    onAddReceiptClick: () -> Unit,
    onViewReceiptClick: (Uri) -> Unit,
    onDeleteReceiptClick: (Uri) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onVisibilityChange: (Boolean) -> Unit
) {
    ExpennySection(
        modifier = modifier,
        title = stringResource(R.string.additions_label),
        isExpanded = showSection,
        onClick = { onVisibilityChange(!showSection) },
    ) {
        Column(
            modifier = modifier.animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LabelsRow(
                modifier = Modifier.fillMaxWidth(),
                labels = labels,
                onRemoveLabelClick = onLabelRemove,
                onSelectLabelClick = onSelectLabelClick
            )
            DescriptionInputField(
                modifier = Modifier.fillMaxWidth(),
                state = descriptionState,
                onAddReceiptClick = onAddReceiptClick,
                onValueChange = onDescriptionChange
            )
            ReceiptsList(
                modifier = Modifier.fillMaxWidth(),
                receipts = receipts,
                onViewReceiptClick = onViewReceiptClick,
                onDeleteReceiptClick = onDeleteReceiptClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LabelsRow(
    modifier: Modifier = Modifier,
    labels: List<String>,
    onSelectLabelClick: () -> Unit,
    onRemoveLabelClick: (String) -> Unit,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        labels.forEach { label ->
            ExpennyChip(
                onClick = {},
                content = {
                    ChipText(text = label)
                },
                trailingIcon = {
                    ChipIcon(
                        painter = painterResource(R.drawable.ic_close),
                        onClick = {
                            onRemoveLabelClick(label)
                        }
                    )
                }
            )
        }
        ExpennyChip(
            onClick = onSelectLabelClick,
            content = {
                ChipText(text = stringResource(R.string.select_labels_label))
            },
            trailingIcon = {
                ChipIcon(painter = painterResource(R.drawable.ic_add))
            }
        )
    }
}

@Composable
private fun ReceiptsList(
    modifier: Modifier = Modifier,
    receipts: List<Uri>,
    onViewReceiptClick: (Uri) -> Unit,
    onDeleteReceiptClick: (Uri) -> Unit,
) {
    if (receipts.isNotEmpty()) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            receipts.forEach { receipt ->
                RecordDetailsReceipt(
                    modifier = Modifier.weight(1f),
                    uri = receipt,
                    onClick = {
                        onViewReceiptClick(receipt)
                    },
                    onDeleteClick = {
                        onDeleteReceiptClick(receipt)
                    }
                )
            }
        }
    }
}

@Composable
private fun DescriptionInputField(
    modifier: Modifier = Modifier,
    state: InputUi,
    onAddReceiptClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    with(state) {
        ExpennyInputField(
            modifier = modifier,
            isRequired = isRequired,
            isSingleLine = false,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.description_label),
            onValueChange = onValueChange,
            trailingContent = {
                IconButton(onClick = onAddReceiptClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = null,
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
    }
}
