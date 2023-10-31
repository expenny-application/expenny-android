package org.expenny.feature.dashboard.style

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object DashboardScreenTransitionStyle : DestinationStyle.Animated {

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition {
        return fadeOut(animationSpec = tween(200)) +
                scaleOut(animationSpec = tween(200), targetScale = 0.95f)
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return fadeIn(animationSpec = tween(200)) +
                scaleIn(animationSpec = tween(200), initialScale = 0.95f)
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition {
        return fadeOut(animationSpec = tween(200))
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return fadeIn(animationSpec = tween(200))
    }
}