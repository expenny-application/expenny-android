package org.expenny.main

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.components.ExpennySnackbar
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import org.expenny.navigation.*
import org.expenny.navigation.ExpennyNavigation
import org.expenny.core.ui.base.ExpennyDrawerManager

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun MainScreen(startRoute: Route) {
    val state = rememberExpennyState()

    val navController = state.navHostController
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
            if (state.drawerManager.isDrawerTabAsState) {
                val currentSelectedTab by navController.currentTabAsState()

                MainNavigationDrawer(
                    currentTab = currentSelectedTab,
                    onTabSelect = {
                        state.drawerManager.animateTo(DrawerValue.Closed) {
                            navController.navigateTab(it)
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = Color.Transparent,
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

@Composable
private fun PressAgainToExitBackHandler(currentDestination: String?) {
    var isIdle by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(key1 = isIdle) {
        if (!isIdle) {
            delay(2000)
            isIdle = true
        }
    }

    BackHandler(isIdle) {
        isIdle = false
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
