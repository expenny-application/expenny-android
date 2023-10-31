package org.expenny.core.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.embedded.LabelEmbedded
import org.expenny.core.database.model.entity.AccountEntity
import org.expenny.core.database.model.entity.LabelEntity

@Dao
interface LabelDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(label: LabelEntity): Long

    @Update(entity = LabelEntity::class)
    suspend fun update(label: LabelEntity.Update)

    @Transaction
    @Query("SELECT * FROM label WHERE label.labelId == :id")
    suspend fun select(id: Long): LabelEmbedded?

    @Transaction
    @Query("SELECT * FROM label WHERE label.profileId == :profileId ORDER BY labelId DESC")
    fun selectByProfileId(profileId: Long): Flow<List<LabelEmbedded>>

    @Query("DELETE FROM label WHERE label.labelId == :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM label WHERE label.labelId IN (:ids)")
    suspend fun deleteAll(ids: List<Long>)
}