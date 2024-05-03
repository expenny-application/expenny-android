package org.expenny.feature.institution.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import org.expenny.core.resources.R
import org.expenny.core.ui.components.ExpennyLoadingContainer
import org.expenny.core.ui.foundation.primaryFixed
import org.expenny.feature.institution.contract.InstitutionRequisitionAction
import org.expenny.feature.institution.contract.InstitutionRequisitionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InstitutionRequisitionContent(
    state: InstitutionRequisitionState,
    onAction: (InstitutionRequisitionAction) -> Unit
) {
    var isWebViewReady by rememberSaveable { mutableStateOf(false) }
    var webViewTitle by rememberSaveable { mutableStateOf<String?>(null) }

    val onWebViewPageReadinessChanged: (String?, Boolean) -> Unit = { title, isReady ->
        isWebViewReady = isReady
        webViewTitle = title
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                title = {
                    webViewTitle?.let {
                        Text(
                            text = it,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(InstitutionRequisitionAction.OnRequisitionAborted) }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars,
        containerColor = Color.White,
    ) { paddingValues ->
        Box {
            InstitutionRequisitionWebView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                url = state.url,
                redirectUrl = state.redirectUrl,
                onPageStarted = { onWebViewPageReadinessChanged(it, false) },
                onPageFinished = { onWebViewPageReadinessChanged(it, true) },
                onSuccess = { onAction(InstitutionRequisitionAction.OnRequisitionGranted) },
                onAbort = { onAction(InstitutionRequisitionAction.OnRequisitionAborted) }
            )
            ExpennyLoadingContainer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                isLoading = !isWebViewReady || state.isLoading,
                color = MaterialTheme.colorScheme.primaryFixed
            )
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun InstitutionRequisitionWebView(
    modifier: Modifier = Modifier,
    url: String?,
    redirectUrl: String?,
    onPageStarted: (title: String?) -> Unit,
    onPageFinished: (title: String?) -> Unit,
    onSuccess: () -> Unit,
    onAbort: () -> Unit,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        onPageStarted(view?.title)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        onPageFinished(view?.title)
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val requestUrl = request?.url.toString()
                        return if (requestUrl == redirectUrl) {
                            onSuccess()
                            true
                        } else if (requestUrl.contains("UserCancelledSession")) {
                            onAbort()
                            true
                        } else {
                            false
                        }
                    }
                }

                url?.let { loadUrl(it) }
            }
        },
        update = { currentWebView ->
            url?.let { currentWebView.loadUrl(it) }
        }
    )
}