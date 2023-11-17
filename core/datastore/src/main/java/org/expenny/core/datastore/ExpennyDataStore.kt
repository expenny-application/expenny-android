package org.expenny.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpennyDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    companion object {
        val CURRENT_PROFILE_ID_KEY = longPreferencesKey(name = "current_profile_id")
        val PASSCODE_KEY = stringPreferencesKey(name = "passcode")
        val REMINDER_TIME_UTC_KEY = stringPreferencesKey(name = "reminder_time_utc")
        val IS_REMINDER_ENABLED_KEY = booleanPreferencesKey(name = "is_reminder_enabled")
        val IS_BIOMETRIC_ENROLLED_KEY = booleanPreferencesKey(name = "is_biometric_enrolled")
        val IS_DARK_MODE_KEY = booleanPreferencesKey(name = "is_dark_mode")
        val IS_ONBOARDING_PASSED_KEY = booleanPreferencesKey(name = "onboarding_passed")
        val IS_SETUP_PASSED_KEY = booleanPreferencesKey(name = "setup_passed")
    }

    suspend fun <T> put(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit {
            it.remove(key = key)
        }
    }

    fun <T> get(key: Preferences.Key<T>, default: T): Flow<T> = dataStore.data.map { it[key] ?: default }

    fun <T> get(key: Preferences.Key<T>): Flow<T?> = dataStore.data.map { it[key] }
}