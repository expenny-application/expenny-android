package org.expenny.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.feature.splash.navigation.SplashNavigator
import org.expenny.feature.splash.view.SplashContent

@Destination
@Composable
fun SplashScreen(
    navigator: SplashNavigator,
) {
    val vm: SplashViewModel = hiltViewModel()
    val isProfileStored by vm.isProfileStored.collectAsState()

    println("isProfileStored $isProfileStored")

    LaunchedEffect(isProfileStored) {
        // Warning. This acts as a stub navigation screen responsible for redirecting user
        // either to dashboard ('app' nav graph) or welcome ('setup' nav graph) screen.
        // Unfortunately this cannot be done within Activity because of noticeable navigation delay.
        // Current approach makes process almost instant and visually user shouldn't notice this empty screen
        when (isProfileStored) {
            true -> navigator.navigateToApp()
            false -> navigator.navigateToSetup()
            null -> {}
        }
    }

    SplashContent()
}