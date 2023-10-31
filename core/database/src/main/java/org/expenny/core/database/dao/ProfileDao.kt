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
    suspend fun selectProfile(id: Long): ProfileEntity?

    @Query("SELECT * FROM profile")
    fun selectProfiles(): Flow<List<ProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profileEntity: ProfileEntity): Long
}