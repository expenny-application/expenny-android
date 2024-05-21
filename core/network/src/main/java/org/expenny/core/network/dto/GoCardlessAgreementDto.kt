package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessAgreementDto(
    @SerialName("id") val id: String,
    @SerialName("institution_id") val institutionId: String,
    @SerialName("access_valid_for_days") val accessValidForDays: String,
    @SerialName("accepted") val acceptedAt: String,
)
