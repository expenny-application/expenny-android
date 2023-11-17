package org.expenny.core.domain.usecase.preferences

import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class GetReminderTimeDelayInSecondsUseCase @Inject constructor() {

    operator fun invoke(reminderTime: LocalTime): Long {
        val today = LocalDateTime.now()
        val todayReminderTime = LocalDateTime.now()
            .withHour(reminderTime.hour)
            .withMinute(reminderTime.minute)
            .withSecond(0)

        val delaySeconds = if (todayReminderTime.isAfter(today)) {
            // start reminder from today
            Duration.between(today, todayReminderTime).seconds
        } else {
            // start reminding from tomorrow
            Duration.between(today, todayReminderTime.plusDays(1)).seconds
        }

        return delaySeconds
    }
}