package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.common.extensions.sumByDecimal
import org.expenny.core.model.account.AccountsBalanceData
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.currency.CurrencyAmount
import javax.inject.Inject

class GetAccountsBalanceUseCase @Inject constructor(
    private val getAccountsWithRecords: GetAccountsWithRecordsUseCase,
) {

    operator fun invoke(params: Params) : Flow<AccountsBalanceData> {
        return getAccountsWithRecords(GetAccountsWithRecordsUseCase.Params(params.accountIds))
            .map { list ->
                val targetCurrency = params.currency

                val balances = list.map {
                    it.account.totalBalance.convertTo(targetCurrency)
                }

                val lastRecord = list.flatMap { it.records }.maxByOrNull { it.date }
                val balance = balances.sumByDecimal { it.value }

                AccountsBalanceData(
                    accounts = list.map { it.account },
                    balance = CurrencyAmount(targetCurrency, balance),
                    lastRecord = lastRecord
                )
            }
    }

    data class Params(
        val accountIds: List<Long> = listOf(),
        val currency: Currency
    )
}