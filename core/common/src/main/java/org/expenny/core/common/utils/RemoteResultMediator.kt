package org.expenny.core.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

inline fun <T> remoteResultMediator(
    crossinline onSuccess: () -> Unit = {},
    crossinline onFailure: (Throwable) -> Unit = {},
    crossinline block: suspend CoroutineScope.() -> T,
) : Flow<RemoteResult<T>> = channelFlow {
    withContext(Dispatchers.IO) {
        // val loading = launch { send(RemoteResult.Loading) }

        runCatching {
            send(RemoteResult.Loading)
            block(this@runCatching)
        }.onSuccess { res ->
            onSuccess()
            // loading.cancelAndJoin()
            send(RemoteResult.Success(res))

            // this@channelFlow.cancel()
            this@channelFlow.close()
        }.onFailure { e ->
            val error: RemoteResult<T> = RemoteResult.Error(throwable = e)
            Timber.w(e)
            println(e.stackTraceToString())
            onFailure(e)
            // loading.cancelAndJoin()
            // Call send() because localQuery().collect { send() } is emitting
            // previously collected Resource.Error on every database change
            send(error)

            // this@channelFlow.cancel()
            this@channelFlow.close()
        }
    }
}

sealed interface RemoteResult<out T> {
    class Success<out T>(val data: T) : RemoteResult<T>
    class Error(val throwable: Throwable? = null) : RemoteResult<Nothing>
    data object Loading : RemoteResult<Nothing>
}
