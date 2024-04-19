package org.expenny.core.ui.extensions

import org.expenny.core.ui.data.ui.MultiSelectionUi
import org.expenny.core.ui.data.ui.SelectionUi
import org.expenny.core.ui.data.ui.SelectionType
import org.expenny.core.ui.data.ui.SingleSelectionUi

val SelectionUi<*>.type
    get() = when(this) {
        is SingleSelectionUi<*> -> SelectionType.Single
        is MultiSelectionUi<*> -> SelectionType.Multi
    }

val SelectionUi<*>.size
    get() = when(this) {
        is SingleSelectionUi<*> -> if (value == null) 0 else 1
        is MultiSelectionUi<*> -> value.size
    }

fun <T> SelectionUi<T>.empty() = when(this) {
    is SingleSelectionUi<T> -> copy(null)
    is MultiSelectionUi<T> -> copy(emptyList())
}

fun SelectionUi<*>.isNotEmpty() = size > 0

fun SelectionUi<*>.isEmpty() = size == 0
