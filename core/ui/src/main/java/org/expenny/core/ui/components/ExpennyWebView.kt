package org.expenny.core.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.expenny.core.resources.R

@Composable
fun ExpennyWebView(
    modifier: Modifier = Modifier,
    state: ExpennyWebViewState,
    captureBackPresses: Boolean = true,
    navigator: ExpennyWebViewNavigator = rememberExpennyWebViewNavigator(),
    onCreated: (WebView) -> Unit = {},
    onDispose: (WebView) -> Unit = {},
    client: ExpennyWebViewClient = remember { ExpennyWebViewClient() },
    factory: ((Context) -> WebView)? = null,
) {
    BoxWithConstraints(modifier) {
        val width =
            if (constraints.hasFixedWidth) FrameLayout.LayoutParams.MATCH_PARENT
            else FrameLayout.LayoutParams.WRAP_CONTENT
        val height =
            if (constraints.hasFixedHeight) FrameLayout.LayoutParams.MATCH_PARENT
            else FrameLayout.LayoutParams.WRAP_CONTENT

        val layoutParams = FrameLayout.LayoutParams(width, height)

        ExpennyWebView(
            Modifier,
            state,
            layoutParams,
            captureBackPresses,
            navigator,
            onCreated,
            onDispose,
            client,
            factory
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ExpennyWebView(
    modifier: Modifier = Modifier,
    state: ExpennyWebViewState,
    layoutParams: FrameLayout.LayoutParams,
    captureBackPresses: Boolean = true,
    navigator: ExpennyWebViewNavigator = rememberExpennyWebViewNavigator(),
    onCreated: (WebView) -> Unit = {},
    onDispose: (WebView) -> Unit = {},
    client: ExpennyWebViewClient = remember { ExpennyWebViewClient() },
    factory: ((Context) -> WebView)? = null,
) {
    val webView = state.webView

    BackHandler(captureBackPresses && navigator.canGoBack) {
        webView?.goBack()
    }

    webView?.let { wv ->
        LaunchedEffect(wv, navigator) {
            with(navigator) {
                wv.handleNavigationEvents()
            }
        }
        LaunchedEffect(wv, state) {
            snapshotFlow { state.url }.collect { urlToLoad ->
                urlToLoad?.let { wv.loadUrl(it) }
            }
        }
    }

    // Set the state of the client
    // This is done internally to ensure they always are the same instance as the parent Web composable
    client.state = state
    client.navigator = navigator

    AndroidView(
        modifier = modifier,
        factory = { context ->
            (factory?.invoke(context) ?: WebView(context)).apply {
                onCreated(this)

                this.layoutParams = layoutParams
                this.settings.javaScriptEnabled = true

                state.viewState?.let {
                    this.restoreState(it)
                }

                webViewClient = client
            }.also { state.webView = it }
        },
        onRelease = {
            onDispose(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyWebViewTopBar(
    modifier: Modifier = Modifier,
    title: String?,
    onClose: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            title?.let {
                Text(
                    text = it,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null
                )
            }
        }
    )
}

class ExpennyWebViewClient : WebViewClient() {

    lateinit var state: ExpennyWebViewState
        internal set

    lateinit var navigator: ExpennyWebViewNavigator
        internal set

    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        state.isLoading = true
        state.errorsForCurrentRequest.clear()
        state.pageTitle = view.title
        state.lastLoadedUrl = url
    }

    override fun onPageFinished(view: WebView, url: String?) {
        state.isLoading = false
        state.pageTitle = view.title
    }

    override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
        super.doUpdateVisitedHistory(view, url, isReload)
        navigator.canGoBack = view.canGoBack()
        navigator.canGoForward = view.canGoForward()
    }

    override fun onReceivedError(view: WebView, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)
        if (error != null) {
            state.errorsForCurrentRequest.add(ExpennyWebViewError(request, error))
        }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return request?.url.toString().takeIf { it.isNotBlank() }?.let {
            state.shouldOverrideUrlLoading(it)
        } ?: false
    }
}

@Stable
class ExpennyWebViewState(
    url: String,
    shouldOverrideUrlLoading: (requestUrl: String) -> Boolean = { false }
) {
    var lastLoadedUrl: String? by mutableStateOf(null)
        internal set

    var url: String? by mutableStateOf(url)

    var shouldOverrideUrlLoading : (requestUrl: String) -> Boolean by mutableStateOf(shouldOverrideUrlLoading)

    var isLoading: Boolean = false
        internal set

    var pageTitle: String? by mutableStateOf(null)
        internal set

    val errorsForCurrentRequest: SnapshotStateList<ExpennyWebViewError> = mutableStateListOf()

    var viewState: Bundle? = null
        internal set

    internal var webView by mutableStateOf<WebView?>(null)
}

class ExpennyWebViewNavigator(private val coroutineScope: CoroutineScope) {
    private sealed interface NavigationEvent {
        data object Back : NavigationEvent
        data object Forward : NavigationEvent
        data object Reload : NavigationEvent
        data object StopLoading : NavigationEvent
        data class LoadUrl(val url: String) : NavigationEvent
    }

    private val navigationEvents: MutableSharedFlow<NavigationEvent> = MutableSharedFlow(replay = 1)

    // Use Dispatchers.Main to ensure that the WebView methods are called on UI thread
    internal suspend fun WebView.handleNavigationEvents(): Nothing = withContext(Dispatchers.Main) {
        navigationEvents.collect { event ->
            when (event) {
                is NavigationEvent.Back -> goBack()
                is NavigationEvent.Forward -> goForward()
                is NavigationEvent.Reload -> reload()
                is NavigationEvent.StopLoading -> stopLoading()
                is NavigationEvent.LoadUrl -> { loadUrl(event.url) }
            }
        }
    }
    var canGoBack: Boolean by mutableStateOf(false)
        internal set

    var canGoForward: Boolean by mutableStateOf(false)
        internal set

    fun loadUrl(url: String) {
        coroutineScope.launch {
            navigationEvents.emit(
                NavigationEvent.LoadUrl(url)
            )
        }
    }

    fun navigateBack() {
        coroutineScope.launch { navigationEvents.emit(NavigationEvent.Back) }
    }

    fun navigateForward() {
        coroutineScope.launch { navigationEvents.emit(NavigationEvent.Forward) }
    }

    fun reload() {
        coroutineScope.launch { navigationEvents.emit(NavigationEvent.Reload) }
    }

    fun stopLoading() {
        coroutineScope.launch { navigationEvents.emit(NavigationEvent.StopLoading) }
    }
}

@Immutable
data class ExpennyWebViewError(
    val request: WebResourceRequest?,
    val error: WebResourceError
)

@Composable
fun rememberExpennyWebViewNavigator(coroutineScope: CoroutineScope = rememberCoroutineScope()) =
    remember(coroutineScope) { ExpennyWebViewNavigator(coroutineScope) }

@Composable
fun rememberExpennyWebViewState(
    url: String,
    shouldOverrideUrlLoading: (requestUrl: String) -> Boolean = { false }
) = remember {
        ExpennyWebViewState(url, shouldOverrideUrlLoading)
    }.apply {
        this.url = url
        this.shouldOverrideUrlLoading = shouldOverrideUrlLoading
    }
