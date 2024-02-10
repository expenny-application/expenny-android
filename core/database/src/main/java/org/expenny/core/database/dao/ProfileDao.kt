package org.expenny.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.expenny.core.database.model.entity.ProfileEntity

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile WHERE profile.profileId = :id")
    suspend fun selectById(id: Long): ProfileEntity?

    @Query("SELECT * FROM profile")
    fun selectAll(): Flow<List<ProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(profileEntity: ProfileEntity): Long

    @Query("DELETE FROM profile WHERE profile.profileId == :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM profile")
    suspend fun deleteAll()
}