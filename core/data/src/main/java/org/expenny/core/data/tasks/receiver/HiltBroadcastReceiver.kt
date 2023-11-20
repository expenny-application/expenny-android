package org.expenny.core.data.tasks.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper

/**
 * This class is created in order to be able to @Inject variables into Broadcast Receiver.
 * Simply Inherit this class into whatever BroadcastReceiver you need and freely use Dagger-Hilt after.
 */
abstract class HiltBroadcastReceiver : BroadcastReceiver() {

    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {}
}