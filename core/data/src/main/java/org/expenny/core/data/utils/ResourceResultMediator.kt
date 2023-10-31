package org.expenny.core.data.utils

import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import org.expenny.core.model.resource.ResourceResult

inline fun <T> flowCatching(
    crossinline onSuccess: () -> Unit = {},
    crossinline onFailure: (Throwable) -> Unit = {},
    crossinline block: () -> Flow<T>,
) : Flow<ResourceResult<T>> = channelFlow {
    runCatching {
        block()
    }.onSuccess { res ->
        onSuccess()
        res.collect { send(ResourceResult.Success(it)) }
    }.onFailure { e ->
        val error: ResourceResult<T> = ResourceResult.Error(throwable = e)
        onFailure(e)
        // Call send() because localQuery().collect { send() } is emitting
        // previously collected Resource.Error on every database change
        send(error)
    }
}

inline fun <T> suspendCatching(
    crossinline onSuccess: () -> Unit = {},
    crossinline onFailure: (Throwable) -> Unit = {},
    crossinline block: suspend () -> T,
) : Flow<ResourceResult<T>> = channelFlow {
    val loading = launch { send(ResourceResult.Loading()) }

    runCatching {
        block()
    }.onSuccess { res ->
        onSuccess()
        loading.cancelAndJoin()
        send(ResourceResult.Success(res))

        this@channelFlow.cancel()
    }.onFailure { e ->
        val error: ResourceResult<T> = ResourceResult.Error(throwable = e)
        println(e.stackTraceToString())
        onFailure(e)
        loading.cancelAndJoin()
        // Call send() because localQuery().collect { send() } is emitting
        // previously collected Resource.Error on every database change
        send(error)

        this@channelFlow.cancel()
    }
}
