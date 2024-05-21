package org.expenny.feature.welcome.contract

sealed interface WelcomeEvent {
    data object NavigateToProfileSetup : WelcomeEvent
}

sealed interface WelcomeAction {
    data object OnCtaClick : WelcomeAction
}