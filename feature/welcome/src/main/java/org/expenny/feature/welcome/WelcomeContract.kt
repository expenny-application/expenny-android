package org.expenny.feature.welcome

sealed interface Event {
    data object NavigateToGetStarted : Event
}

sealed interface Action {
    data object OnCtaClick : Action
}