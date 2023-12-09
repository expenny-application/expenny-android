package org.expenny.core.domain.usecase.record

import android.net.Uri
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.RecordType.Companion.transactionType
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.record.RecordUpdate
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import javax.inject.Inject

class UpdateRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    suspend operator fun invoke(params: Params) {
        with(params) {
            recordRepository.updateRecord(
                when (type) {
                    RecordType.Income,
                    RecordType.Expense -> {
                        RecordUpdate.Transaction(
                            id = id,
                            type = type.transactionType!!,
                            accountId = accountId,
                            categoryId = categoryId,
                            labelIds = labelIds,
                            receiptsUris = receiptsUris,
                            description = description,
                            subject = subject,
                            amount = amount,
                            date = date,
                        )
                    }
                    RecordType.Transfer -> {
                        RecordUpdate.Transfer(
                            id = id,
                            accountId = accountId,
                            labelIds = labelIds,
                            receiptsUris = receiptsUris,
                            description = description,
                            subject = subject,
                            amount = amount,
                            date = date,
                            transferAmount = transferAmount!!,
                            transferAccountId = transferAccountId!!,
                            transferFee = transferFee!!
                        )
                    }
                }
            )
        }
    }

    data class Params(
        val id: Long,
        val type: RecordType,
        val accountId: Long,
        val transferAccountId: Long?,
        val categoryId: Long?,
        val labelIds: List<Long>,
        val receiptsUris: List<Uri>,
        val description: String,
        val subject: String,
        val amount: BigDecimal,
        val transferAmount: BigDecimal?,
        val transferFee: BigDecimal?,
        val date: LocalDateTime,
    )
}