package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessAccountDto(
    @SerialName("id") val id: String,
    @SerialName("iban") val iban: String,
    @SerialName("institution_id") val institutionId: String,
    @SerialName("status") val status: String,
    @SerialName("owner_name") val ownerName: String,
    @SerialName("last_accessed") val lastAccessedAt: String,
    @SerialName("created") val createdAt: String,
)
