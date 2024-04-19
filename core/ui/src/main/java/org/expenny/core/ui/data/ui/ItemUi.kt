package org.expenny.core.ui.data.ui

data class ItemUi(
    val id: Long,
    val label: String,
) {
    constructor(
        id: Int,
        label: String,
    ) : this(id.toLong(), label)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemUi) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode() = id.hashCode()
}
