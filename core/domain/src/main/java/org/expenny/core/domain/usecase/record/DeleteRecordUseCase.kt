package org.expenny.core.domain.usecase.record

import org.expenny.core.domain.repository.RecordRepository
import javax.inject.Inject

class DeleteRecordUseCase @Inject constructor(
    private val recordRepository: RecordRepository,
) {

    suspend operator fun invoke(vararg recordIds: Long) {
        recordRepository.deleteRecords(*recordIds)
    }
}