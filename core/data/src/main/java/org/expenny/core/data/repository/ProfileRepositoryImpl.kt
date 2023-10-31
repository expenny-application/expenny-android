package org.expenny.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.data.mapper.DataMapper.toModel
import org.expenny.core.data.mapper.DataMapper.toEntity
import org.expenny.core.database.ExpennyDatabase
import org.expenny.core.domain.repository.ProfileRepository
import org.expenny.core.model.profile.Profile
import org.expenny.core.model.profile.ProfileCreate
import org.expenny.core.model.profile.ProfileUpdate
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val database: ExpennyDatabase,
) : ProfileRepository {
    private val profileDao = database.profileDao()

    override fun getProfiles(): Flow<List<Profile>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(id: Long): Profile? {
        return profileDao.selectProfile(id)?.toModel()
    }

    override suspend fun createProfile(profile: ProfileCreate): Long {
        return profileDao.insertProfile(profile.toEntity())
    }

    override suspend fun updateProfile(profile: ProfileUpdate) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProfile(id: Long) {
        TODO("Not yet implemented")
    }
}