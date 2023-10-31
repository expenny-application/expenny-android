package org.expenny.core.model.record

import android.net.Uri
import org.expenny.core.common.types.RecordType
import java.math.BigDecimal
import java.time.Instant

sealed interface RecordAmend {

    data class Create(
        val type: RecordType,
        val profileId: Long,
        val accountId: Long,
        val transferAccountId: Long?,
        val categoryId: Long?,
        val labelIds: List<Long>,
        val receiptsUris: List<Uri>,
        val description: String,
        val subject: String,
        val postedAmount: BigDecimal,
        val clearedAmount: BigDecimal,
        val date: Instant,
    ) : RecordAmend

    data class Update(
        val id: Long,
        val profileId: Long,
        val type: RecordType,
        val accountId: Long,
        val transferAccountId: Long?,
        val categoryId: Long?,
        val labelIds: List<Long>,
        val receiptsUris: List<Uri>,
        val description: String,
        val subject: String,
        val postedAmount: BigDecimal,
        val clearedAmount: BigDecimal,
        val date: Instant,
    ) : RecordAmend

    data class Delete(
        val id: Long
    ) : RecordAmend
}