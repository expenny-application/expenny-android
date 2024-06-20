package org.expenny.core.ui.reducers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

abstract class ContainerStateReducer<S : ContainerStateReducer.State>(
    initialState: S,
    scope: CoroutineScope
) : ContainerHost<S, Nothing> {

    override val container = scope.container<S, Nothing>(initialState)

    val stateFlow: StateFlow<S> get() = container.stateFlow

    val state: S get() = container.stateFlow.value

    fun setState(newState: S) {
        intent {
            reduce {
                newState
            }
        }
    }

    interface State
}