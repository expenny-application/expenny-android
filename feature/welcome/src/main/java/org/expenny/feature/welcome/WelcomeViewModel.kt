package org.expenny.feature.welcome

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.expenny.core.ui.base.ExpennyViewModel
import javax.inject.Inject

@HiltViewModel
internal class WelcomeViewModel @Inject constructor() : ExpennyViewModel<Action>() {

    private val _event = MutableSharedFlow<Event>()
    val event: SharedFlow<Event> = _event.asSharedFlow()

    override fun onAction(action: Action) {
        when (action) {
            is Action.OnCtaClick -> handleOnCtaClick()
        }
    }

    private fun handleOnCtaClick() {
        viewModelScope.launch {
            _event.emit(Event.NavigateToProfileSetup)
        }
    }
}