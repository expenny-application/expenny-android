package org.expenny.core.database.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "profile",
    indices = [
        Index(value = arrayOf("name", "currencyCode"), unique = true)
    ]
)
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val profileId: Long = 0,
    val name: String = "",
    val currencyCode: String
)