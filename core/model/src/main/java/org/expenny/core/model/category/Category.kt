package org.expenny.core.model.category

import org.expenny.core.model.profile.Profile

data class Category(
    val id: Long,
    val profile: Profile,
    val name: String,
    val iconResName: String,
    val colorArgb: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode() = id.hashCode()
}
