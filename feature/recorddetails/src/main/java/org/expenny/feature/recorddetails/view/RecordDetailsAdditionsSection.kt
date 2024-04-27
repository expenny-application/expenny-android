package org.expenny.feature.recorddetails.view

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
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
import org.expenny.core.ui.components.ExpennySection
import org.expenny.core.ui.data.InputUi
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.components.ExpennyInputField
import org.expenny.feature.recorddetails.model.LabelsInputField

@Composable
internal fun RecordDetailsAdditionsSection(
    modifier: Modifier = Modifier,
    labelsInputFieldState: LabelsInputField,
    descriptionState: InputUi,
    showSection: Boolean,
    receipts: List<Uri>,
    onAddLabel: (String) -> Unit,
    onRemoveLabelAtIndex: (Int) -> Unit,
    onAddReceiptClick: () -> Unit,
    onViewReceiptClick: (Uri) -> Unit,
    onDeleteReceiptClick: (Uri) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onLabelChange: (String) -> Unit,
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
            LabelsInputField(
                modifier = Modifier.fillMaxWidth(),
                state = labelsInputFieldState,
                onAddLabel = onAddLabel,
                onRemoveLabelAtIndex = onRemoveLabelAtIndex,
                onValueChange = onLabelChange
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

@Composable
private fun LabelsInputField(
    modifier: Modifier = Modifier,
    state: LabelsInputField,
    onAddLabel: (String) -> Unit,
    onRemoveLabelAtIndex: (Int) -> Unit,
    onValueChange: (String) -> Unit
) {
    with(state) {
        RecordDetailsLabelsInput(
            modifier = modifier,
            value = value,
            labels = labels,
            suggestion = suggestion,
            error = error?.asRawString(),
            placeholder = stringResource(R.string.labels_label),
            onValueChange = onValueChange,
            onLabelRemoveAtIndex = onRemoveLabelAtIndex,
            onLabelAdd = onAddLabel,
        )
    }
}