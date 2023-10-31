package org.expenny.core.ui.foundation.transitions

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween

const val animDefaultDurationMs = 300

@ExperimentalAnimationApi
fun slideInHorizontalTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { 300 },
        animationSpec = tween(
            durationMillis = animDefaultDurationMs / 2,
            easing = LinearOutSlowInEasing
        )
    ) + fadeIn(animationSpec = tween(animDefaultDurationMs / 2))
}

@ExperimentalAnimationApi
fun slideOutHorizontalTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { 300 },
        animationSpec = tween(
            durationMillis = animDefaultDurationMs / 2,
            easing = LinearEasing
        )
    ) + fadeOut(animationSpec = tween(animDefaultDurationMs / 2))
}

@ExperimentalAnimationApi
fun slideOutVerticalTransition(): ExitTransition {
    return slideOutVertically(
        targetOffsetY = { 300 },
        animationSpec = tween(
            durationMillis = animDefaultDurationMs / 2,
            easing = LinearEasing
        )
    ) + fadeOut(animationSpec = tween(animDefaultDurationMs / 2))
}

@ExperimentalAnimationApi
fun slideInVerticalTransition(): EnterTransition {
    return slideInVertically(
        initialOffsetY = { 300 },
        animationSpec = tween(
            durationMillis = animDefaultDurationMs / 2,
            easing = LinearEasing
        )
    ) + fadeIn(animationSpec = tween(animDefaultDurationMs / 2))
}

// exitTransition
@ExperimentalAnimationApi
fun defaultExitNavigationTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(200)) +
            scaleOut(animationSpec = tween(200), targetScale = 0.95f)
}

// enterTransition
@ExperimentalAnimationApi
fun defaultEnterNavigationTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(200)) +
            scaleIn(animationSpec = tween(200), initialScale = 0.95f)
}

// popEnterTransition
@ExperimentalAnimationApi
fun defaultPopEnterNavigationTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(200)) +
            scaleIn(animationSpec = tween(200), initialScale = 0.95f)
}

// popExitTransition
@ExperimentalAnimationApi
fun defaultPopExitNavigationTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(200)) +
            scaleOut(animationSpec = tween(200), targetScale = 0.95f)
}

@ExperimentalAnimationApi
fun fadeInTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(animDefaultDurationMs))
}

@ExperimentalAnimationApi
fun fadeOutTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(animDefaultDurationMs))
}

@ExperimentalAnimationApi
fun scaleInTransition(): EnterTransition {
    return fadeIn(animationSpec = tween(animDefaultDurationMs)) + scaleIn(
        animationSpec = tween(animDefaultDurationMs),
        initialScale = 0.9f
    )
}

@ExperimentalAnimationApi
fun scaleOutTransition(): ExitTransition {
    return fadeOut(animationSpec = tween(animDefaultDurationMs)) + scaleOut(
        animationSpec = tween(animDefaultDurationMs),
        targetScale = 0.9f
    )
}