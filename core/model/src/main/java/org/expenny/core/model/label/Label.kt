package org.expenny.core.model.label

import org.expenny.core.model.profile.Profile

data class Label(
    val id: Long,
    val profile: Profile,
    val name: String,
    val colorArgb: Int
)