package org.expenny.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess
import org.expenny.core.network.dto.GoCardlessAccountBalanceDto
import org.expenny.core.network.dto.GoCardlessAccountBalancesWrapperDto
import org.expenny.core.network.dto.GoCardlessAccountDetailsDto
import org.expenny.core.network.dto.GoCardlessAccountDetailsWrapperDto
import org.expenny.core.network.dto.GoCardlessAccountDto
import org.expenny.core.network.dto.GoCardlessAgreementDto
import org.expenny.core.network.dto.GoCardlessInstitutionDto
import org.expenny.core.network.dto.GoCardlessRequisitionDto
import javax.inject.Inject
import javax.inject.Named

class GoCardlessService @Inject constructor(
    @Named("gocardless") private val goCardlessClient: HttpClient
) {

    suspend fun getInstitution(institutionId: String): GoCardlessInstitutionDto {
        return goCardlessClient.get("institutions/$institutionId/").body<GoCardlessInstitutionDto>()
    }

    suspend fun getInstitutions(countryCode: String? = null): List<GoCardlessInstitutionDto> {
        return if (countryCode.isNullOrBlank()) {
            goCardlessClient.get("institutions/")
        } else {
            goCardlessClient.get("institutions") {
                url {
                    parameters.append("country", countryCode)
                }
            }
        }.body<List<GoCardlessInstitutionDto>>()
    }

    suspend fun createRequisition(institutionId: String): GoCardlessRequisitionDto {
        return goCardlessClient.post("requisitions/") {
            setBody(
                mapOf(
                    "redirect" to "app://expenny",
                    "institution_id" to institutionId
                )
            )
        }.body<GoCardlessRequisitionDto>()
    }

    suspend fun getRequisition(requisitionId: String): GoCardlessRequisitionDto {
        return goCardlessClient.get("requisitions/$requisitionId/")
            .body<GoCardlessRequisitionDto>()
    }

    suspend fun deleteRequisition(requisitionId: String): Boolean {
        return goCardlessClient.delete("requisitions/$requisitionId/").status.isSuccess()
    }

    suspend fun getAccount(accountId: String): GoCardlessAccountDto {
        return goCardlessClient.get("accounts/$accountId/").body<GoCardlessAccountDto>()
    }

    suspend fun getAccountDetails(accountId: String): GoCardlessAccountDetailsDto {
        val wrapper = goCardlessClient.get("accounts/$accountId/details/")
            .body<GoCardlessAccountDetailsWrapperDto>()
        return wrapper.account
    }

    suspend fun getAccountBalances(accountId: String): List<GoCardlessAccountBalanceDto> {
        val wrapper = goCardlessClient.get("accounts/$accountId/balances/")
            .body<GoCardlessAccountBalancesWrapperDto>()
        return wrapper.balances
    }

    suspend fun getAgreement(agreementId: String): GoCardlessAgreementDto {
        return goCardlessClient.get("agreements/enduser/$agreementId/").body<GoCardlessAgreementDto>()
    }
}