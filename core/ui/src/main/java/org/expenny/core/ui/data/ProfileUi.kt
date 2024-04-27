package org.expenny.core.ui.data

data class ProfileUi(
    val id: Long,
    val name: String,
    val currency: String,
    val displayName: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProfileUi) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode() = id.hashCode()
}
