package org.expenny.core.ui.foundation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.expenny.core.ui.foundation.model.button.ExpennyButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFlatButtonType
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonAttributes
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonSize
import org.expenny.core.ui.foundation.model.button.ExpennyFloatingButtonType

@Composable
fun ExpennyButton(
    modifier: Modifier = Modifier,
    attributes: ExpennyButtonAttributes,
    onClick: () -> Unit
) {
    with(attributes) {
        CompositionLocalProvider(
            LocalRippleTheme provides ButtonRippleTheme(type.rippleColor)
        ) {
            when (this) {
                is ExpennyFlatButtonAttributes -> {
                    FlatButton(
                        modifier = modifier,
                        isEnabled = isEnabled,
                        type = type,
                        size = size,
                        icon = icon,
                        label = label,
                        onClick = onClick
                    )
                }
                is ExpennyFloatingButtonAttributes -> {
                    FloatingButton(
                        modifier = modifier,
                        isEnabled = isEnabled,
                        isExpanded = isExpanded,
                        type = type,
                        size = size,
                        icon = icon,
                        label = label,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun FlatButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    type: ExpennyFlatButtonType,
    size: ExpennyFlatButtonSize,
    icon: Painter? = null,
    label: String? = null,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.sizeIn(
            minHeight = size.height,
            minWidth = size.width
        ),
        elevation = null,
        border = null,
        onClick = onClick,
        enabled = isEnabled,
        shape = size.shape,
        contentPadding = size.padding,
        colors = ButtonDefaults.buttonColors(
            containerColor = type.containerColor,
            contentColor = type.contentColor,
            disabledContainerColor = type.disabledContainerColor,
            disabledContentColor = type.disabledContentColor
        ),
        content = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(size.spacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        modifier = Modifier.size(size.iconSize),
                        painter = icon,
                        contentDescription = label.orEmpty()
                    )
                }
                label?.let {
                    Text(
                        text = label,
                        style = size.textStyle,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    )
}

@Composable
private fun FloatingButton(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    isExpanded: Boolean,
    type: ExpennyFloatingButtonType,
    size: ExpennyFloatingButtonSize,
    icon: Painter? = null,
    label: String? = null,
    onClick: () -> Unit
) {
    val minWidth by animateDpAsState(
        targetValue = if (isExpanded) size.width.times(size.expandSizeModifier) else size.width,
        label = "MinWidthAnimation"
    )
    val minHeight by animateDpAsState(
        targetValue = if (isExpanded) size.height.times(size.expandSizeModifier) else size.height,
        label = "MinHeightAnimation"
    )
    val iconSize by animateDpAsState(
        targetValue = if (isExpanded) size.iconSize.times(size.expandSizeModifier) else size.iconSize,
        label = "IconSizeAnimation"
    )

    val expandAnimation = fadeIn(
        tween(100, 0, easingLinearCubicBezier)
    ) + expandHorizontally(
        tween(200, 0, easingEmphasizedCubicBezier),
        Alignment.Start
    )

    val collapseAnimation = fadeOut(
        tween(100, 0, easingLinearCubicBezier)
    ) + shrinkHorizontally(
        tween(200, 0, easingEmphasizedCubicBezier),
        Alignment.Start
    )

    Surface(
        modifier = modifier.semantics { role = Role.Button },
        enabled = isEnabled,
        shape = size.shape,
        color = type.containerColor,
        contentColor = type.contentColor,
        shadowElevation = 3.dp,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .sizeIn(
                    minWidth = minWidth,
                    minHeight = minHeight
                )
                .padding(size.padding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isExpanded) Arrangement.Start else Arrangement.Center
        ) {
            icon?.let {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = icon,
                    contentDescription = label.orEmpty()
                )
            }
            label?.let {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandAnimation,
                    exit = collapseAnimation,
                ) {
                    Row {
                        Spacer(Modifier.width(size.spacing))
                        Text(
                            text = label,
                            style = size.textStyle,
                            overflow = TextOverflow.Clip,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

private val easingLinearCubicBezier = CubicBezierEasing(0.0f, 0.0f, 1.0f, 1.0f)
private val easingEmphasizedCubicBezier = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

private class ButtonRippleTheme(private val rippleColor: Color) : RippleTheme {

    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = rippleColor,
        lightTheme = isSystemInDarkTheme().not()
    )

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = rippleColor.copy(alpha = 0.75f),
        lightTheme = isSystemInDarkTheme().not()
    )
}
