package org.expenny.core.domain.usecase.account

import kotlinx.coroutines.flow.first
import org.expenny.core.common.types.LocalAccountType
import org.expenny.core.domain.usecase.currency.GetMainCurrencyUseCase
import java.math.BigDecimal
import javax.inject.Inject

class CreateInitialAccountUseCase @Inject constructor(
    private val getMainCurrency: GetMainCurrencyUseCase,
    private val createAccount: CreateAccountUseCase,
) {

    suspend operator fun invoke(params: Params): Long {
        val mainCurrencyId = getMainCurrency().first()!!.id

        return createAccount(
            with(params) {
                CreateAccountUseCase.Params(
                    currencyId = mainCurrencyId,
                    type = LocalAccountType.Cash,
                    description = "",
                    name = name,
                    startBalance = startBalance
                )
            }
        )
    }

    data class Params(
        val name: String,
        val startBalance: BigDecimal
    )
}