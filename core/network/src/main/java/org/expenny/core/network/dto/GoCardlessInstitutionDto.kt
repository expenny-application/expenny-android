package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessInstitutionDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("bic") val bic: String,
    @SerialName("transaction_total_days") val transactionTotalDays: String,
    @SerialName("countries") val countries: List<String>,
    @SerialName("logo") val logoUrl: String,
)