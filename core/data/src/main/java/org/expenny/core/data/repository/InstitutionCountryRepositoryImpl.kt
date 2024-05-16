package org.expenny.core.data.repository
import kotlinx.coroutines.flow.Flow
import org.expenny.core.common.utils.remoteResultMediator
import org.expenny.core.domain.repository.InstitutionCountryRepository
import org.expenny.core.model.institution.InstitutionCountry
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.network.GoCardlessService
import org.expenny.core.network.dto.GoCardlessInstitutionDto
import java.util.Locale
import javax.inject.Inject

class InstitutionCountryRepositoryImpl @Inject constructor(
    private val goCardlessService: GoCardlessService
) : InstitutionCountryRepository {

    override fun getInstitutionCountries(): Flow<RemoteResult<List<InstitutionCountry>>> {
        return remoteResultMediator {
            val institutions = goCardlessService.getInstitutions()

            institutions
                .flatMap { it.countries }
                .distinct()
                .sorted()
                .mapNotNull { code ->
                    if (isValidCountryCode(code)) {
                        val country = Locale("", code).displayCountry
                        val flag = getCountryFlag(code)
                        val institutionsCount = institutions.getCountByCountry(code)
                        InstitutionCountry(
                            country = country,
                            code = code,
                            flag = flag,
                            institutionsCount = institutionsCount
                        )
                    } else {
                        null
                    }
                }
        }
    }

    private fun List<GoCardlessInstitutionDto>.getCountByCountry(code: String) =
        count { it.countries.contains(code) }

    private fun getCountryFlag(code: String) : String {
        val flagOffset = 0x1F1E6
        val asciiOffset = 0x41
        val firstChar = Character.codePointAt(code, 0) - asciiOffset + flagOffset
        val secondChar = Character.codePointAt(code, 1) - asciiOffset + flagOffset
        return (String(Character.toChars(firstChar)) + String(Character.toChars(secondChar)))
    }

    private fun isValidCountryCode(code: String) =
        Locale.getISOCountries().toSet().contains(code)
}