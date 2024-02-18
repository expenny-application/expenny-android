package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.first
import org.expenny.core.common.types.AccountType
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.model.account.AccountUpdate
import java.math.BigDecimal
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(params: Params) {
        val account = accountRepository.getAccount(params.id).first()!!
        val totalBalance = account.totalBalance.value - (account.startBalance.value - params.startBalance)

        accountRepository.updateAccount(
            with(params) {
                AccountUpdate(
                    id = id,
                    currencyId = currencyId,
                    name = name,
                    type = type,
                    description = description,
                    startBalance = startBalance,
                    totalBalance = totalBalance
                )
            }
        )
    }

    data class Params(
        val id: Long,
        val currencyId: Long,
        val name: String,
        val type: AccountType,
        val description: String,
        val startBalance: BigDecimal,
    )
}