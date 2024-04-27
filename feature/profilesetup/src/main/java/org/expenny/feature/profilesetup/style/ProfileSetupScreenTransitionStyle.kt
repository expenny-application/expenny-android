package org.expenny.feature.profilesetup.style

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle
import org.expenny.core.ui.transitions.animDefaultDurationMs
import org.expenny.core.ui.transitions.slideInHorizontalTransition
import org.expenny.core.ui.transitions.slideOutHorizontalTransition

@OptIn(ExperimentalAnimationApi::class)
object ProfileSetupScreenTransitionStyle : DestinationStyle.Animated {

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition = fadeOut(animationSpec = tween(
        animDefaultDurationMs
    ))

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition = fadeIn(animationSpec = tween(
        animDefaultDurationMs
    ))

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition() = slideOutHorizontalTransition()

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() = slideInHorizontalTransition()
}