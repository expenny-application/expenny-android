package org.expenny.core.ui.utils

import androidx.compose.animation.core.TweenSpec
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ramcosta.composedestinations.spec.NavGraphSpec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.expenny.core.resources.R

class ExpennyDrawerState(
    val drawer: DrawerState,
    private val tabs: List<NavGraphSpec>,
    private val navController: NavController,
    private val coroutineScope: CoroutineScope
) {

    private val isDrawerTab: Boolean
        get() {
            return navController.currentBackStackEntry?.destination?.route in tabs.map { it.startRoute.route }
        }

    val isDrawerTabAsState: Boolean
        @Composable get() {
            return tabs
                .map { it.startRoute.route }
                .contains(navController.currentBackStackEntryAsState().value?.destination?.route)
        }

    @Composable
    fun NavigationDrawerIcon(orElse: @Composable () -> Unit) {
        if (isDrawerTab) {
            NavigationDrawerIcon()
        } else {
            orElse()
        }
    }

    @Composable
    fun NavigationDrawerIcon() {
        IconButton(
            onClick = {
                animateTo(DrawerValue.Open)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_menu),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
    }

    fun animateTo(value: DrawerValue, onFinish: () -> Unit = {}) {
        coroutineScope.launch {
            drawer.animateTo(value, TweenSpec(200))
        }.invokeOnCompletion { onFinish() }
    }
}