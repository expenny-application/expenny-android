package org.expenny.core.data.repository

import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.common.utils.Constants
import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.datastore.ExpennyDataStore.Companion.CURRENT_PROFILE_ID_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.IS_BIOMETRIC_ENROLLED_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.IS_DARK_MODE_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.IS_ONBOARDING_PASSED_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.IS_REMINDER_ENABLED_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.IS_SETUP_PASSED_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.PASSCODE_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.REMINDER_TIME_UTC_KEY
import org.expenny.core.domain.repository.LocalRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val dataStore: ExpennyDataStore,
    private val workManager: WorkManager
) : LocalRepository {

    override suspend fun setOnboardingPassed() {
        dataStore.put(IS_ONBOARDING_PASSED_KEY, true)
    }

    override suspend fun setSetupPassed() {
        dataStore.put(IS_SETUP_PASSED_KEY, true)
    }

    override suspend fun setCurrentProfile(id: Long) {
        dataStore.put(CURRENT_PROFILE_ID_KEY, id)
    }

    override suspend fun setThemeSystemDefault() {
        dataStore.remove(IS_DARK_MODE_KEY)
    }

    override suspend fun setThemeDarkMode(isDarkMode: Boolean) {
        dataStore.put(IS_DARK_MODE_KEY, isDarkMode)
    }

    override suspend fun setPasscode(passcode: String?) {
        if (passcode == null) {
            dataStore.remove(PASSCODE_KEY)
        } else {
            dataStore.put(PASSCODE_KEY, passcode)
        }
    }

    override suspend fun setBiometricEnrolled(isEnrolled: Boolean) {
        dataStore.put(IS_BIOMETRIC_ENROLLED_KEY, isEnrolled)
    }

    override suspend fun setReminderEnabled(isEnabled: Boolean) {
        dataStore.put(IS_REMINDER_ENABLED_KEY, isEnabled)
    }

    override suspend fun setReminderTime(time: LocalTime) {
        val timeString = time.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_REMINDER_TIME_FORMAT))
        dataStore.put(REMINDER_TIME_UTC_KEY, timeString)
    }

    override fun isDarkMode(): Flow<Boolean?> {
        return dataStore.get(IS_DARK_MODE_KEY)
    }

    override fun isSetupPassed(): Flow<Boolean> {
        return dataStore.get(IS_SETUP_PASSED_KEY, false)
    }

    override fun isOnboardingPassed(): Flow<Boolean> {
        return dataStore.get(IS_ONBOARDING_PASSED_KEY, false)
    }

    override fun getCurrentProfileId(): Flow<Long?> {
        return dataStore.get(CURRENT_PROFILE_ID_KEY)
    }

    override fun getPasscode(): Flow<String?> {
        return dataStore.get(PASSCODE_KEY)
    }

    override fun isBiometricEnrolled(): Flow<Boolean> {
        return dataStore.get(IS_BIOMETRIC_ENROLLED_KEY, false)
    }

    override fun isReminderEnabled(): Flow<Boolean> {
        return dataStore.get(IS_REMINDER_ENABLED_KEY, false)
    }

    override fun getReminderTime(): Flow<LocalTime> {
        return dataStore.get(REMINDER_TIME_UTC_KEY, Constants.DEFAULT_REMINDER_TIME).map { timeUtc ->
            return@map LocalTime.parse(timeUtc, DateTimeFormatter.ofPattern(Constants.DEFAULT_REMINDER_TIME_FORMAT))
        }
    }

    override suspend fun clear() {
        dataStore.clear()
    }
}