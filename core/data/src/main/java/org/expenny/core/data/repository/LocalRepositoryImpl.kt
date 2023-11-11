package org.expenny.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.expenny.core.datastore.ExpennyDataStore
import org.expenny.core.datastore.ExpennyDataStore.Companion.CURRENT_PROFILE_ID_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.IS_DARK_MODE_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.IS_ONBOARDING_PASSED_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.IS_SETUP_PASSED_KEY
import org.expenny.core.datastore.ExpennyDataStore.Companion.PASSCODE_KEY
import org.expenny.core.domain.repository.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val dataStore: ExpennyDataStore
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
}