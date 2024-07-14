package org.expenny.core.model.budgetgroup

import org.expenny.core.common.extensions.sumByDecimal
import org.expenny.core.common.types.IntervalType
import org.expenny.core.model.budget.Budget
import org.expenny.core.model.currency.Currency
import org.expenny.core.model.profile.Profile
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

sealed interface BudgetGroup {
    val id: Long
    val profile: Profile
    val currency: Currency
    val budgets: List<Budget>

    val averageFavorability: BigDecimal
        get() = if (budgets.isNotEmpty()) {
            val sum = budgets.sumByDecimal { it.favorability }
            sum.divide(BigDecimal(budgets.size), 1, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(0)
        }

    val totalSpentValue: BigDecimal
        get() = budgets.sumByDecimal { it.spentValue }

    val totalLimitValue: BigDecimal
        get() = budgets.sumByDecimal { it.limitValue }

    val totalLeftValue: BigDecimal
        get() = budgets.sumByDecimal { it.leftValue }

    val totalProgressPercentage: BigDecimal
        get() = budgets.sumByDecimal { it.progressPercentage }

    data class Periodic(
        override val id: Long,
        override val profile: Profile,
        override val currency: Currency,
        override val budgets: List<Budget>,
        val intervalType: IntervalType,
    ) : BudgetGroup

    data class Onetime(
        override val id: Long,
        override val profile: Profile,
        override val currency: Currency,
        override val budgets: List<Budget>,
        val name: String,
        val dateRange: ClosedRange<LocalDate>,
    ) : BudgetGroup
}

