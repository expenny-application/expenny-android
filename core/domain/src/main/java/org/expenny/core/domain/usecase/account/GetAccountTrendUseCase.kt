package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.common.extensions.toList
import org.expenny.core.common.types.AccountTrendType
import org.expenny.core.common.types.TransactionType
import org.expenny.core.model.common.DatedAmount
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class GetAccountTrendUseCase @Inject constructor(
    private val getAccountBalanceHistory: GetAccountBalanceHistoryUseCase,
    private val getAccountFundsHistory: GetAccountFundsHistoryUseCase,
) {

    operator fun invoke(params: Params): Flow<List<DatedAmount>> {
        return getAccountHistory(params.accountId, params.trendType)
            .map { accountHistory ->
                // Build a trend for given date range and account history
                return@map buildList {
                    params.dateRange.toList().forEach { date ->
                        val amount = accountHistory.getNearestAmount(date)
                        add(DatedAmount(amount, date))
                    }
                }
            }
    }

    private fun getAccountHistory(accountId: Long, trendType: AccountTrendType): Flow<List<DatedAmount>> {
        return when (trendType) {
            AccountTrendType.Balance ->
                getAccountBalanceHistory(GetAccountBalanceHistoryUseCase.Params(accountId))
            AccountTrendType.Expenses ->
                getAccountFundsHistory(GetAccountFundsHistoryUseCase.Params(accountId, TransactionType.Outgoing, true))
            AccountTrendType.Income ->
                getAccountFundsHistory(GetAccountFundsHistoryUseCase.Params(accountId, TransactionType.Incoming, true))
        }
    }

    private fun List<DatedAmount>.getNearestAmount(date: LocalDate): BigDecimal {
        if (isEmpty()) {
            return BigDecimal.ZERO
        }

        val startDatedAmount = first()
        val endDatedAmount = last()

        if (date.isBefore(startDatedAmount.date)) {
            return BigDecimal.ZERO
        } else if (date.isAfter(endDatedAmount.date)) {
            return endDatedAmount.amount
        }

        return last { it.date <= date }.amount
    }

    data class Params(
        val accountId: Long,
        val dateRange: ClosedRange<LocalDate>,
        val trendType: AccountTrendType,
    )
}