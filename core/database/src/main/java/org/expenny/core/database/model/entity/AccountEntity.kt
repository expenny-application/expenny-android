package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "account",
    indices = [
        Index(value = arrayOf("name", "currencyId", "profileId"), unique = true)
    ]
)
class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val accountId: Long = 0,
    val profileId: Long,
    val currencyId: Long,
    val name: String,
    val type: String,
    val description: String,
    val startBalance: BigDecimal,
    val totalBalance: BigDecimal,
) {

    class Update(
        val accountId: Long,
        val currencyId: Long,
        val name: String,
        val type: String,
        val description: String,
        val startBalance: BigDecimal,
        val totalBalance: BigDecimal,
    )
}
