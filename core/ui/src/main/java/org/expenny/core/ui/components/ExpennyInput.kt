package org.expenny.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import org.expenny.core.ui.data.DecimalInputUi
import org.expenny.core.ui.extensions.asRawString
import org.expenny.core.ui.extensions.noRippleClickable
import org.expenny.core.ui.transformations.ExpennyDecimalVisualTransformation
import org.expenny.core.ui.transformations.ExpennyDecimalVisualTransformation.Companion.formatToInput
import org.expenny.core.ui.transformations.ExpennyDecimalVisualTransformation.Companion.formatToOutput
import org.expenny.core.ui.foundation.surfaceInput
import java.math.BigDecimal

@Composable
fun ExpennyMonetaryInputField(
    modifier: Modifier = Modifier,
    state: DecimalInputUi,
    description: String? = null,
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
        description = description?.let { AnnotatedString(it) },
        isRequired = state.isRequired,
        isEnabled = state.isEnabled,
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
    placeholder: String? = null,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    trailingContent: @Composable (() -> Unit)? = null,
) {
    ExpennyInputField(
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        value = value,
        error = error,
        isReadonly = true,
        isEnabled = isEnabled,
        isRequired = isRequired,
        onValueChange = {},
        trailingContent = trailingContent,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        )
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
                onClick = onClick,
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
    description: AnnotatedString? = null,
    placeholder: String? = null,
    isRequired: Boolean = false,
    isReadonly: Boolean = false,
    isEnabled: Boolean = true,
    isSingleLine: Boolean = true,
    maxLines: Int = 1,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isError by rememberUpdatedState(error != null)
    val isFocusable by rememberUpdatedState(!isReadonly && isEnabled)
    var isFocusedDirty by rememberSaveable { mutableStateOf(false) }
    val disabledContentColor by rememberExpennyInputDisabledContentColor(isEnabled)
    val textSelectionColors by rememberExpennyInputTextSelectionColors(isError)
    val errorContainerColor by animateExpennyInputContainerAsState(interactionSource)
    val borderStroke by animateExpennyInputBorderAsState(isEnabled, isError, interactionSource)

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        Column(modifier = modifier) {
            TextField(
                modifier = Modifier
                    .height(56.dp)
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

                    ExpennyInputLabel(
                        text = text,
                        required = isRequired
                    )
                },
                placeholder = {
                    if (placeholder != null) {
                        ExpennyInputLabel(
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
                colors = expennyInputDefaultColors.copy(
                    errorContainerColor = errorContainerColor,
                    disabledPlaceholderColor = disabledContentColor,
                    disabledTextColor = disabledContentColor,
                )
            )
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                if (isError) {
                    Text(
                        text = error!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                } else if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpennyInputLabel(text: String, required: Boolean) {
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
fun animateExpennyInputContainerAsState(
    interactionSource: InteractionSource,
): State<Color> {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val backgroundColor =
        if (isFocused) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surfaceInput

    val animatedBackground = animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = 150),
        label = "AnimateBackground"
    )

    return rememberUpdatedState(animatedBackground.value)
}

@Composable
fun animateExpennyInputBorderAsState(
    isEnabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource
): State<BorderStroke> {
    val focusedBorderThickness = 2.dp
    val unfocusedBorderThickness = if (isError) 1.dp else 0.dp
    val isFocused by interactionSource.collectIsFocusedAsState()
    val strokeColor = rememberUpdatedState(
        when {
            !isEnabled -> Color.Transparent
            isError -> MaterialTheme.colorScheme.error
            isFocused -> MaterialTheme.colorScheme.primary
            else -> Color.Transparent
        }
    )
    val targetThickness = if (isFocused) focusedBorderThickness else unfocusedBorderThickness
    val animatedThickness = if (isEnabled) {
        animateDpAsState(
            targetValue = targetThickness,
            animationSpec = tween(durationMillis = 150),
            label = "AnimateThickness"
        )
    } else {
        rememberUpdatedState(unfocusedBorderThickness)
    }
    return rememberUpdatedState(
        BorderStroke(animatedThickness.value, SolidColor(strokeColor.value))
    )
}

@Composable
private fun rememberExpennyInputDisabledContentColor(isEnabled: Boolean) = rememberUpdatedState(
    if (isEnabled) MaterialTheme.colorScheme.onSurface
    else MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5f)
)

@Composable
private fun rememberExpennyInputTextSelectionColors(isError: Boolean) = rememberUpdatedState(
    TextSelectionColors(
        handleColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        backgroundColor = (if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary).copy(0.4f)
    )
)

private val expennyInputDefaultColors
    @Composable
    get() = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceInput,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceInput,
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
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )