package org.expenny.core.ui.data.ui


data class MultiSelectionUi<T>(val value: List<T> = emptyList()) : SelectionUi<T> {
    override fun contains(value: T): Boolean = this.value.contains(value)
}

data class SingleSelectionUi<T>(val value: T? = null) : SelectionUi<T> {
    override fun contains(value: T): Boolean = this.value == value
}

sealed interface SelectionUi<T> {
    fun contains(value: T): Boolean
}

enum class SelectionType {
    Single, Multi
}