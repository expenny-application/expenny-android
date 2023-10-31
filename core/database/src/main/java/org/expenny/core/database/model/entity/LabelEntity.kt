package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "label",
    indices = [
        Index(value = arrayOf("name", "profileId"), unique = true)
    ]
)
data class LabelEntity(
    @PrimaryKey(autoGenerate = true)
    val labelId: Long = 0,
    val profileId: Long,
    val name: String,
    val colorArgb: Int,
) {

    class Update(
        val labelId: Long,
        val name: String,
        val colorArgb: Int,
    )
}
