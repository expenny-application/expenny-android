package org.expenny.feature.welcome

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.expenny.core.ui.base.ExpennyViewModel
import org.expenny.feature.welcome.contract.WelcomeAction
import org.expenny.feature.welcome.contract.WelcomeEvent
import javax.inject.Inject

@HiltViewModel
internal class WelcomeViewModel @Inject constructor() : ExpennyViewModel<WelcomeAction>() {

    private val _event = MutableSharedFlow<WelcomeEvent>()
    val event: SharedFlow<WelcomeEvent> = _event.asSharedFlow()

    override fun onAction(action: WelcomeAction) {
        when (action) {
            is WelcomeAction.OnCtaClick -> handleOnCtaClick()
        }
    }

    private fun handleOnCtaClick() {
        viewModelScope.launch {
            _event.emit(WelcomeEvent.NavigateToProfileSetup)
        }
    }
}