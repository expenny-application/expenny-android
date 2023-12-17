package org.expenny.core.domain.usecase.record

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.domain.repository.RecordRepository
import javax.inject.Inject

class GetRecordLabelsUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    operator fun invoke() : Flow<List<String>> {
        return recordRepository.getRecordsDesc().map { records ->
            records.flatMap { it.labels }.distinct()
        }
    }
}