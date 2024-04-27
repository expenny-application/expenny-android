package org.expenny.feature.welcome.style

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

object WelcomeScreenTransitionStyle : DestinationStyle.Animated {

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return slideOutHorizontally(
            targetOffsetX = { -300 },
            animationSpec = tween(durationMillis = 150, easing = LinearEasing)
        ) + fadeOut(animationSpec = tween(150))
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return slideInHorizontally(
            initialOffsetX = { -300 },
            animationSpec = tween(durationMillis = 150, easing = LinearEasing)
        ) + fadeIn(animationSpec = tween(150))
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return fadeOut(animationSpec = tween(300))
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return fadeIn(animationSpec = tween(1000))
    }
}