package org.expenny.core.ui.transitions

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object VerticalSlideTransitionStyle : DestinationStyle.Animated {

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition? = null

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? = null

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition() = slideOutVerticalTransition()

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition() = slideInVerticalTransition()
}