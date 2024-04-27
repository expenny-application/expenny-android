package org.expenny.core.ui.transitions

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object HorizontalSlideTransitionStyle : DestinationStyle.Animated {

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return slideOutHorizontally(
            targetOffsetX = { -300 },
            animationSpec = tween(
                durationMillis = animDefaultDurationMs / 2,
                easing = LinearEasing
            )
        ) + fadeOut(animationSpec = tween(animDefaultDurationMs / 2))
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { -300 },
            animationSpec = tween(
                durationMillis = animDefaultDurationMs / 2,
                easing = LinearOutSlowInEasing
            )
        ) + fadeIn(animationSpec = tween(animDefaultDurationMs / 2))
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition() = slideOutHorizontalTransition()

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() = slideInHorizontalTransition()
}