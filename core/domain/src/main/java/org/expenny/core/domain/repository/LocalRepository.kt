package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    suspend fun setOnboardingPassed()

    suspend fun setSetupPassed()

    suspend fun setCurrentProfile(id: Long)

    suspend fun setThemeSystemDefault()

    suspend fun setThemeDarkMode(isDarkMode: Boolean)

    suspend fun setLocale(locale: String)

    fun isDarkMode(): Flow<Boolean?>

    fun getLocale(): Flow<String>

    fun isSetupPassed(): Flow<Boolean>

    fun isOnboardingPassed(): Flow<Boolean>

    fun getCurrentProfileId(): Flow<Long?>
}