package org.expenny.core.data.utils

import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import org.expenny.core.model.resource.RemoteResult

inline fun <T> remoteResultMediator(
    crossinline onSuccess: () -> Unit = {},
    crossinline onFailure: (Throwable) -> Unit = {},
    crossinline block: suspend () -> T,
) : Flow<RemoteResult<T>> = channelFlow {
    val loading = launch { send(RemoteResult.Loading()) }

    runCatching {
        block()
    }.onSuccess { res ->
        onSuccess()
        loading.cancelAndJoin()
        send(RemoteResult.Success(res))

        this@channelFlow.cancel()
    }.onFailure { e ->
        val error: RemoteResult<T> = RemoteResult.Error(throwable = e)
        println(e.stackTraceToString())
        onFailure(e)
        loading.cancelAndJoin()
        // Call send() because localQuery().collect { send() } is emitting
        // previously collected Resource.Error on every database change
        send(error)

        this@channelFlow.cancel()
    }
}
