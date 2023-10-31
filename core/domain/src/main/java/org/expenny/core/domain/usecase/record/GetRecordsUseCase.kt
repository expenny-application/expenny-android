package org.expenny.core.domain.usecase.record

import kotlinx.coroutines.flow.Flow
import org.expenny.core.common.types.RecordType
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.record.Record
import org.threeten.extra.LocalDateRange
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    operator fun invoke(params: Params): Flow<List<Record>> {
        return with(params) {
            recordRepository.getRecordsDesc(
                labelIds = labels,
                accountIds = accounts,
                categoryIds = categories,
                types = types,
                dateRange = dateRange,
                withoutCategory = withoutCategory,
            )
        }
    }

    operator fun invoke(): Flow<List<Record>> {
        return invoke(Params())
    }

    data class Params(
        val accounts: List<Long> = listOf(),
        val labels: List<Long> = listOf(),
        val categories: List<Long> = listOf(),
        val types: List<RecordType> = listOf(),
        val dateRange: LocalDateRange? = null,
        val withoutCategory: Boolean = false,
    )
}