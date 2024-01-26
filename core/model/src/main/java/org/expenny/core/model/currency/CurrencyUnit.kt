package org.expenny.core.model.currency

data class CurrencyUnit(
    val id: Long,
    val name: String,
    val code: String,
    val scale: Int
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrencyUnit

        if (id != other.id) return false
        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + code.hashCode()
        return result
    }
}
