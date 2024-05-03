package org.expenny.core.model.resource

sealed interface RemoteResult<T> {
    class Success<T>(val data: T) : RemoteResult<T>
    class Error<T>(val throwable: Throwable? = null) : RemoteResult<T>
    class Loading<T> : RemoteResult<T>
}