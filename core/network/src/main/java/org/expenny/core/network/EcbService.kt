package org.expenny.core.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.isEmpty
import io.ktor.utils.io.core.readBytes
import org.expenny.core.network.dto.EcbRateReferenceDto
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

class EcbService @Inject constructor(
    @Named("ecb") private val ecbClient: HttpClient
) {

    suspend fun getLatestEurBasedData(vararg quoteUnits: String): List<EcbRateReferenceDto> {
        return request(
            units = quoteUnits.toList(),
            builder = {
                parameter("format", ECB_RESPONSE_DATA_FORMAT)
                parameter("lastNObservations", "1")
            },
            onResponse = { it.asString() }
        ).parseCsvOrEmpty()
    }

    suspend fun getEurBaseData(fromDate: LocalDate, vararg quoteUnits: String): List<EcbRateReferenceDto> {
        return request(
            units = quoteUnits.toList(),
            builder = {
                parameter("format", ECB_RESPONSE_DATA_FORMAT)
                parameter("startPeriod", fromDate.toString())
            },
            onResponse = { it.asString() }
        ).parseCsvOrEmpty()
    }

    suspend fun getEurBaseData(vararg quoteUnits: String): List<EcbRateReferenceDto> {
        return request(
            units = quoteUnits.toList(),
            builder = {
                parameter("format", ECB_RESPONSE_DATA_FORMAT)
            },
            onResponse = { it.asString() }
        ).parseCsvOrEmpty()
    }

    private suspend inline fun <T> request(
        units: List<String>,
        builder: HttpRequestBuilder.() -> Unit,
        noinline onResponse: suspend (HttpResponse) -> T
    ): T {
        return ecbClient.prepareGet(getUrlPath(*units.toTypedArray()), builder).execute(onResponse)
    }

    private suspend fun HttpResponse.asString(): String {
        val stringBuilder = StringBuilder()
        val channel: ByteReadChannel = this.body()

        while (!channel.isClosedForRead) {
            val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
            while (!packet.isEmpty) {
                val bytes = packet.readBytes()
                stringBuilder.append(bytes.toString(Charsets.UTF_8))
            }
        }
        return stringBuilder.toString()
    }

    private fun String.parseCsvOrEmpty(): List<EcbRateReferenceDto> {
        val rows = split("\n").map { row ->
            row.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
        }.drop(1) // drop header row

        return rows.mapNotNull { row ->
            try {
                EcbRateReferenceDto(
                    quoteCurrency = row[2],
                    baseCurrency = row[3],
                    value = row[7].toBigDecimal(),
                    date = LocalDate.parse(row[6])
                )
            } catch (e: Throwable) {
                null
            }
        }
    }

    private fun getUrlPath(vararg units: String): String {
        return "D.${units.joinToString("+").uppercase()}.${ECB_BASE_UNIT}.SP00.A"
    }

    companion object {
        private const val ECB_RESPONSE_DATA_FORMAT = "csvdata"
        const val ECB_BASE_UNIT = "EUR"
    }
}