package org.expenny.core.ui.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import org.expenny.core.common.models.ErrorMessage
import org.expenny.core.common.utils.StringResourceProvider
import timber.log.Timber
import javax.inject.Inject

abstract class ExpennyViewModel<Action> : ViewModel() {

    @Inject
    lateinit var provideString: StringResourceProvider

    protected fun defaultCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            val errorMessage = ErrorMessage(throwable)

            Timber.e(throwable.stackTraceToString())
            onCoroutineException(errorMessage)
        }
    }

    protected open fun onCoroutineException(message: ErrorMessage) {}

    open fun onAction(action: Action) {}
}