package org.expenny.core.common.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.expenny.core.common.utils.ErrorMessage
import timber.log.Timber

abstract class ExpennyViewModel : ViewModel() {

    protected open fun onCoroutineException(message: ErrorMessage) {}

    protected fun defaultCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            val errorMessage = ErrorMessage(throwable)

            Timber.e(throwable.stackTraceToString())

            onCoroutineException(errorMessage)
        }
    }
}