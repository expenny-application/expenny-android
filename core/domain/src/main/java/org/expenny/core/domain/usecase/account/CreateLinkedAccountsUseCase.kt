package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.expenny.core.common.utils.Constants
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.model.currency.CurrencyRate
import org.expenny.core.common.utils.RemoteResult
import org.expenny.core.domain.usecase.currency.CreateCurrencyUseCase
import org.expenny.core.domain.usecase.currency.GetCurrenciesUseCase
import org.expenny.core.domain.usecase.currency.GetCurrencyUseCase
import org.expenny.core.domain.usecase.institution.GetInstitutionUseCase
import org.expenny.core.domain.usecase.rate.GetLatestCurrencyRateUseCase
import org.expenny.core.domain.usecase.requisition.GetRequisitionAccountsUseCase
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyUnit
import org.expenny.core.model.institution.Institution
import org.expenny.core.model.institution.InstitutionAccount
import java.math.BigDecimal
import javax.inject.Inject

class CreateLinkedAccountsUseCase @Inject constructor(
    private val getRequisitionAccounts: GetRequisitionAccountsUseCase,
    private val getInstitution: GetInstitutionUseCase,
    private val getLatestCurrencyRate: GetLatestCurrencyRateUseCase,
    private val getCurrencies: GetCurrenciesUseCase,
    private val getCurrency: GetCurrencyUseCase,
    private val createCurrency: CreateCurrencyUseCase,
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(params: Params) {
        val institutionToAccounts = combine(
            getRequisitionAccounts(params.requisitionId),
            getInstitution(params.institutionId)
        ) { accounts, institution ->
            institution to accounts
        }.first()

        val profile = getCurrentProfile().first()!!
        val mainCurrencyUnit = profile.currencyUnit

        institutionToAccounts.second.forEach { account ->
            val currencyId = getAccountCurrency(mainCurrencyUnit, account.currencyUnit).id

//            accountRepository.createAccount(
//                AccountCreate(
//                    profileId = profile.id,
//                    currencyId = currencyId,
//                    name = account.name,
//                    type = LocalAccountType.General,
//                    description = account.iban,
//                    startBalance = account.balance,
//                    totalBalance = account.balance
//                )
//            )
        }
    }

    private suspend fun getAccountCurrency(
        mainCurrencyUnit: CurrencyUnit,
        accountCurrencyUnit: CurrencyUnit
    ): Currency {
        return getCurrencies().first()
            .firstOrNull { it.unit.code == accountCurrencyUnit.code }
            .let { currency ->
                if (currency == null) {
                    val currencyRate = getCurrencyRate(mainCurrencyUnit.code, accountCurrencyUnit.code)
                    val currencyId = createCurrency(
                        CreateCurrencyUseCase.Params(
                            currencyUnitCode = accountCurrencyUnit.code,
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

    private fun getRequisitionAccounts(requisitionId: String): Flow<List<InstitutionAccount>> {
        return getRequisitionAccounts(GetRequisitionAccountsUseCase.Params(requisitionId))
            .filterIsInstance<RemoteResult.Success<List<InstitutionAccount>>>().map { it.data }
    }

    private fun getInstitution(institutionId: String): Flow<Institution> {
        return getInstitution(GetInstitutionUseCase.Params(institutionId))
            .filterIsInstance<RemoteResult.Success<Institution>>().map { it.data }
    }

    data class Params(
        val requisitionId: String,
        val institutionId: String,
    )
}