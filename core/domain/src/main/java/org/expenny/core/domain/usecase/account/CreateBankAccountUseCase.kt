package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import org.expenny.core.common.utils.Constants
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.model.institution.InstitutionAccount
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.domain.usecase.currency.CreateCurrencyUseCase
import org.expenny.core.domain.usecase.currency.GetCurrenciesUseCase
import org.expenny.core.domain.usecase.currency.GetCurrencyUseCase
import org.expenny.core.domain.usecase.rate.GetLatestCurrencyRateUseCase
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyUnit
import java.math.BigDecimal
import javax.inject.Inject

class CreateBankAccountUseCase @Inject constructor(
    private val getLatestCurrencyRate: GetLatestCurrencyRateUseCase,
    private val getCurrencies: GetCurrenciesUseCase,
    private val getCurrency: GetCurrencyUseCase,
    private val createCurrency: CreateCurrencyUseCase,
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val accountRepository: AccountRepository,
) {

//    suspend operator fun invoke(params: Params): Flow<RemoteResult<Boolean>> {
//        remoteResultMediator {
//            val profile = getCurrentProfile().first()!!
//            val mainCurrencyUnit = profile.currencyUnit
//
//            val accountCurrencyUnit = params.institutionAccount.currency
//            val accountCurrency = getAccountCurrency(mainCurrencyUnit, accountCurrencyUnit)
//
////            createAccount(
////                CreateAccountUseCase.Params(
////                    currencyId = accountCurrency.id,
////                    name = params.institutionAccount.name,
////                    type = LocalAccountType.General,
////                    description = params.institutionAccount.iban,
////                    startBalance = params.institutionAccount.balance
////                )
////            )
//        }
//    }

    private suspend fun getAccountCurrency(
        mainCurrencyUnit: CurrencyUnit,
        accountCurrencyUnit: CurrencyUnit
    ): Currency {
        return getCurrencies().first()
            .firstOrNull { it.unit == accountCurrencyUnit }
            .let { currency ->
                if (currency == null) {
                    val currencyRate = getCurrencyRate(mainCurrencyUnit.code, accountCurrencyUnit.code)
                    val currencyId = createCurrency(
                        CreateCurrencyUseCase.Params(
                            currencyUnitId = accountCurrencyUnit.id,
                            baseToQuoteRate = currencyRate,
                            isSubscribedToRateUpdates = false
                        )
                    )
                    getCurrency(GetCurrencyUseCase.Params(currencyId)).first()!!
                } else {
                    currency
                }
            }
    }

    private suspend fun getCurrencyRate(base: String, quote: String): BigDecimal {
        return try {
            getLatestCurrencyRate(GetLatestCurrencyRateUseCase.Params(base = base, quote = quote))
                .filterIsInstance<RemoteResult.Success<CurrencyRate>>()
                .first()
                .data
                .rate
        } catch (e: Exception) {
            BigDecimal.ONE
        }.setScale(Constants.CURRENCY_RATE_SCALE)
    }

    data class Params(
        val institutionAccount: InstitutionAccount
    )
}