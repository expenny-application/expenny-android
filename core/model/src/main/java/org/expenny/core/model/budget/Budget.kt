package org.expenny.core.model.budget

import org.expenny.core.common.extensions.percentageOf
import org.expenny.core.model.category.Category
import org.expenny.core.model.profile.Profile
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

data class Budget(
    val id: Long,
    val profile: Profile,
    val category: Category,
    val limitValue: BigDecimal,
    val spentValue: BigDecimal = BigDecimal.ZERO,
    val startDate: LocalDate,
    val endDate: LocalDate?
) {

    val isOpened: Boolean
        get() = endDate == null

    val progressPercentage: BigDecimal
        get() = spentValue.percentageOf(limitValue).setScale(0, RoundingMode.HALF_UP)

    val leftValue: BigDecimal
        get() {
            val left = limitValue - spentValue
            return (if (left < BigDecimal.ZERO) BigDecimal.ZERO else left).setScale(spentValue.scale())
        }

    // TODO
    val favorability: BigDecimal = BigDecimal.ZERO.setScale(1)
}
