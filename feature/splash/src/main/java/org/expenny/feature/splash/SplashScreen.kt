package org.expenny.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.feature.splash.navigation.SplashNavigator
import org.expenny.feature.splash.view.SplashContent

@Deprecated("Moved to MainActivity")
@Destination
@Composable
fun SplashScreen(
    navigator: SplashNavigator,
) {
    val vm: SplashViewModel = hiltViewModel()
    val isProfileStored by vm.isProfileStored.collectAsState()

    println("isProfileStored $isProfileStored")

    LaunchedEffect(isProfileStored) {
        when (isProfileStored) {
            true -> navigator.navigateToApp()
            false -> navigator.navigateToSetup()
            null -> {}
        }
    }

    SplashContent()
}