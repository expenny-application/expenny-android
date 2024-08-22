package org.expenny.core.ui.data


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

operator fun <T> MultiSelectionUi<T>.minus(element: T): MultiSelectionUi<T> {
    val result = ArrayList<T>(value.size)
    var removed = false
    val newValue = value.filterTo(result) {
        if (!removed && it == element) { removed = true; false } else true
    }
    return MultiSelectionUi(newValue)
}

operator fun <T> MultiSelectionUi<T>.plus(element: T): MultiSelectionUi<T> {
    val result = ArrayList<T>(value.size + 1)
    result.addAll(value)
    result.add(element)
    return MultiSelectionUi(result)
}