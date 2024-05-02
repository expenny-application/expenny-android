package org.expenny.core.domain.usecase.preferences

import kotlinx.coroutines.flow.Flow
import org.expenny.core.datastore.ExpennyDataStore
import java.time.LocalTime
import javax.inject.Inject

class GetReminderTimePreferenceUseCase @Inject constructor(
    private val preferences: ExpennyDataStore,
) {

    operator fun invoke(): Flow<LocalTime> {
        return preferences.getReminderTime()
    }
}