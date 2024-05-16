package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessAccountBalanceDto(
    @SerialName("balanceAmount") val balanceAmount: GoCardlessAccountBalanceAmountDto,
    @SerialName("balanceType") val balanceType: String,
    @SerialName("referenceDate") val referenceDate: String,
)
