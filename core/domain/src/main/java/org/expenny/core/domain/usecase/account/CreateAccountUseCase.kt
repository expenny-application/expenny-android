package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.first
import org.expenny.core.common.types.AccountType
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.domain.repository.AccountRepository
import org.expenny.core.model.account.AccountCreate
import java.math.BigDecimal
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val getCurrentProfile: GetCurrentProfileUseCase
) {

    suspend operator fun invoke(params: Params): Long {
        val profileId = getCurrentProfile().first()!!.id

        return accountRepository.createAccount(
            with(params) {
                AccountCreate(
                    profileId = profileId,
                    currencyId = currencyId,
                    name = name,
                    type = type,
                    description = description,
                    startBalance = startBalance,
                    // the same as start balance because it is new
                    totalBalance = startBalance
                )
            }
        )
    }

    data class Params(
        val currencyId: Long,
        val name: String,
        val type: AccountType,
        val description: String,
        val startBalance: BigDecimal
    )
}