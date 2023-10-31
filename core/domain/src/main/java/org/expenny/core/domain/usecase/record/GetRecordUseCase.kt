package org.expenny.core.domain.usecase.record

import kotlinx.coroutines.flow.Flow
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.record.Record
import javax.inject.Inject

class GetRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    operator fun invoke(params: Params): Flow<Record?> {
        return recordRepository.getRecord(params.id)
    }

    data class Params(
        val id: Long
    )
}