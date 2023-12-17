package org.expenny.core.database.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.embedded.RecordEmbedded
import org.expenny.core.database.model.entity.RecordEntity

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