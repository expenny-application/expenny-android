package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category",
    indices = [
        Index(value = arrayOf("name", "profileId"), unique = true)
    ]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryId: Long = 0,
    val profileId: Long,
    val name: String,
    val iconResName: String,
    val colorArgb: Int,
) {

    class Update(
        val categoryId: Long,
        val name: String,
        val iconResName: String,
        val colorArgb: Int,
    )
}