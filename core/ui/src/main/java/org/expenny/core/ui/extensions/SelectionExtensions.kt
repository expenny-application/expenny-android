package org.expenny.core.ui.extensions

import org.expenny.core.ui.data.selection.MultiSelection
import org.expenny.core.ui.data.selection.Selection
import org.expenny.core.ui.data.selection.SelectionType
import org.expenny.core.ui.data.selection.SingleSelection

val Selection<*>.type
    get() = when(this) {
        is SingleSelection<*> -> SelectionType.Single
        is MultiSelection<*> -> SelectionType.Multi
    }

val Selection<*>.size
    get() = when(this) {
        is SingleSelection<*> -> 1
        is MultiSelection<*> -> this.data.size
    }

val Selection<*>.isNotEmpty
    get() = size > 0

val Selection<*>.isEmpty
    get() = size == 0