package org.expenny.core.ui.foundation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.extensions.noRippleClickable
import org.expenny.core.resources.R
import org.expenny.core.ui.data.field.MonetaryInputField
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.foundation.transformations.ExpennyDecimalVisualTransformation
import org.expenny.core.ui.foundation.transformations.ExpennyDecimalVisualTransformation.Companion.formatToOutput
import org.expenny.core.ui.foundation.transformations.ExpennyDecimalVisualTransformation.Companion.formatToInput
import org.expenny.core.ui.theme.surfaceInput
import java.math.BigDecimal

@Composable
fun ExpennyMonetaryInputField(
    modifier: Modifier = Modifier,
    state: MonetaryInputField,
    label: String,
    currency: String,
    onValueChange: (BigDecimal) -> Unit,
    onCurrencyClick: () -> Unit = {},
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    ExpennyInputField(
        modifier = modifier,
        label = label,
        value = formatToInput(state.value),
        error = state.error?.asRawString(),
        isRequired = state.required,
        isEnabled = state.enabled,
        onValueChange = {
            onValueChange(formatToOutput(it, state.value.scale()))
        },
        interactionSource = interactionSource,
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = imeAction
        ),
        visualTransformation = ExpennyDecimalVisualTransformation(state.value.scale()),
        trailingContent = {
            ExpennyText(
                modifier = Modifier.noRippleClickable { onCurrencyClick() },
                style = MaterialTheme.typography.bodyMedium,
                text = currency
            )
        }
    )
}

@Composable
fun ExpennySelectInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String? = null,
    placeholder: String? = null,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
    onValueChange: (String) -> Unit = {},
) {
    ExpennyInputField(
        modifier = modifier.noRippleClickable {
            if (isEnabled) onClick()
        },
        label = label,
        placeholder = placeholder,
        value = value,
        error = error,
        isReadonly = true,
        isEnabled = isEnabled,
        isRequired = isRequired,
        onValueChange = onValueChange,
        trailingContent = {
            IconButton(
                onClick = { if (isEnabled) onClick() },
                enabled = isEnabled
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_right),
                    contentDescription = null,
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        )
    )
}

@Composable
fun ExpennyInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String? = null,
    placeholder: String? = null,
    maxLines: Int = 1,
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
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isError = error != null
    val isFocusable = !isReadonly && isEnabled
    var isFocusedDirty by rememberSaveable { mutableStateOf(false) }

    val disabledContentColor =
        if (isEnabled) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f)

    val textSelectionColors = TextSelectionColors(
        handleColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        backgroundColor = (if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary).copy(0.5f)
    )
    val errorContainerColor by animateBackgroundAsState(interactionSource)
    val borderStroke by animateBorderAsState(
        isEnabled = isEnabled,
        isError = isError,
        interactionSource = interactionSource
    )

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        Column(modifier = modifier) {
            TextField(
                modifier = Modifier
                    .height(ExpennyInputFieldHeight)
                    .fillMaxWidth()
                    .border(
                        border = borderStroke,
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
                textStyle = LocalTextStyle.current,
                isError = isError,
                enabled = isFocusable,
                value = value,
                maxLines = maxLines,
                singleLine = isSingleLine,
                onValueChange = {
                    onValueChange(it)
                },
                label = {
                    // if the field is not enabled by default, there is no way to show placeholder
                    // other then replace label with placeholder value while field is empty
                    val text = placeholder.takeIf { it != null && value.isBlank() && !isFocusable }
                        ?: label

                    InputFieldLabel(
                        text = text,
                        required = isRequired
                    )
                },
                placeholder = {
                    if (placeholder != null) {
                        InputFieldLabel(
                            text = placeholder,
                            required = isRequired
                        )
                    }
                },
                leadingIcon = leadingContent,
                trailingIcon = trailingContent,
                interactionSource = interactionSource,
                keyboardOptions = keyboardOptions.copy(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = keyboardActions,
                visualTransformation = visualTransformation,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceInput,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceInput,
                    errorContainerColor = errorContainerColor,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    errorPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = disabledContentColor,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTextColor = disabledContentColor,
                ),
            )
            Box(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
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
}

@Composable
private fun InputFieldLabel(text: String, required: Boolean) {
    val formattedText = if (required) {
        buildAnnotatedString {
            append(text)
            withStyle(SpanStyle(MaterialTheme.colorScheme.error)) {
                append(" *")
            }
        }
    } else {
        AnnotatedString(text)
    }

    Text(
        text = formattedText,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

@Composable
private fun animateBackgroundAsState(
    interactionSource: InteractionSource,
): State<Color> {
    val focused by interactionSource.collectIsFocusedAsState()
    val backgroundColor =
        if (focused) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surfaceInput

    val animatedBackground = animateColorAsState(backgroundColor, tween(durationMillis = 150))

    return rememberUpdatedState(animatedBackground.value)
}

@Composable
private fun animateBorderAsState(
    isEnabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource
): State<BorderStroke> {
    val focusedBorderThickness = 2.dp
    val unfocusedBorderThickness = if (isError) 1.dp else 0.dp
    val isFocused by interactionSource.collectIsFocusedAsState()
    val strokeColor = strokeColor(isEnabled, isError, interactionSource)
    val targetThickness = if (isFocused) focusedBorderThickness else unfocusedBorderThickness
    val animatedThickness = if (isEnabled) {
        animateDpAsState(targetThickness, tween(durationMillis = 150))
    } else {
        rememberUpdatedState(unfocusedBorderThickness)
    }
    return rememberUpdatedState(
        BorderStroke(animatedThickness.value, SolidColor(strokeColor.value))
    )
}

@Composable
private fun strokeColor(
    enabled: Boolean,
    error: Boolean,
    interactionSource: InteractionSource
): State<Color> {
    val focused by interactionSource.collectIsFocusedAsState()
    val targetValue = when {
        !enabled -> Color.Transparent
        error -> MaterialTheme.colorScheme.error
        focused -> MaterialTheme.colorScheme.primary
        else -> Color.Transparent
    }

    return rememberUpdatedState(targetValue)
}

val ExpennyInputFieldHeight = 56.dp
