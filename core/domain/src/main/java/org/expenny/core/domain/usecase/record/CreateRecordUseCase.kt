package org.expenny.core.domain.usecase.record

import android.net.Uri
import kotlinx.coroutines.flow.first
import org.expenny.core.common.types.RecordType
import org.expenny.core.common.types.RecordType.Companion.transactionType
import org.expenny.core.domain.usecase.profile.GetCurrentProfileUseCase
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.record.RecordCreate
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

class CreateRecordUseCase @Inject constructor(
    private val getCurrentProfile: GetCurrentProfileUseCase,
    private val recordRepository: RecordRepository
) {

    suspend operator fun invoke(params: Params) {
        val profileId = getCurrentProfile().first()!!.id

        with(params) {
            recordRepository.createRecord(
                when (type) {
                    RecordType.Income,
                    RecordType.Expense -> {
                        RecordCreate.Transaction(
                            type = type.transactionType!!,
                            profileId = profileId,
                            accountId = accountId,
                            categoryId = categoryId,
                            receiptsUris = receiptsUris,
                            description = description,
                            labels = labels,
                            amount = amount,
                            date = date,
                        )
                    }
                    RecordType.Transfer -> {
                        RecordCreate.Transfer(
                            profileId = profileId,
                            accountId = accountId,
                            receiptsUris = receiptsUris,
                            description = description,
                            labels = labels,
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
        val type: RecordType,
        val accountId: Long,
        val transferAccountId: Long?,
        val categoryId: Long?,
        val receiptsUris: List<Uri>,
        val description: String,
        val labels: List<String>,
        val amount: BigDecimal,
        val transferAmount: BigDecimal?,
        val transferFee: BigDecimal?,
        val date: LocalDateTime,
    )
}