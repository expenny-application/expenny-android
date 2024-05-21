package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessTokenDto(
    @SerialName("access") val access: String,
    @SerialName("access_expires") val accessExpires: Int,
    @SerialName("refresh") val refresh: String,
    @SerialName("refresh_expires") val refreshExpires: Int
)
