package org.expenny.core.common.utils

import kotlinx.coroutines.flow.FlowCollector

object EmptyCollector : FlowCollector<Any?> {
    override suspend fun emit(value: Any?) {
        // does nothing
    }
}