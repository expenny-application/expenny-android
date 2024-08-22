package org.expenny.core.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.expenny.core.common.models.ErrorMessage
import org.expenny.core.common.models.StringResource
import org.expenny.core.common.utils.StringResourceProvider
import timber.log.Timber
import javax.inject.Inject
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

abstract class ExpennyViewModel<Action> : ViewModel() {

    @Inject
    lateinit var provideString: StringResourceProvider

    protected fun defaultCoroutineExceptionHandler(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable.stackTraceToString())
            Firebase.crashlytics.log(throwable.stackTraceToString())

            onCoroutineException(ErrorMessage(throwable))
        }
    }

    protected fun parseError(throwable: Throwable?): StringResource {
        return ErrorMessage(throwable).text
    }

    protected open fun onCoroutineException(message: ErrorMessage) {}

    open fun onAction(action: Action) {}
}