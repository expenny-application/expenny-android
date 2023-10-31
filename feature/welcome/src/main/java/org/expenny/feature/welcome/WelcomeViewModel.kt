package org.expenny.feature.welcome

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.expenny.core.common.viewmodel.ExpennyViewModel
import org.expenny.feature.welcome.state.WelcomeUiEvent
import javax.inject.Inject

@HiltViewModel
internal class WelcomeViewModel @Inject constructor() : ExpennyViewModel() {
    private val _screenEvent = MutableSharedFlow<ScreenEvent>()
    val screenEvent: SharedFlow<ScreenEvent> = _screenEvent.asSharedFlow()

    fun onEvent(event: WelcomeUiEvent) {
        when (event) {
            is WelcomeUiEvent.OnContinueClick -> handleOnContinueClick()
        }
    }

    private fun handleOnContinueClick() {
        viewModelScope.launch {
            _screenEvent.emit(ScreenEvent.NavigateToGetStarted)
        }
    }

    sealed interface ScreenEvent {
        object NavigateToGetStarted : ScreenEvent
    }
}