package org.expenny.feature.recorddetails.view

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyLabel
import org.expenny.core.ui.components.ExpennySection
import org.expenny.core.ui.data.field.InputField
import org.expenny.core.ui.data.ui.LabelUi
import org.expenny.core.ui.foundation.ExpennyInputField
import org.expenny.core.ui.foundation.ExpennyText
import org.expenny.core.ui.theme.surfaceInput
import org.expenny.core.ui.extensions.noRippleClickable

@Composable
internal fun RecordDetailsAdditionsSection(
    modifier: Modifier = Modifier,
    payeeOrPayerState: InputField,
    descriptionState: InputField,
    showSection: Boolean,
    labels: List<LabelUi>,
    receipts: List<Uri>,
    onDeleteLabelClick: (Long) -> Unit,
    onSelectLabelClick: () -> Unit,
    onAddReceiptClick: () -> Unit,
    onViewReceiptClick: (Uri) -> Unit,
    onDeleteReceiptClick: (Uri) -> Unit,
    onPayeeOrPayerChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onVisibilityChange: (Boolean) -> Unit
) {
    ExpennySection(
        modifier = modifier,
        title = stringResource(R.string.additions_label),
        isExpanded = showSection,
        onClick = { onVisibilityChange(!showSection) },
    ) {
        RecordDetailsAdditionsInputForm(
            labels = labels,
            receipts = receipts,
            payeeOrPayerState = payeeOrPayerState,
            descriptionState = descriptionState,
            onSelectLabelsClick = onSelectLabelClick,
            onDeleteLabelClick = onDeleteLabelClick,
            onAddReceiptClick = onAddReceiptClick,
            onViewReceiptClick = onViewReceiptClick,
            onDeleteReceiptClick = onDeleteReceiptClick,
            onPayeeOrPayerChange = onPayeeOrPayerChange,
            onDescriptionChange = onDescriptionChange,
        )
    }
}

@Composable
private fun RecordDetailsAdditionsInputForm(
    modifier: Modifier = Modifier,
    labels: List<LabelUi>,
    receipts: List<Uri>,
    payeeOrPayerState: InputField,
    descriptionState: InputField,
    onSelectLabelsClick: () -> Unit,
    onDeleteLabelClick: (Long) -> Unit,
    onAddReceiptClick: () -> Unit,
    onViewReceiptClick: (Uri) -> Unit,
    onDeleteReceiptClick: (Uri) -> Unit,
    onPayeeOrPayerChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
) {
    Column(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PayeeOrPayerInputField(
            modifier = Modifier.fillMaxWidth(),
            state = payeeOrPayerState,
            onValueChange = onPayeeOrPayerChange
        )
        LabelsInputField(
            labels = labels,
            onSelectClick = onSelectLabelsClick,
            onDeleteClick = onDeleteLabelClick
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
private fun PayeeOrPayerInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onValueChange: (String) -> Unit
) {
    with(state) {
        ExpennyInputField(
            modifier = modifier,
            isRequired = required,
            value = value,
            error = error?.asRawString(),
            label = stringResource(R.string.payee_payer_label),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
    }
}

@Composable
private fun DescriptionInputField(
    modifier: Modifier = Modifier,
    state: InputField,
    onAddReceiptClick: () -> Unit,
    onValueChange: (String) -> Unit
) {
    with(state) {
        ExpennyInputField(
            modifier = modifier,
            isRequired = required,
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
    labels: List<LabelUi>,
    onSelectClick: () -> Unit,
    onDeleteClick: (Long) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceInput)
            .noRippleClickable { onSelectClick() }
            .padding(
                start = 16.dp,
                end = 12.dp,
                top = 12.dp,
                bottom = 12.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (labels.isEmpty()) {
                ExpennyText(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.select_labels_label),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyRow(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ) {
                    items(
                        items = labels,
                        key = LabelUi::id
                    ) { label ->
                        ExpennyLabel(
                            modifier = Modifier.fillMaxHeight(),
                            contentColor = label.color,
                            label = {
                                ExpennyText(text = label.name)
                            },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier.clickable { onDeleteClick(label.id) },
                                    painter = painterResource(R.drawable.ic_close),
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
            Icon(
                modifier = Modifier.noRippleClickable { onSelectClick() },
                painter = painterResource(R.drawable.ic_chevron_right),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null,
            )
        }
    }
}