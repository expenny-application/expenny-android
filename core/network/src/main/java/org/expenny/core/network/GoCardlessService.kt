package org.expenny.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.expenny.core.network.dto.GoCardlessInstitutionDto
import javax.inject.Inject
import javax.inject.Named

class GoCardlessService @Inject constructor(
    @Named("gocardless") private val goCardlessClient: HttpClient
) {

    suspend fun getInstitutions(countryCode: String): List<GoCardlessInstitutionDto> {
        return goCardlessClient.get("institutions") {
            url {
                parameters.append("country", countryCode)
            }
        }.body<List<GoCardlessInstitutionDto>>()
    }
}