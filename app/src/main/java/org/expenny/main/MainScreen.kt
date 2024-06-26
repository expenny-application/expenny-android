package org.expenny.main

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import kotlinx.coroutines.CoroutineScope
import org.expenny.core.ui.base.ExpennyDrawerManager
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.components.ExpennySnackbar
import org.expenny.navigation.ExpennyNavigation
import org.expenny.navigation.currentTabAsState
import org.expenny.navigation.navigateFirstTab
import org.expenny.navigation.navigateTab

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun MainScreen(startRoute: Route) {
    val state = rememberExpennyState()

    val navController = state.navHostController
    val currentSelectedTab by navController.currentTabAsState()
    val currentDestination = navController.currentDestination?.route
    val isCurrentNavGraphDestinationStart = currentDestination in DrawerTab.startRoutes.drop(1)
    val enableDrawerGestures = state.drawerManager.isDrawerTabAsState && currentDestination != DrawerTab.Settings.route

    BackHandler(state.drawerManager.drawer.isOpen) {
        state.drawerManager.animateTo(DrawerValue.Closed)
    }

    BackHandler(
        state.drawerManager.isDrawerTabAsState
                && isCurrentNavGraphDestinationStart
                && state.drawerManager.drawer.isClosed
    ) {
        navController.navigateFirstTab()
    }

    ModalNavigationDrawer(
        gesturesEnabled = enableDrawerGestures,
        drawerState = state.drawerManager.drawer,
        drawerContent = {
            MainNavigationDrawer(
                currentTab = currentSelectedTab,
                onTabSelect = {
                    state.drawerManager.animateTo(DrawerValue.Closed) {
                        navController.navigateTab(it)
                    }
                }
            )
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            snackbarHost = {
                ExpennySnackbar(hostState = state.snackbarHostState)
            },
        ) { _ ->
            ExpennyNavigation(
                mainState = state,
                startRoute = startRoute
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun rememberExpennyState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarManager: ExpennySnackbarManager = ExpennySnackbarManager(LocalContext.current.resources),
    navHostController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: ExpennyDrawerManager = rememberExpennyDrawerManager(navHostController, coroutineScope)
) = remember(snackbarHostState, navHostController, coroutineScope, snackbarManager) {
    MainState(
        drawerManager = drawerState,
        snackbarManager = snackbarManager,
        snackbarHostState = snackbarHostState,
        navHostController = navHostController,
        coroutineScope = coroutineScope,
    )
}

@Composable
private fun rememberExpennyDrawerManager(
    navHostController: NavHostController,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed),
    tabs: List<NavGraphSpec> = DrawerTab.values().map { it.navGraph },
) = remember(navHostController, coroutineScope, drawerState, tabs) {
    ExpennyDrawerManager(
        drawer = drawerState,
        tabs = tabs,
        navController = navHostController,
        coroutineScope = coroutineScope
    )
}
