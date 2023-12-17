package org.expenny.core.common.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <reified T, B> Flow<List<T>>.mapFlatten(crossinline block: T.() -> B): Flow<List<B>> {
    return this.map { it.map(block) }
}
