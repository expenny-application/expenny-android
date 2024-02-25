package org.expenny.core.domain.usecase.record

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.common.types.RecordType
import org.expenny.core.domain.repository.RecordRepository
import org.expenny.core.model.record.Record
import org.threeten.extra.LocalDateRange
import javax.inject.Inject

class GetRecordsUseCase @Inject constructor(
    private val recordRepository: RecordRepository
) {

    operator fun invoke(): Flow<List<Record>> {
        return invoke(Params())
    }

    operator fun invoke(params: Params): Flow<List<Record>> {
        // Moved filtering here from repository because maintaining a sql query is much harder in this case
        return recordRepository.getRecordsDesc().map { records ->
            records.filter { it.matchesFilter(params) }
        }
    }

    private fun Record.matchesFilter(params: Params): Boolean {
        return buildList {
            if (params.withoutCategory) {
                add(hasCategory().not())
            }
            add(matchesCategoriesFilter(params.categories))
            add(matchesAccountsFilter(params.accounts))
            add(matchesLabelsFilter(params.labels))
            add(matchesTypesFilter(params.types))
            add(matchesDateRangeFilter(params.dateRange))
        }.all { it }
    }

    private fun Record.hasCategory(): Boolean {
        return when (this) {
            is Record.Transaction -> this.category != null
            is Record.Transfer -> true
        }
    }

    private fun Record.matchesCategoriesFilter(categories: List<Long>): Boolean {
        return categories.isEmpty()
                || (this as? Record.Transaction)?.category?.id in categories
    }

    private fun Record.matchesAccountsFilter(accounts: List<Long>): Boolean {
        return accounts.isEmpty()
                || account.id in accounts
                || (this as? Record.Transfer)?.transferAccount?.id in accounts
    }

    private fun Record.matchesLabelsFilter(labels: List<String>): Boolean {
        return labels.isEmpty() || this.labels.any { it in labels }
    }

    private fun Record.matchesTypesFilter(types: List<RecordType>): Boolean {
        return types.isEmpty() || this.recordType in types
    }

    private fun Record.matchesDateRangeFilter(dateRange: LocalDateRange?): Boolean {
        return dateRange?.contains(this.date.toLocalDate()) ?: true
    }

    data class Params(
        val accounts: List<Long> = listOf(),
        val labels: List<String> = listOf(),
        val categories: List<Long> = listOf(),
        val types: List<RecordType> = listOf(),
        val dateRange: LocalDateRange? = null,
        val withoutCategory: Boolean = false,
    )
}