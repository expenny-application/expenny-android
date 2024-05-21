package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessAccountDetailsDto(
    @SerialName("resourceId") val resourceId: String,
    @SerialName("iban") val iban: String,
    @SerialName("currency") val currency: String,
    @SerialName("name") val name: String,
    @SerialName("ownerName") val ownerName: String,
)
