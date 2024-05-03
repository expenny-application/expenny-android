package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessInstitutionRequisitionDto(
    @SerialName("id") val id: String,
    @SerialName("status") val status: String,
    @SerialName("redirect") val redirectUrl: String,
    @SerialName("link") val url: String,
)
