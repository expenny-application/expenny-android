package org.expenny.core.ui.foundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExpennyButton(
    modifier: Modifier = Modifier,
    size: ExpennyButtonSize = ExpennyButtonSize.Large,
    style: ExpennyButtonStyle = ExpennyButtonStyle.Filled,
    type: ExpennyButtonType = ExpennyButtonType.Primary,
    label: @Composable () -> Unit,
    leadingIcon:  @Composable (() -> Unit)? = null,
    isEnabled: Boolean = true,
    onClick: () -> Unit,
) {
    val buttonColors = when(type) {
        ExpennyButtonType.Primary -> {
            ButtonColors(
                rippleColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(disabledContainerOpacity),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(disabledContentOpacity),
            )
        }
        ExpennyButtonType.Secondary -> {
            ButtonColors(
                rippleColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(disabledContainerOpacity),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(disabledContentOpacity),
            )
        }
    }.let {
        when (style) {
            ExpennyButtonStyle.Text -> it.copy(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            )
            else -> it
        }
    }

    ButtonContent(
        modifier = modifier,
        size = size,
        label = label,
        leadingIcon = leadingIcon,
        isEnabled = isEnabled,
        rippleColor = buttonColors.rippleColor,
        containerColor = buttonColors.containerColor,
        contentColor = buttonColors.contentColor,
        disabledContainerColor = buttonColors.disabledContainerColor,
        disabledContentColor = buttonColors.disabledContentColor,
        onClick = onClick
    )
}

@Composable
private fun ButtonContent(
    modifier: Modifier = Modifier,
    size: ExpennyButtonSize,
    label: @Composable () -> Unit,
    leadingIcon:  @Composable (() -> Unit)? = null,
    isEnabled: Boolean = true,
    rippleColor: Color,
    containerColor: Color,
    contentColor: Color,
    disabledContainerColor: Color = containerColor,
    disabledContentColor: Color = contentColor,
    onClick: () -> Unit
) {
    val (height, iconSize, textStyle) = when(size) {
        ExpennyButtonSize.Small -> Triple(32.dp, 18.dp, MaterialTheme.typography.titleSmall)
        ExpennyButtonSize.Medium -> Triple(44.dp, 24.dp, MaterialTheme.typography.titleMedium)
        ExpennyButtonSize.Large -> Triple(56.dp, 24.dp, MaterialTheme.typography.titleMedium)
    }

    CompositionLocalProvider(
        LocalRippleTheme provides ExpennyButtonRippleTheme(rippleColor = rippleColor)
    ) {
        Button(
            modifier = modifier.height(height),
            elevation = null,
            border = null,
            onClick = onClick,
            enabled = isEnabled,
            shape = defaultShape,
            contentPadding = PaddingValues(
                horizontal = horizontalContentPadding,
                vertical = verticalContentPadding
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = disabledContainerColor,
                disabledContentColor = disabledContentColor
            ),
            content = {
                ProvideTextStyle(value = textStyle) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (leadingIcon != null) {
                            Box(modifier = Modifier.size(iconSize)) {
                                leadingIcon()
                            }
                        }
                        label()
                    }
                }
            }
        )
    }
}

enum class ExpennyButtonSize { Small, Medium, Large }

enum class ExpennyButtonStyle { Filled, Text }

enum class ExpennyButtonType { Primary, Secondary }

private data class ButtonColors(
    val rippleColor: Color,
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
)

private val defaultShape = RoundedCornerShape(8.dp)
private val horizontalContentPadding = 8.dp
private val verticalContentPadding = 6.dp
private val disabledContainerOpacity = 0.12f
private val disabledContentOpacity = 0.38f

class ExpennyButtonRippleTheme(private val rippleColor: Color) : RippleTheme {

    @Composable
    override fun defaultColor(): Color =
        RippleTheme.defaultRippleColor(
            contentColor = rippleColor,
            lightTheme = isSystemInDarkTheme().not()
        )

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleTheme.defaultRippleAlpha(
            contentColor = rippleColor.copy(alpha = 0.75f),
            lightTheme = isSystemInDarkTheme().not()
        )
}
