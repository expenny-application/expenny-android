package org.expenny.core.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.base.ExpennyLoremIpsum
import org.expenny.core.ui.base.ExpennyPreview
import org.expenny.core.ui.extensions.noRippleClickable
import org.expenny.core.ui.foundation.ExpennyThemePreview
import org.expenny.core.ui.foundation.transparent
import org.expenny.core.ui.transformations.ExpennyDecimalVisualTransformation
import org.expenny.core.ui.transformations.ExpennyDecimalVisualTransformation.Companion.formatToInput
import org.expenny.core.ui.transformations.ExpennyDecimalVisualTransformation.Companion.formatToOutput
import java.math.BigDecimal

@Composable
fun ExpennyInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String? = null,
    description: String? = null,
    placeholder: String? = null,
    isRequired: Boolean = false,
    isReadonly: Boolean = false,
    isEnabled: Boolean = true,
    isSingleLine: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isError by rememberUpdatedState(error != null)
    val isFocusable by rememberUpdatedState(!isReadonly && isEnabled)
    var isFocusedDirty by rememberSaveable { mutableStateOf(false) }
    val textSelectionColors by rememberTextSelectionColors(isError)
    val disabledPlaceholderColor by rememberDisabledPlaceholderColor(isEnabled)
    val disabledLabelColor by rememberDisabledLabelColor(isEnabled)
    val disabledTextColor by rememberDisabledTextColor(isEnabled)
    val disabledContainerColor by rememberDisabledContainerColorAsState(!isEnabled)
    val border by animateBorderAsState(isEnabled, isError, isFocused)
    val maxLines by rememberUpdatedState(if (isSingleLine) 1 else 5)

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        Column(modifier = modifier) {
            TextField(
                modifier = Modifier
                    .then(
                        if (isSingleLine) Modifier.height(56.dp)
                        else Modifier.heightIn(min = 56.dp)
                    )
                    .fillMaxWidth()
                    .border(
                        border = border,
                        shape = MaterialTheme.shapes.small,
                    )
                    .onFocusChanged {
                        // Expose onValueChange to trigger validation on field focus lost
                        if (it.isFocused) {
                            isFocusedDirty = true
                        } else {
                            if (isFocusedDirty) {
                                onValueChange(value)
                            }
                            isFocusedDirty = false
                        }
                    },
                shape = MaterialTheme.shapes.small,
                textStyle = MaterialTheme.typography.bodyLarge,
                isError = isError,
                enabled = isFocusable,
                value = value,
                maxLines = maxLines,
                singleLine = isSingleLine,
                onValueChange = {
                    onValueChange(it)
                },
                label = {
                    InputLabel(
                        text = label,
                        isRequired = isRequired,
                        isEnabled = isEnabled
                    )
                },
                placeholder = placeholder?.let {
                    {
                        InputLabel(
                            text = placeholder,
                            isRequired = isRequired,
                            isEnabled = isEnabled
                        )
                    }
                },
                leadingIcon = leadingContent,
                trailingIcon = trailingContent,
                interactionSource = interactionSource,
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions.copy(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    errorPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorContainerColor = MaterialTheme.colorScheme.transparent,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    errorIndicatorColor = MaterialTheme.colorScheme.transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.transparent,
                    focusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.transparent,
                    disabledIndicatorColor = MaterialTheme.colorScheme.transparent,
                    disabledContainerColor = disabledContainerColor,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = disabledLabelColor,
                    disabledPlaceholderColor = disabledPlaceholderColor,
                    disabledTextColor = disabledTextColor,
                )
            )
            InputDescription(description = description)
            InputError(error = error)
        }
    }
}


