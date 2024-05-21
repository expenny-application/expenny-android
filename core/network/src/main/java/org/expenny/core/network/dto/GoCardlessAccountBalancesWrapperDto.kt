package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessAccountBalancesWrapperDto(
    @SerialName("balances") val balances: List<GoCardlessAccountBalanceDto>
)
