package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessAccountBalanceAmountDto(
    @SerialName("amount") val amount: String,
    @SerialName("currency") val currency: String,
)
