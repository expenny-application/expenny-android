package org.expenny.core.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.expenny.core.common.extensions.mapFlatten
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
    private val recordDao = database.recordDao()
    private val categoryDao = database.categoryDao()
    private val accountDao = database.accountDao()
    private val currencyDao = database.currencyDao()
    private val recordFileDao = database.recordFileDao()
    private val fileDao = database.fileDao()

    override fun getProfiles(): Flow<List<Profile>> {
        return profileDao.selectAll().mapFlatten { toModel() }
    }

    override suspend fun getProfile(id: Long): Profile? {
        return profileDao.selectById(id)?.toModel()
    }

    override suspend fun createProfile(profile: ProfileCreate): Long {
        return profileDao.insert(profile.toEntity())
    }

    override suspend fun deleteAllProfiles() {
        database.withTransaction {
            profileDao.deleteAll()
            recordFileDao.deleteAll()
            fileDao.deleteAll()
            recordDao.deleteAll()
            accountDao.deleteAll()
            currencyDao.deleteAll()
            categoryDao.deleteAll()
        }
    }

    override suspend fun deleteProfile(id: Long) {
        database.withTransaction {
            profileDao.delete(id)
            recordFileDao.deleteByProfileId(id)
            fileDao.deleteByProfileId(id)
            recordDao.deleteByProfileId(id)
            accountDao.deleteByProfileId(id)
            currencyDao.deleteByProfileId(id)
            categoryDao.deleteByProfileId(id)
        }
    }

    override suspend fun deleteProfileData(id: Long) {
        database.withTransaction {
            recordFileDao.deleteByProfileId(id)
            fileDao.deleteByProfileId(id)
            recordDao.deleteByProfileId(id)
            accountDao.deleteByProfileId(id)
            currencyDao.selectMain(id).first()!!.also {
                currencyDao.deleteAllExcept(it.currency.currencyId)
            }
            categoryDao.deleteByProfileId(id)
        }
    }
}