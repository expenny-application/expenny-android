package org.expenny.feature.records.details.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyLabel
import org.expenny.core.ui.extensions.noRippleClickable
import org.expenny.core.ui.components.animateInputFieldBorderAsState
import org.expenny.core.ui.components.animateInputFieldContainerAsState

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun RecordDetailsLabelsInput(
    modifier: Modifier = Modifier,
    value: String,
    labels: List<String>,
    placeholder: String,
    suggestion: String? = null,
    error: String? = null,
    onValueChange: (String) -> Unit,
    onLabelRemoveAtIndex: (Int) -> Unit,
    onLabelAdd: (String) -> Unit,
    focusRequester: FocusRequester = remember { FocusRequester() },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isError = error != null
    val isFocused by interactionSource.collectIsFocusedAsState()
    var isInputVisible by remember(isFocused, labels, value) {
        mutableStateOf(isFocused || value.isNotBlank() || labels.isEmpty())
    }

    val containerColor by animateInputFieldContainerAsState(interactionSource)
    val borderStroke by animateInputFieldBorderAsState(
        isEnabled = true,
        isError = isError,
        interactionSource = interactionSource
    )

    LaunchedEffect(isInputVisible) {
        if (isInputVisible && labels.isNotEmpty()) {
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier = Modifier.noRippleClickable {
            if (!isFocused && !isInputVisible) {
                isInputVisible = true
            }
        }
    ) {
        FlowRow(
            modifier = modifier
                .heightIn(min = 56.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(containerColor)
                .border(borderStroke, MaterialTheme.shapes.small)
                .padding(
                    start = 16.dp,
                    end = 12.dp,
                    top = 12.dp,
                    bottom = 12.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        ) {
            repeat(times = labels.size) { index ->
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    ExpennyLabel(
                        label = {
                            Text(text = labels[index])
                        },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(12.dp)
                                    .noRippleClickable { onLabelRemoveAtIndex(index) },
                                painter = painterResource(R.drawable.ic_close),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                contentDescription = null,
                            )
                        }
                    )
                }
            }
            if (isInputVisible) {
                Box(
                    modifier = Modifier
                        .heightIn(min = 28.dp)
                        .widthIn(min = 4.dp)
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onKeyEvent {
                                if (it.key == Key.Backspace) {
                                    if (value.isEmpty() && labels.isNotEmpty()) {
                                        onLabelRemoveAtIndex(labels.size - 1)
                                    }
                                }
                                return@onKeyEvent false
                            },
                        value = value,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        decorationBox = { innerTextField ->
                            Box {
                                val placeholderText = when {
                                    isFocused && value.isNotBlank() && suggestion != null -> suggestion
                                    !isFocused && value.isBlank() && labels.isEmpty() -> placeholder
                                    else -> null
                                }
                                placeholderText?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        onValueChange = onValueChange,
                        interactionSource = interactionSource,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            autoCorrect = false
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (value.isNotBlank() && !isError) {
                                    onLabelAdd((suggestion ?: value).trim())
                                    onValueChange("")
                                }
                            }
                        )
                    )
                }
            }
        }
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            if (isError) {
                Text(
                    text = error!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}