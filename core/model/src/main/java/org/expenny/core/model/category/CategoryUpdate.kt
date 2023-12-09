package org.expenny.core.model.category

data class CategoryUpdate(
    val id: Long,
    val name: String,
    val iconResName: String,
    val colorArgb: Int
)
