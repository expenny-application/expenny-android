package org.expenny.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import org.expenny.core.network.dto.GoCardlessInstitutionDto
import org.expenny.core.network.dto.GoCardlessInstitutionRequisitionDto
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

    suspend fun getInstitutions(): List<GoCardlessInstitutionDto> {
        return goCardlessClient.get("institutions/").body<List<GoCardlessInstitutionDto>>()
    }

    suspend fun createInstitutionRequisition(institutionId: String): GoCardlessInstitutionRequisitionDto {
        return goCardlessClient.post("requisitions/") {
            setBody(
                mapOf(
                    "redirect" to "app://expenny",
                    "institution_id" to institutionId
                )
            )
        }.body<GoCardlessInstitutionRequisitionDto>()
    }

    suspend fun getInstitutionRequisition(requisitionId: String): GoCardlessInstitutionRequisitionDto {
        return goCardlessClient.get("requisitions/$requisitionId/")
            .body<GoCardlessInstitutionRequisitionDto>()
    }

    suspend fun deleteInstitutionRequisition(requisitionId: String): Boolean {
        return goCardlessClient.delete("requisitions/$requisitionId/").status.isSuccess()
    }
}