@Composable
fun ExpennyMonetaryInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: BigDecimal,
    currency: String,
    error: String? = null,
    description: String? = null,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    onValueChange: (BigDecimal) -> Unit,
    onCurrencyClick: () -> Unit = {},
    leadingContent: @Composable (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    ExpennyInputField(
        modifier = modifier,
        label = label,
        value = formatToInput(value),
        error = error,
        description = description,
        isRequired = isRequired,
        isEnabled = isEnabled,
        onValueChange = {
            onValueChange(formatToOutput(it, value.scale()))
        },
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        visualTransformation = ExpennyDecimalVisualTransformation(value.scale()),
        leadingContent = leadingContent,
        trailingContent = {
            Text(
                modifier = Modifier.noRippleClickable { onCurrencyClick() },
                text = currency,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}

@Composable
fun ExpennyReadonlyInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String? = null,
    description: String? = null,
    placeholder: String? = null,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    isSingleLine: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    ExpennyInputField(
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        description = description,
        value = value,
        error = error,
        isReadonly = true,
        isSingleLine = isSingleLine,
        isEnabled = isEnabled,
        isRequired = isRequired,
        onValueChange = {},
        trailingContent = trailingContent,
        leadingContent = leadingContent,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Composable
fun ExpennySelectInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String? = null,
    description: String? = null,
    placeholder: String? = null,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    isSingleLine: Boolean = true,
    onClick: () -> Unit,
    onValueChange: (String) -> Unit = {},
    leadingContent: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    ExpennyInputField(
        modifier = modifier.noRippleClickable {
            if (isEnabled) onClick()
        },
        label = if (value.isBlank() && placeholder != null) placeholder else label,
        placeholder = placeholder,
        description = description,
        value = value,
        error = error,
        isReadonly = true,
        isEnabled = isEnabled,
        isRequired = isRequired,
        isSingleLine = isSingleLine,
        onValueChange = onValueChange,
        leadingContent = leadingContent,
        trailingContent = {
            IconButton(
                onClick = onClick,
                enabled = isEnabled
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
private fun InputError(
    modifier: Modifier = Modifier,
    error: String?,
) {
    error?.let {
        Text(
            modifier = modifier.padding(
                top = 4.dp,
                start = 16.dp,
                end = 16.dp
            ),
            text = error,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            maxLines = 3
        )
    }
}

@Composable
private fun InputDescription(
    modifier: Modifier = Modifier,
    description: String?,
) {
    description?.let {
        Text(
            modifier = modifier.padding(
                top = 4.dp,
                start = 16.dp,
                end = 16.dp
            ),
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun InputLabel(
    modifier: Modifier = Modifier,
    text: String,
    isRequired: Boolean,
    isEnabled: Boolean
) {
    val formattedText = if (isRequired) {
        buildAnnotatedString {
            append(text)
            withStyle(
                SpanStyle(
                    MaterialTheme.colorScheme.error.copy(if (isEnabled) 1f else 0.38f)
                )
            ) {
                append(" *")
            }
        }
    } else {
        AnnotatedString(text)
    }
    Text(
        modifier = modifier,
        text = formattedText,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Composable
private fun rememberDisabledContainerColorAsState(
    isDisabled: Boolean,
): State<Color> {
    return rememberUpdatedState(
        if (isDisabled) MaterialTheme.colorScheme.surfaceContainer
        else MaterialTheme.colorScheme.transparent
    )
}

@Composable
private fun animateBorderAsState(
    isEnabled: Boolean,
    isError: Boolean,
    isFocused: Boolean
): State<BorderStroke> {
    val focusedBorderWidth = 2.dp
    val unfocusedBorderWidth = 1.dp
    val borderColor = rememberUpdatedState(
        when {
            isError -> MaterialTheme.colorScheme.error
            isFocused -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outlineVariant
        }
    )
    val targetWidth = if (isFocused) focusedBorderWidth else unfocusedBorderWidth
    val animatedWidth = if (isEnabled) {
        animateDpAsState(
            targetValue = targetWidth,
            animationSpec = tween(durationMillis = 150),
            label = "AnimateBorderWidth"
        )
    } else {
        rememberUpdatedState(unfocusedBorderWidth)
    }
    return rememberUpdatedState(
        BorderStroke(animatedWidth.value, SolidColor(borderColor.value))
    )
}

@Composable
private fun rememberDisabledPlaceholderColor(isEnabled: Boolean) = rememberUpdatedState(
    if (isEnabled) MaterialTheme.colorScheme.onSurfaceVariant
    else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.38f)
)

@Composable
private fun rememberDisabledLabelColor(isEnabled: Boolean) = rememberUpdatedState(
    if (isEnabled) MaterialTheme.colorScheme.onSurfaceVariant
    else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.38f)
)

@Composable
private fun rememberDisabledTextColor(isEnabled: Boolean) = rememberUpdatedState(
    if (isEnabled) MaterialTheme.colorScheme.onSurface
    else MaterialTheme.colorScheme.onSurface.copy(0.38f)
)

@Composable
private fun rememberTextSelectionColors(isError: Boolean) = rememberUpdatedState(
    TextSelectionColors(
        handleColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        backgroundColor = (if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary).copy(0.38f)
    )
)

@ExpennyPreview
@Composable
private fun ExpennyInputPreview() {
    ExpennyThemePreview {
        ExpennyInputField(
            isRequired = true,
            label = "Preview label",
            value = "Preview value",
            placeholder = "Preview placeholder",
            onValueChange = {}
        )
        ExpennyInputField(
            label = "Preview label",
            value = "Preview value",
            error = "Preview error",
            onValueChange = {}
        )
        ExpennyInputField(
            label = "Preview label",
            value = "Preview value",
            description = "Preview description",
            onValueChange = {}
        )
        ExpennyMonetaryInputField(
            label = "Preview label",
            value = BigDecimal.ZERO.setScale(2),
            currency = "PLN",
            onValueChange = {}
        )
        ExpennySelectInputField(
            label = "Preview label",
            value = "Preview value",
            onClick = {},
            onValueChange = {}
        )
        ExpennySelectInputField(
            label = "Preview label",
            value = "Preview value",
            isEnabled = false,
            onClick = {},
            onValueChange = {}
        )
        ExpennyInputField(
            isEnabled = false,
            label = "Preview label",
            value = "Preview value",
            onValueChange = {}
        )
        ExpennyInputField(
            isSingleLine = false,
            label = "Preview label",
            value = ExpennyLoremIpsum(30).text,
            onValueChange = {}
        )
    }
}