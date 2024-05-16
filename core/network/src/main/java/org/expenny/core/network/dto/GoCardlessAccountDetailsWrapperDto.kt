package org.expenny.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoCardlessAccountDetailsWrapperDto(
    @SerialName("account") val account: GoCardlessAccountDetailsDto
)
