package org.expenny.core.domain.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

interface LocalRepository {

    suspend fun setOnboardingPassed()

    suspend fun setSetupPassed()

    suspend fun setCurrentProfile(id: Long)

    suspend fun setThemeSystemDefault()

    suspend fun setThemeDarkMode(isDarkMode: Boolean)

    suspend fun setPasscode(passcode: String?)

    suspend fun setBiometricEnrolled(isEnrolled: Boolean)

    suspend fun setReminderEnabled(isEnabled: Boolean)

    suspend fun setReminderTime(time: LocalTime)

    fun isDarkMode(): Flow<Boolean?>

    fun isSetupPassed(): Flow<Boolean>

    fun isOnboardingPassed(): Flow<Boolean>

    fun getCurrentProfileId(): Flow<Long?>

    fun getPasscode(): Flow<String?>

    fun isBiometricEnrolled(): Flow<Boolean>

    fun isReminderEnabled(): Flow<Boolean>

    fun getReminderTime(): Flow<LocalTime>

    suspend fun clear()
}