package org.expenny.core.data.tasks.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.expenny.core.domain.repository.AlarmRepository
import org.expenny.core.domain.repository.LocalRepository
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class BootReceiver : HiltBroadcastReceiver() {

    @Inject lateinit var localRepository: LocalRepository
    @Inject lateinit var alarmRepository: AlarmRepository

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // Re-schedule alarm on reboot https://developer.android.com/training/scheduling/alarms#boot
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            Timber.i("${intent.action} intent action received")
            goAsync {
                localRepository.getReminderTime().first().also {
                    alarmRepository.setReminderAlarm(it)
                }
            }
        }
    }

    private fun BroadcastReceiver.goAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob()).launch(context) {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }
}