package org.expenny.core.domain.usecase.category

import kotlinx.coroutines.flow.first
import org.expenny.core.domain.repository.CategoryRepository
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.category.Category
import org.expenny.core.model.record.Record
import javax.inject.Inject

class GetMostUsedCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val recordRepository: RecordRepository
) {

    suspend operator fun invoke() : Category? {
        val transactions = recordRepository.getRecordsDesc().first().filterIsInstance(Record.Transaction::class.java)

        return if (transactions.isNotEmpty()) {
            transactions.filter { it.category != null }.groupBy { it.category }.maxBy { it.value.size }.key
        } else {
            categoryRepository.getCategories().first().takeIf { it.isNotEmpty() }?.get(0)
        }
    }
}