package org.expenny.core.model.category

data class CategoryCreate(
    val profileId: Long,
    val name: String,
    val iconResName: String,
    val colorValue: Long,
)
