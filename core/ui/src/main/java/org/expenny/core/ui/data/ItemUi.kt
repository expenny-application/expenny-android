package org.expenny.core.ui.data

data class ItemUi<T>(
    override val key: T,
    override val label: String
) : AbstractItemUi<T>()