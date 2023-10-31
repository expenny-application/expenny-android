package org.expenny.core.domain.usecase.currencyunit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.expenny.core.domain.repository.CurrencyRepository
import org.expenny.core.model.currency.CurrencyUnit
import javax.inject.Inject

class GetAvailableCurrencyUnitsUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val getCurrencyUnits: GetCurrencyUnitsUseCase
) {

    operator fun invoke(searchQuery: String = "") : Flow<List<CurrencyUnit>> {
        return combine(
            currencyRepository.getCurrencies(),
            getCurrencyUnits(searchQuery)
        ) { currencies, units ->
            val existingCurrencyUnits = currencies.map { it.unit }

            if (existingCurrencyUnits.isNotEmpty()) {
                units.filter { !existingCurrencyUnits.contains(it) }
            } else {
                units
            }
        }
    }
}