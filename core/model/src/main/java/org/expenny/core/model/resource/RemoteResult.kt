package org.expenny.core.model.resource

import java.util.concurrent.CancellationException

sealed class RemoteResult<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Success<T>(data: T) : RemoteResult<T>(data)
    class Error<T>(throwable: Throwable? = null, data: T? = null) : RemoteResult<T>(data, throwable)
    class Loading<T>(data: T? = null) : RemoteResult<T>(data)
}

inline fun <reified T> RemoteResult<T>.onError(
    block: (Throwable) -> Unit,
): RemoteResult<T> {
    val e = if (this is RemoteResult.Error<*>) throwable else null
    when {
        e is CancellationException -> throw e
        e != null -> block(e)
    }
    return this
}

inline fun <reified T> RemoteResult<T>.onLoading(
    block: (data: T?) -> Unit,
): RemoteResult<T> {
    if (this is RemoteResult.Loading<T>) block(data)
    return this
}

inline fun <reified T> RemoteResult<T>.onSuccess(
    block: (data: T) -> Unit,
): RemoteResult<T> {
    if (this is RemoteResult.Success<T>) block(data!!)
    return this
}