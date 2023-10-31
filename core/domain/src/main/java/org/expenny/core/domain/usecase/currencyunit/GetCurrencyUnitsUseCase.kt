package org.expenny.core.domain.usecase.currencyunit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.CurrencyUnitRepository
import org.expenny.core.model.currency.CurrencyUnit
import javax.inject.Inject

class GetCurrencyUnitsUseCase @Inject constructor(
    private val currencyUnitRepository: CurrencyUnitRepository
) {

    operator fun invoke(searchQuery: String = ""): Flow<List<CurrencyUnit>> {
        return currencyUnitRepository.getCurrencyUnits().map { units ->
            val matchByCode: (CurrencyUnit) -> Boolean = { it.code.startsWith(searchQuery, true) }
            val matchByName: (CurrencyUnit) -> Boolean = { it.name.startsWith(searchQuery, true) }

            units.filter { currencyUnit ->
                (matchByCode(currencyUnit) || matchByName(currencyUnit))
            }
        }
    }
}