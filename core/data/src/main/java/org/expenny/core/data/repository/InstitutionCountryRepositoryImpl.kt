package org.expenny.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.common.utils.remoteResultMediator
import org.expenny.core.domain.repository.InstitutionCountryRepository
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.model.common.Country
import org.expenny.core.network.GoCardlessService
import java.util.Locale
import javax.inject.Inject

class InstitutionCountryRepositoryImpl @Inject constructor(
    private val goCardlessService: GoCardlessService
) : InstitutionCountryRepository {

    override fun getInstitutionCountries(): Flow<RemoteResult<List<Country>>> {
        return remoteResultMediator {
            val institutions = goCardlessService.getInstitutions()

            institutions
                .flatMap { it.countries }
                .distinct()
                .sorted()
                .mapNotNull { code ->
                    if (isValidCountryCode(code)) {
                        val name = Locale("", code).displayCountry
                        Country(name = name, code = code)
                    } else {
                        null
                    }
                }
        }
    }

    private fun isValidCountryCode(code: String) =
        Locale.getISOCountries().toSet().contains(code)
}