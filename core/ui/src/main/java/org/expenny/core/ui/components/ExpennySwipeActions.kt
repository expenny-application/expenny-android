package org.expenny.core.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class SwipeActionConfig(
    val threshold: Float,
    val iconResId: Int,
    val iconTint: Color,
    val background: Color,
    val stayDismissed: Boolean,
    val onDismiss: () -> Unit,
)

//val DefaultSwipeActionsConfig = SwipeActionsConfig(
//    threshold = 0.4f,
//    iconResId = R.drawable.ic_delete,
//    iconTint = Color.Transparent,
//    background = Color.Transparent,
//    stayDismissed = false,
//    onDismiss = {},
//)

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpennySwipeActions(
    modifier: Modifier = Modifier,
    config: SwipeActionConfig,
    content: @Composable (DismissState) -> Unit,
) = BoxWithConstraints(modifier) {
    val width = constraints.maxWidth.toFloat()
    val height = constraints.maxHeight.toFloat()

    var willDismissDirection: DismissDirection? by remember {
        mutableStateOf(null)
    }

    val state = rememberDismissState(
        confirmValueChange = {
            if (willDismissDirection == DismissDirection.EndToStart && it == DismissValue.DismissedToStart) {
                config.onDismiss()
                config.stayDismissed
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { state.requireOffset() }.collect {
            willDismissDirection = when {
                it < -width * config.threshold -> DismissDirection.EndToStart
                else -> null
            }
        }
    }

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(key1 = willDismissDirection) {
        if (willDismissDirection != null) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    val dismissDirections by remember(config) {
        derivedStateOf {
            mutableSetOf<DismissDirection>().apply {
                add(DismissDirection.EndToStart)
            }
        }
    }

    SwipeToDismiss(
        state = state,
        directions = dismissDirections,
        background = {
            AnimatedContent(
                targetState = Pair(state.dismissDirection, willDismissDirection != null),
                transitionSpec = {
                    fadeIn(
                        tween(0),
                        initialAlpha = if (targetState.second) 1f else 0f,
                    ) with fadeOut(
                        tween(0),
                        targetAlpha = if (targetState.second) .7f else 0f,
                    )
                }
            ) { (direction, willDismiss) ->
                val revealSize = remember { Animatable(if (willDismiss) 0f else 1f) }
                val iconSize = remember { Animatable(if (willDismiss) .8f else 1f) }

                LaunchedEffect(key1 = Unit) {
                    if (willDismiss) {
                        revealSize.snapTo(0f)
                        launch {
                            revealSize.animateTo(1f, animationSpec = tween(400))
                        }
                        iconSize.snapTo(.8f)
                        iconSize.animateTo(
                            1.45f,
                            spring(
                                dampingRatio = Spring.DampingRatioHighBouncy,
                            )
                        )
                        iconSize.animateTo(
                            1f,
                            spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                        .background(
                            color = when (direction) {
                                DismissDirection.EndToStart -> {
                                    if (willDismiss) config.background else config.iconTint
                                }
                                else -> Color.Transparent
                            },
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .scale(iconSize.value)
                            .offset {
                                IntOffset(x = 0, y = (10 * (1f - iconSize.value)).roundToInt())
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        when (direction) {
                            DismissDirection.EndToStart -> {
                                Image(
                                    painter = painterResource(config.iconResId),
                                    colorFilter = ColorFilter.tint(
                                        if (willDismiss) config.iconTint else config.background
                                    ),
                                    contentDescription = null
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }
        },
        dismissContent = {
            content(state)
        }
    )
}


class CirclePath(private val progress: Float, private val start: Boolean) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val origin = Offset(
            x = if (start) 0f else size.width,
            y = size.center.y,
        )

        val radius = (sqrt(
            size.height * size.height + size.width * size.width
        ) * 1f) * progress

        return Outline.Generic(
            Path().apply {
                addOval(
                    Rect(
                        center = origin,
                        radius = radius,
                    )
                )
            }
        )
    }
}