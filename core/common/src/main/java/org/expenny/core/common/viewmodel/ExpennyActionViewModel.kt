package org.expenny.core.common.viewmodel

abstract class ExpennyActionViewModel<Action> : ExpennyViewModel() {
    abstract fun onAction(action: Action)
}