package org.expenny.core.domain.repository

import java.time.LocalTime

interface AlarmRepository {

    fun setReminderAlarm(time: LocalTime)

    fun cancelReminderAlarm()

    fun canScheduleAlarms(): Boolean
}