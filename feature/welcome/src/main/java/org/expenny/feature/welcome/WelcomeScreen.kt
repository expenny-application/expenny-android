package org.expenny.feature.welcome

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.expenny.core.ui.extensions.observeWithLifecycle
import org.expenny.feature.welcome.navigation.WelcomeNavigator
import org.expenny.feature.welcome.style.WelcomeScreenTransitionStyle
import org.expenny.feature.welcome.view.WelcomeContent

@Destination(style = WelcomeScreenTransitionStyle::class)
@Composable
fun WelcomeScreen(
    navigator: WelcomeNavigator,
) {
    val vm: WelcomeViewModel = hiltViewModel()

    vm.event.observeWithLifecycle {
        when(it) {
            is Event.NavigateToGetStarted -> {
                navigator.navigateToGetStartedScreen()
            }
        }
    }

    WelcomeContent(
        onAction = vm::onAction
    )
}