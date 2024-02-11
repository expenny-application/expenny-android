package org.expenny.feature.welcome

sealed interface Event {
    data object NavigateToProfileSetup : Event
}

sealed interface Action {
    data object OnCtaClick : Action
}