package org.expenny.main

import android.content.res.Resources
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalTonalElevationEnabled
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
import org.expenny.core.common.utils.Constants
import org.expenny.core.ui.foundation.ExpennyTheme
import org.expenny.navigation.ExpennyNavGraphs
import timber.log.Timber
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val content by lazy { findViewById<ComposeView>(R.id.compose_root) }
    private var isContentReadyToBeDrawn: Boolean = false
    private var startRoute: Route = ExpennyNavGraphs.setup
    private val isConfigurationChangedKey = "isConfigurationChangedKey"
    private val isInitPassedKey = "isInitPassedKey"

    companion object {
        const val isDataCleanupRequestedKey = "isDataCleanupRequestedKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        overrideDefaultLocale()

        setContentView(R.layout.activity_root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val isDataCleanupRequested = intent.extras?.getBoolean(isDataCleanupRequestedKey) ?: false
        val isConfigurationChanged = savedInstanceState?.getBoolean(isConfigurationChangedKey, false) ?: false
        val isInitPassed = savedInstanceState?.getBoolean(isInitPassedKey, false) ?: false
        val shouldSkipInit = isConfigurationChanged && isInitPassed

        if (shouldSkipInit) {
            isContentReadyToBeDrawn = true
        } else {
            init(
                isDataCleanupRequested = isDataCleanupRequested,
                onComplete = { isContentReadyToBeDrawn = true }
            )
        }
        addOnPreDrawListener()
        setRootContent()
    }

    private fun init(isDataCleanupRequested: Boolean, onComplete: () -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (isDataCleanupRequested) {
                Timber.i("MainActivity data cleanup requested")
                viewModel.onDataCleanupRequest()
            }
            viewModel.isProfileSetUp.filterNotNull().first().also { isProfileSetUp ->
                Timber.i("MainActivity data init")

                val isPasscodeSetUp = viewModel.isPasscodeSetUp.filterNotNull().first()
                // This is needed for redirecting user either to dashboard, passcode or welcome screen
                startRoute = when {
                    isProfileSetUp && isPasscodeSetUp -> ExpennyNavGraphs.auth
                    isProfileSetUp && !isPasscodeSetUp -> ExpennyNavGraphs.home
                    else -> ExpennyNavGraphs.setup
                }
                // forcing composable content to recompose with new startRoute value
                content.disposeComposition()
                // waiting for composable content to be composed before dispatching onPreDraw
                delay(Constants.DEFAULT_COMPOSITION_DELAY_MS)
                onComplete()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.verifyBiometricInvalidationStatus()
        viewModel.verifyAlarmsRevokeStatus()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(isConfigurationChangedKey, isChangingConfigurations)
        // required for edge case when configuration change is happening
        // while isContentReadyToBeDrawn is still false
        outState.putBoolean(isInitPassedKey, isContentReadyToBeDrawn)

        super.onSaveInstanceState(outState)
    }

    private fun addOnPreDrawListener() {
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isContentReadyToBeDrawn) {
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
                    LocalDensity provides Density(LocalDensity.current.density, 1f),
                    LocalTonalElevationEnabled provides false
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

    private fun overrideDefaultLocale() {
        val applicationLocales = AppCompatDelegate.getApplicationLocales()

        val preferredLocaleLanguage = if (applicationLocales.isEmpty) {
            Resources.getSystem().configuration.getLocales()[0].language
        } else {
            applicationLocales.toLanguageTags()
        }

        if (preferredLocaleLanguage != Locale.getDefault().language) {
            Timber.i("Overriding default locale: $preferredLocaleLanguage")
            Locale.setDefault(Locale(preferredLocaleLanguage))
        }

        Timber.i("Default locale: ${Locale.getDefault().toLanguageTag()}")
    }
}
