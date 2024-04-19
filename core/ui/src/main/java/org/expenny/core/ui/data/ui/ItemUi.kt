package org.expenny.core.ui.data.ui

data class ItemUi<T>(
    override val key: T,
    override val label: String
) : AbstractItemUi<T>()