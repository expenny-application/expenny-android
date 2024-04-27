package org.expenny.feature.recorddetails

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import org.expenny.core.ui.base.ExpennySnackbarManager
import org.expenny.core.ui.data.navargs.LongNavArg
import org.expenny.core.ui.data.navargs.NavArgResult
import org.expenny.core.ui.foundation.ExpennyTheme
import org.expenny.core.ui.utils.ManagedTakePhotoResultLauncher
import org.expenny.core.ui.utils.TakePhoto
import org.expenny.core.ui.utils.rememberLauncherForTakePhotoResult
import org.expenny.feature.recorddetails.navigation.RecordDetailsNavArgs
import org.expenny.feature.recorddetails.navigation.RecordDetailsNavigator
import org.expenny.feature.recorddetails.view.RecordDetailsContent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination(navArgsDelegate = RecordDetailsNavArgs::class)
@Composable
fun RecordDetailsScreen(
    snackbarManager: ExpennySnackbarManager,
    navigator: RecordDetailsNavigator,
    accountResult: OpenResultRecipient<NavArgResult>,
    categoryResult: OpenResultRecipient<LongNavArg>,
) {
    val vm: RecordDetailsViewModel = hiltViewModel()
    val state by vm.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val amountInputFocusRequester = remember { FocusRequester() }

    val pickImageLauncher = rememberGalleryLauncher(
        onSuccess = { vm.onAction(Action.OnReceiptSelect(it)) }
    )

    val takePhotoLauncher = rememberCameraLauncher(
        onSuccess = { vm.onAction(Action.OnReceiptCapture(it)) }
    )

    accountResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(Action.OnAccountSelect(res.value as LongNavArg))
        }
    }

    categoryResult.onNavResult { res ->
        if (res is NavResult.Value) {
            vm.onAction(Action.OnCategorySelect(res.value))
        }
    }

    vm.collectSideEffect {
        when (it) {
            is Event.ShowMessage -> {
                // set minActiveState to CREATED
                snackbarManager.showInfo(it.message)
            }
            is Event.OpenImageViewer -> {
                launchImageViewer(context, it.uri)
            }
            is Event.OpenImagePicker -> {
                pickImageLauncher.launch(PickVisualMediaRequest(ImageOnly))
            }
            is Event.OpenCamera -> {
                takePhotoLauncher.launch(it.uri)
            }
            is Event.NavigateBack -> {
                navigator.navigateBack()
            }
            is Event.NavigateToCategorySelectionList -> {
                navigator.navigateToCategorySelectionListScreen(it.selection)
            }
            is Event.NavigateToAccountSelectionList -> {
                navigator.navigateToAccountSelectionListScreen(it.selection, it.excludeIds)
            }
            is Event.RequestAmountInputFocus -> {
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

@Preview
@Composable
private fun RecordDetailsContentPreview() {
    ExpennyTheme {
        RecordDetailsContent(
            state = State(),
            scrollState = rememberScrollState(),
            amountInputFocusRequester = remember { FocusRequester() },
            onAction = {}
        )
    }
}