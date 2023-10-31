package org.expenny.core.ui.data.selection


data class MultiSelection<T>(val data: List<T>) : Selection<T> {
    override fun contains(value: T): Boolean = data.contains(value)
}

data class SingleSelection<T>(val data: T?) : Selection<T> {
    override fun contains(value: T): Boolean = data == value
}

sealed interface Selection<T> {
    fun contains(value: T): Boolean
}