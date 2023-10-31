package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.model.profile.Profile
import org.expenny.core.model.profile.ProfileCreate
import org.expenny.core.model.profile.ProfileUpdate

interface ProfileRepository {

    fun getProfiles(): Flow<List<Profile>>

    suspend fun getProfile(id: Long): Profile?

    suspend fun createProfile(profile: ProfileCreate): Long

    suspend fun updateProfile(profile: ProfileUpdate)

    suspend fun deleteProfile(id: Long)
}