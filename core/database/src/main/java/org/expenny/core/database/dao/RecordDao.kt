package org.expenny.core.database.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import org.expenny.core.common.extensions.toInt
import org.expenny.core.common.types.RecordType
import org.expenny.core.database.model.embedded.RecordEmbedded
import org.expenny.core.database.model.entity.RecordEntity
import org.expenny.core.database.utils.LocalDateTimeConverter
import org.threeten.extra.LocalDateRange

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(record: RecordEntity): Long

    @Update(entity = RecordEntity::class)
    suspend fun update(record: RecordEntity.Update)

    @Transaction
    @Query("SELECT * FROM record WHERE record.recordId = :id")
    fun select(id: Long): Flow<RecordEmbedded?>

    @Query("DELETE FROM record WHERE record.recordId == :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM record WHERE record.accountId == :id OR record.transferAccountId == :id")
    suspend fun deleteByAccountId(id: Long)

    @Query("UPDATE record SET categoryId = :newCategoryId WHERE categoryId == :oldCategoryId")
    suspend fun updateCategoryId(oldCategoryId: Long?, newCategoryId: Long?)

    @Transaction
    @Query("SELECT * FROM record WHERE record.profileId == :profileId ORDER BY date DESC")
    fun selectAllDesc(profileId: Long): Flow<List<RecordEmbedded>>

    @RawQuery(observedEntities = [RecordEntity::class])
    fun selectAllDesc(query: SupportSQLiteQuery): Flow<List<RecordEmbedded>>

    fun selectAllDesc(
        profileId: Long,
        labelIds: List<Long>,
        accountIds: List<Long>,
        categoryIds: List<Long>,
        types: List<RecordType>,
        dateRange: LocalDateRange?,
        withoutCategory: Boolean,
    ): Flow<List<RecordEmbedded>> {
        // Yes, this is terrible. But at least it works. It is better not to read the code while drunk.
        // There is a chance to catch a desire to redo everything and something might break after that
        val bindArgs = mutableListOf<Any>()

        val queryString = buildString {
            append("SELECT * FROM record")
            append(" WHERE (record.profileId == ?)")
            bindArgs.add(profileId)

            val accountIdsString = Array(accountIds.size){'?'}.joinToString()
            append(" AND (${accountIds.isEmpty().toInt()} == 1 OR record.accountId IN ($accountIdsString) OR record.transferAccountId IN ($accountIdsString))")
            if (accountIds.isNotEmpty()) {
                bindArgs.addAll(accountIds)
                bindArgs.addAll(accountIds)
            }

            val categoryIdsString = Array(categoryIds.size){'?'}.joinToString()
            val categoryQueryPartConditions = buildString {
                if (withoutCategory) {
                    append("record.categoryId IN ($categoryIdsString)")
                    append(" OR (record.categoryId IS NULL AND record.type != 'Transfer')")
                } else {
                    append("${categoryIds.isEmpty().toInt()} == 1")
                    append(" OR record.categoryId IN ($categoryIdsString)")
                }
            }
            append(" AND ($categoryQueryPartConditions)")
            if (categoryIds.isNotEmpty()) bindArgs.addAll(categoryIds)

            val labelIdsString = Array(labelIds.size){'?'}.joinToString()
            append(" AND (${labelIds.isEmpty().toInt()} == 1 OR EXISTS(SELECT record_label_ref.labelId FROM record_label_ref WHERE record_label_ref.recordId == record.recordId AND record_label_ref.labelId in ($labelIdsString)))")
            if (labelIds.isNotEmpty()) bindArgs.addAll(labelIds)

            val typesString = Array(types.size){'?'}.joinToString()
            append(" AND (${types.isEmpty().toInt()} == 1 OR record.type IN ($typesString))")
            if (types.isNotEmpty()) bindArgs.addAll(types.map { it.name })

            if (dateRange != null) {
                val startDate = LocalDateTimeConverter().localDateTimeToLong(dateRange.start.atStartOfDay())
                val endDate = LocalDateTimeConverter().localDateTimeToLong(dateRange.end.atStartOfDay())
                append(" AND (record.date BETWEEN $startDate AND $endDate)")
            }

            append(" ORDER BY record.date DESC")
        }

        return selectAllDesc(SimpleSQLiteQuery(queryString, bindArgs.toTypedArray()))
    }

    fun selectRecordsByAccountId(vararg accountIds: Long): Flow<List<RecordEmbedded>> {
        val queryString = buildString {
            append("SELECT * FROM record")
            if (accountIds.isNotEmpty()) {
                append(" WHERE record.accountId IN (${Array(accountIds.size){'?'}.joinToString()})")
            }
            append(" ORDER BY date DESC")
        }
        return selectAllDesc(SimpleSQLiteQuery(queryString, accountIds.toTypedArray()))
    }
}