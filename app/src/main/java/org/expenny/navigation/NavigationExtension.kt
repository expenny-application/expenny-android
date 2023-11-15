package org.expenny.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.utils.isRouteOnBackStack
import com.ramcosta.composedestinations.utils.startDestination
import org.expenny.main.drawer.DrawerTab
import timber.log.Timber

@Composable
internal fun NavController.currentTabAsState(): State<NavGraphSpec> {
    val selectedItem = remember { mutableStateOf(ExpennyNavGraphs.dashboard) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->

            currentBackStack.value.printStack()

            destination.tabNavGraph().also {
                selectedItem.value = it
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

internal fun NavController.navigateFirstTab() {
    navigateTab(DrawerTab.values().first().navGraph)
}

internal fun NavController.navigateTab(tabNavGraph: NavGraphSpec) {
    if (currentDestination?.route == tabNavGraph.startDestination.route) {
        // ignore if current destination is start tab destination
        return
    }

    if (isRouteOnBackStack(tabNavGraph)) {
        // pop up back stack to start tab destination if target tab equals to current tab
        if (isRouteOnBackStack(tabNavGraph.startRoute)) {
            popBackStack(tabNavGraph.route, false)
        } else {
            // situation when the tab doesn't have its start destination on back stack
            // for ex. when the destination was triggered from another tab
            navigate(tabNavGraph.startRoute.route) {
                popUpTo(tabNavGraph.route)
            }
        }
    }

    navigate(tabNavGraph.route) {
        // Pop up to start NavGraph tab route
        // to avoid building up a large stack of destinations on the back stack
        // popUpTo(AppNavGraphs.tabs.startRoute.startDestination.route) {
        popUpTo(ExpennyNavGraphs.home.route) {
            inclusive = false
            saveState = true
        }
        // Avoid multiple copies of the same destination when re-selecting the same item
        launchSingleTop = true
        // Restore state when re-selecting a previously selected item
        restoreState = true
    }
}

private fun NavDestination.tabNavGraph(): NavGraphSpec {
    hierarchy.forEach { destination ->
        ExpennyNavGraphs.home.nestedNavGraphs.forEach { navGraph ->
            if (destination.route == navGraph.route) {
                return navGraph
            }
        }
    }

    throw RuntimeException("Unknown nav graph for tab destination: $route")
}

internal fun NavDestination.navGraph(): NavGraphSpec {
    hierarchy.forEach { destination ->
        if (destination.route in DrawerTab.routes) {
            ExpennyNavGraphs.home.nestedNavGraphs.forEach { navGraph ->
                if (destination.route == navGraph.route) {
                    return navGraph
                }
            }
        }

        ExpennyNavGraphs.navGraphs.forEach { navGraph ->
            if (destination.parentNavGraph.route == navGraph.route) {
                return navGraph
            }
        }
    }

    throw RuntimeException("Unknown NavGraph for destination: $route")
}

internal val NavDestination.parentNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

private fun List<NavBackStackEntry>.printStack() {
    val stack = toMutableList()
        .map { it.destination.route }
        .toTypedArray().contentToString()
    Timber.i("navigation stack = $stack")
}