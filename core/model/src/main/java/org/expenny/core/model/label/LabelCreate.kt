package org.expenny.core.model.label

data class LabelCreate(
    val profileId: Long,
    val name: String,
    val colorArgb: Int
)
