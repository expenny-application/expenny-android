package org.expenny.core.ui.mapper

import org.expenny.core.common.extensions.isNegative
import org.expenny.core.common.extensions.isZero
import org.expenny.core.common.extensions.toDateString
import org.expenny.core.model.record.Record
import org.expenny.core.ui.data.AmountUi
import org.expenny.core.ui.data.RecordUi
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
        val amount = amountMapper(typedAmount).prependAmountSign()
        val transferAmount = (this as? Record.Transfer)?.typedTransferAmount?.let { amountMapper(it) }?.prependAmountSign()
        val category = (this as? Record.Transaction)?.category?.let { categoryMapper(it) }
        val title = (this as? Record.Transfer)?.account?.name ?: category?.name
        val subtitle = (this as? Record.Transfer)?.transferAccount?.name ?: account.name

        return RecordUi.Item(
            id = id,
            title = title,
            subtitle = subtitle,
            description = description,
            labels = labels,
            attachmentsCount = attachments.size,
            amount = amount,
            transferAmount = transferAmount,
            category = category,
            date = date.toDateString()
        )
    }

    private fun AmountUi.prependAmountSign() : AmountUi {
        if (value.isZero() || value.isNegative()) return this
        return copy(displayValue = "+$displayValue")
    }
}