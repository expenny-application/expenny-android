package org.expenny.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.expenny.core.common.utils.Constants
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ExpennyDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val currentProfileIdKey = longPreferencesKey("current_profile_id")
    private val passcodeKey = stringPreferencesKey("passcode")
    private val reminderTimeUtcKey = stringPreferencesKey("reminder_time_utc")
    private val isReminderEnabledKey = booleanPreferencesKey("is_reminder_enabled")
    private val isBiometricEnrolledKey = booleanPreferencesKey("is_biometric_enrolled")
    private val isDarkThemeKey = booleanPreferencesKey("is_dark_theme")
    private val isOnboardingPassedKey = booleanPreferencesKey("is_onboarding_passed")
    private val isSetupPassedKey = booleanPreferencesKey("is_setup_passed")
    private val goCardlessAccessToken = stringPreferencesKey("gocardless_access_token")
    private val goCardlessRefreshToken = stringPreferencesKey("gocardless_refresh_token")

    suspend fun setCurrentProfileId(value: Long) = put(currentProfileIdKey, value)

    suspend fun setPasscode(value: String?) {
        if (value == null) remove(passcodeKey)
        else put(passcodeKey, value)
    }

    suspend fun setReminderTimeUtc(value: LocalTime) {
        val stringValue = value.format(DateTimeFormatter.ofPattern(Constants.DEFAULT_REMINDER_TIME_FORMAT))
        put(reminderTimeUtcKey, stringValue)
    }

    suspend fun setIsReminderEnabled(value: Boolean) = put(isReminderEnabledKey, value)

    suspend fun setIsBiometricEnrolled(value: Boolean) = put(isBiometricEnrolledKey, value)

    suspend fun setIsDarkTheme(value: Boolean?) {
        if (value == null) remove(isDarkThemeKey)
        else put(isDarkThemeKey, value)
    }

    suspend fun setIsOnboardingPassed(value: Boolean) = put(isOnboardingPassedKey, value)

    suspend fun setIsSetupPassed(value: Boolean) = put(isSetupPassedKey, value)

    suspend fun setGoCardlessAccessToken(value: String) = put(goCardlessAccessToken, value)

    suspend fun setGoCardlessRefreshToken(value: String) = put(goCardlessRefreshToken, value)

    fun isDarkTheme() = get(isDarkThemeKey)

    fun isSetupPassed() = get(isSetupPassedKey, false)

    fun isOnboardingPassed() = get(isOnboardingPassedKey, false)

    fun isBiometricEnrolled() = get(isBiometricEnrolledKey, false)

    fun isReminderEnabled() = get(isReminderEnabledKey, false)

    fun getGoCardlessAccessToken() = get(goCardlessAccessToken)

    fun getGoCardlessRefreshToken() = get(goCardlessRefreshToken)

    fun getCurrentProfileId() = get(currentProfileIdKey)

    fun getPasscode() = get(passcodeKey)

    fun getReminderTime() = get(reminderTimeUtcKey, Constants.DEFAULT_REMINDER_TIME).map { timeUtc ->
        LocalTime.parse(timeUtc, DateTimeFormatter.ofPattern(Constants.DEFAULT_REMINDER_TIME_FORMAT))
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    private suspend fun <T> put(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    private suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit {
            it.remove(key = key)
        }
    }

    private fun <T> get(key: Preferences.Key<T>, default: T): Flow<T> = dataStore.data.map { it[key] ?: default }

    private fun <T> get(key: Preferences.Key<T>): Flow<T?> = dataStore.data.map { it[key] }
}