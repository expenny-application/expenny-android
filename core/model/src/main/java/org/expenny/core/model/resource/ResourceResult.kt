package org.expenny.core.model.resource

import java.util.concurrent.CancellationException

sealed class ResourceResult<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Success<T>(data: T) : ResourceResult<T>(data)
    class Error<T>(throwable: Throwable? = null, data: T? = null) : ResourceResult<T>(data, throwable)
    class Loading<T>(data: T? = null) : ResourceResult<T>(data)
}

inline fun <reified T> ResourceResult<T>.onError(
    block: (Throwable) -> Unit,
): ResourceResult<T> {
    val e = if (this is ResourceResult.Error<*>) throwable else null
    when {
        e is CancellationException -> throw e
        e != null -> block(e)
    }
    return this
}

inline fun <reified T> ResourceResult<T>.onLoading(
    block: (data: T?) -> Unit,
): ResourceResult<T> {
    if (this is ResourceResult.Loading<T>) block(data)
    return this
}

inline fun <reified T> ResourceResult<T>.onSuccess(
    block: (data: T) -> Unit,
): ResourceResult<T> {
    if (this is ResourceResult.Success<T>) block(data!!)
    return this
}