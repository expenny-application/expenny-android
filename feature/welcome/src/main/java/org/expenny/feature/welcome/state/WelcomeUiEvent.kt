package org.expenny.feature.welcome.state

sealed interface WelcomeUiEvent {
    object OnContinueClick : WelcomeUiEvent
}