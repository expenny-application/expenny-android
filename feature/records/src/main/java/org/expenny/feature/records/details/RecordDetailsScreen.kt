package org.expenny.feature.records.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.navargs.NavArgResult
import org.expenny.core.ui.data.navargs.StringArrayNavArg
import org.expenny.core.ui.utils.ManagedTakePhotoResultLauncher
import org.expenny.core.ui.utils.TakePhoto
import org.expenny.core.ui.utils.rememberLauncherForTakePhotoResult
import org.expenny.feature.records.details.contract.RecordDetailsAction
import org.expenny.feature.records.details.contract.RecordDetailsEvent
import org.expenny.feature.records.details.navigation.RecordDetailsNavArgs
import org.expenny.feature.records.details.navigation.RecordDetailsNavigator
import org.expenny.feature.records.details.view.RecordDetailsContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = RecordDetailsNavArgs::class)
@Composable
fun RecordDetailsScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: RecordDetailsNavigator,
    labelResult: OpenResultRecipient<StringArrayNavArg>,
    accountResult: OpenResultRecipient<NavArgResult>,
    categoryResult: OpenResultRecipient<LongNavArg>,
) {
    val vm: RecordDetailsViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val amountInputFocusRequester = remember { FocusRequester() }

    val pickImageLauncher = rememberGalleryLauncher(
        onSuccess = { vm.onAction(RecordDetailsAction.OnReceiptSelect(it)) }
    )

    val takePhotoLauncher = rememberCameraLauncher(
        onSuccess = { vm.onAction(RecordDetailsAction.OnReceiptCapture(it)) }
    )

    labelResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(RecordDetailsAction.OnLabelSelect(res.value))
        }
    }

    accountResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(RecordDetailsAction.OnAccountSelect(res.value as LongNavArg))
        }
    }

    categoryResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(RecordDetailsAction.OnCategorySelect(res.value))
        }
    }

    vm.collectSideEffect {
        when (it) {
            is RecordDetailsEvent.ShowMessage -> {
                // set minActiveState to CREATED
                snackbarManager.showInfo(it.message)
            }
            is RecordDetailsEvent.OpenImageViewer -> {
                launchImageViewer(context, it.uri)
            }
            is RecordDetailsEvent.OpenImagePicker -> {
                pickImageLauncher.launch(PickVisualMediaRequest(ImageOnly))
            }
            is RecordDetailsEvent.OpenCamera -> {
                takePhotoLauncher.launch(it.uri)
            }
            is RecordDetailsEvent.NavigateBack -> {
                navigator.navigateBack()
            }
            is RecordDetailsEvent.NavigateToCategorySelectionList -> {
                navigator.navigateToCategorySelectionListScreen(it.selection)
            }
            is RecordDetailsEvent.NavigateToAccountSelectionList -> {
                navigator.navigateToAccountSelectionListScreen(it.selection, it.excludeIds)
            }
            is RecordDetailsEvent.NavigateToLabelsSelectionList -> {
                navigator.navigateToRecordLabelsListScreen(it.selection)
            }
            is RecordDetailsEvent.RequestAmountInputFocus -> {
                amountInputFocusRequester.requestFocus()
            }
        }
    }

    RecordDetailsContent(
        state = state,
        scrollState = scrollState,
        amountInputFocusRequester = amountInputFocusRequester,
        onAction = vm::onAction
    )
}

private fun launchImageViewer(context: Context, uri: Uri) {
    val intent = Intent().apply {
        setDataAndType(uri, "image/*")
        action = Intent.ACTION_VIEW
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    ContextCompat.startActivity(context, intent, null)
}

@Composable
private fun rememberCameraLauncher(
    onSuccess: (Uri) -> Unit,
    onDismiss: () -> Unit = {}
): ManagedTakePhotoResultLauncher {
    return rememberLauncherForTakePhotoResult(
        contract = TakePhoto(),
        onResult = { uri ->
            if (uri != null) onSuccess(uri) else onDismiss()
        }
    )
}

@Composable
private fun rememberGalleryLauncher(
    onSuccess: (Uri) -> Unit,
    onDismiss: () -> Unit = {}
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) onSuccess(uri) else onDismiss()
        }
    )
}
