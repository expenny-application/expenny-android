package org.expenny.core.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.app.ActivityOptionsCompat
import java.util.*

@Composable
fun rememberLauncherForTakePhotoResult(
    contract: TakePhoto,
    onResult: (Uri?) -> Unit
): ManagedTakePhotoResultLauncher {
    val currentContract = rememberUpdatedState(contract)
    val currentOnResult = rememberUpdatedState(onResult)

    val key = rememberSaveable { UUID.randomUUID().toString() }

    val activityResultRegistry = checkNotNull(LocalActivityResultRegistryOwner.current) {
        "No ActivityResultRegistryOwner was provided via LocalActivityResultRegistryOwner"
    }.activityResultRegistry

    val realLauncher = remember { TakePhotoResultLauncherHolder() }
    val returnedLauncher = remember { ManagedTakePhotoResultLauncher(realLauncher, currentContract) }

    DisposableEffect(activityResultRegistry, key, contract) {
        realLauncher.launcher = activityResultRegistry.register(key, contract) {
            // workaround to return Uri instead of Boolean
            currentOnResult.value(if (it) returnedLauncher.inputUri else null )
        }
        onDispose {
            realLauncher.unregister()
        }
    }
    return returnedLauncher
}

class TakePhoto : ActivityResultContract<Uri, Boolean>() {

    @CallSuper
    override fun createIntent(context: Context, input: Uri): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, input)
    }

    override fun getSynchronousResult(context: Context, input: Uri): SynchronousResult<Boolean>? = null

    @Suppress("AutoBoxing")
    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}

class TakePhotoResultLauncherHolder {
    var launcher: ActivityResultLauncher<Uri>? = null

    fun launch(input: Uri?, options: ActivityOptionsCompat?) {
        launcher?.launch(input, options) ?: error("Launcher has not been initialized")
    }

    fun unregister() {
        launcher?.unregister() ?: error("Launcher has not been initialized")
    }
}

class ManagedTakePhotoResultLauncher internal constructor(
    private val launcher: TakePhotoResultLauncherHolder,
    private val contract: State<ActivityResultContract<Uri, Boolean>>
) : ActivityResultLauncher<Uri>() {
    lateinit var inputUri: Uri

    override fun unregister() {
        throw UnsupportedOperationException(
            "Registration is automatically handled by rememberLauncherForActivityResult"
        )
    }

    override fun launch(input: Uri, options: ActivityOptionsCompat?) {
        inputUri = input
        launcher.launch(input, options)
    }

    override fun getContract(): ActivityResultContract<Uri, *> = contract.value
}