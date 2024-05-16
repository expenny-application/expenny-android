package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessRequisitionDto(
    @SerialName("id") val id: String,
    @SerialName("status") val status: String,
    @SerialName("redirect") val redirectUrl: String,
    @SerialName("link") val url: String,
    @SerialName("accounts") val accounts: List<String>,
)
