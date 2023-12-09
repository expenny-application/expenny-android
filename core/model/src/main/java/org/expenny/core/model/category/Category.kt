package org.expenny.core.model.category

import org.expenny.core.model.profile.Profile

data class Category(
    val id: Long,
    val profile: Profile,
    val name: String,
    val iconResName: String,
    val colorArgb: Int,
)
