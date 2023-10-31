package org.expenny.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.onEach
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.ui.theme.ExpennyTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Stub to let main activity do initial load from datastore while splash screen is active
        // instead of SplashScreenDestination, which might have lead to unpleasant visual delay for
        // couple of seconds before navigation either to setup or welcome screen
        var profileState by mutableStateOf<ProfileState>(ProfileState.Loading)

        splashScreen.setKeepOnScreenCondition {
            profileState == ProfileState.Loading
        }

        setContent {
            val vm: MainViewModel = hiltViewModel()
            val theme by vm.theme.collectAsState()
            val isDarkTheme = shouldUseDarkTheme(theme)
            val systemUiController = rememberSystemUiController()

            LaunchedEffect(Unit) {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    vm.profileState
                        .filterNot { it == ProfileState.Loading }
                        .onEach { profileState = it }
                        .collect()
                }
            }

            DisposableEffect(systemUiController, isDarkTheme) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkTheme
                )
                onDispose {}
            }

            ExpennyTheme(isDarkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    private fun shouldUseDarkTheme(
        applicationTheme: ApplicationTheme,
    ) = when (applicationTheme) {
        ApplicationTheme.SystemDefault -> isSystemInDarkTheme()
        ApplicationTheme.Light -> false
        ApplicationTheme.Dark -> true
    }
}
