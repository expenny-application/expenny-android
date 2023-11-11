package org.expenny.main

import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.expenny.R
import org.expenny.core.common.types.ApplicationTheme
import org.expenny.core.ui.theme.ExpennyTheme
import org.expenny.navigation.ExpennyNavGraphs

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val content by lazy { findViewById<ComposeView>(R.id.compose_root) }
    private var isReadyToProceed: Boolean = false
    private var startRoute: Route = ExpennyNavGraphs.setup

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        getInitialData()
        addOnPreDrawListener()
        setRootContent()
    }

    private fun getInitialData() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(300) // delay splash screen appearance
            viewModel.isProfileSetUp.filterNotNull().first().also { isProfileSetUp ->
                val isPasscodeSetUp = viewModel.isPasscodeSetUp.filterNotNull().first()
                // This is needed for redirecting user either to dashboard, passcode or welcome screen
                startRoute = when {
                    isProfileSetUp && isPasscodeSetUp -> ExpennyNavGraphs.security
                    isProfileSetUp && !isPasscodeSetUp -> ExpennyNavGraphs.home
                    else -> ExpennyNavGraphs.setup
                }
                // forcing composable content to recompose with new startRoute value
                content.disposeComposition()
                // waiting for composable content to be composed before dispatching onPreDraw
                delay(300)
                isReadyToProceed = true
            }
        }
    }

    private fun addOnPreDrawListener() {
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isReadyToProceed) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    private fun setRootContent() {
        content.setContent {
            val theme by viewModel.theme.collectAsState()
            val isDarkTheme = shouldUseDarkTheme(theme)
            val systemUiController = rememberSystemUiController()

            DisposableEffect(systemUiController, isDarkTheme) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !isDarkTheme
                )
                onDispose {}
            }

            ExpennyTheme(isDarkTheme = isDarkTheme) {
                CompositionLocalProvider(
                    // default font scale instead of system one
                    LocalDensity provides Density(LocalDensity.current.density, 1f)
                ) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        MainScreen(startRoute = startRoute)
                    }
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
