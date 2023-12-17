package org.expenny.core.ui.mapper

import org.expenny.core.common.extensions.isNegative
import org.expenny.core.common.extensions.isZero
import org.expenny.core.common.extensions.toDateString
import org.expenny.core.model.record.Record
import org.expenny.core.ui.data.ui.*
import javax.inject.Inject

class RecordMapper @Inject constructor(
    private val amountMapper: AmountMapper,
    private val categoryMapper: CategoryMapper,
) {

    operator fun invoke(model: Record): RecordUi.Item {
        return model.toItem()
    }

    operator fun invoke(model: List<Record>): List<RecordUi> {
        return model.map { it.toItem() }
    }

    private fun Record.toItem(): RecordUi.Item {
        return when (this) {
            is Record.Transfer -> {
                RecordUi.Item.Transfer(
                    id = id,
                    key = id,
                    description = description,
                    account = account.name,
                    transferAccount = transferAccount.name,
                    postedAmount = amountMapper(amount),
                    clearedAmount = amountMapper(transferAmount),
                    receiptsCount = receipts.size,
                    labels = labels,
                    isConversionApplied = account.currency != transferAccount.currency,
                    date = date.toDateString(),
                )
            }
            is Record.Transaction -> {
                RecordUi.Item.Transaction(
                    id = id,
                    key = id,
                    category = category?.let { categoryMapper(it) },
                    description = description,
                    account = account.name,
                    postedAmount = amountMapper(transactionAmount).prependPlusSign(),
                    receiptsCount = receipts.size,
                    labels = labels,
                    date = date.toDateString()
                )
            }
        }
    }

    private fun AmountUi.prependPlusSign(): AmountUi {
        return if (!displayValue.startsWith("+") && !value.isZero() && !value.isNegative()) {
            copy(displayValue = "+$displayValue")
        } else this
    }
}