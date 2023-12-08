package org.expenny.core.ui.foundation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.foundation.*
import org.expenny.core.ui.theme.*
import org.expenny.core.ui.extensions.noRippleClickable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennyToolbar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        actions = {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                actions()
            }
        },
        navigationIcon = {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                navigationIcon()
            }
        },
        title = {
            ProvideTextStyle(value = MaterialTheme.typography.titleLarge) {
                title()
            }
        },
        windowInsets = windowInsets,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun ExpennySearchToolbar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSearch by rememberSaveable { mutableStateOf(false) }

    if (showSearch) {
        val searchFocusRequester = remember { FocusRequester() }

        DisposableEffect(Unit) {
            searchFocusRequester.requestFocus()
            onDispose {}
        }

        BackHandler(enabled = true) {
            showSearch = false
            onQueryChange("")
        }

        ExpennyToolbar(
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            navigationIcon = {
                IconButton(
                    onClick = {
                        showSearch = false
                        onQueryChange("")
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = null
                    )
                }
            },
            title = {
                SearchToolbarField(
                    query = searchQuery,
                    focusRequester = searchFocusRequester,
                    onQueryChange = onQueryChange,
                    onFocused = {
                        keyboardController?.show()
                    }
                )
            }
        )
    } else {
        ExpennyToolbar(
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            navigationIcon = navigationIcon,
            actions = {
                IconButton(
                    onClick = {
                        showSearch = true
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = null
                    )
                }

                actions()
            },
            title = title
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchToolbarField(
    query: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onFocused: () -> Unit,
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Transparent,
            )
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = 48.dp
            )
            .height(48.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    onFocused()
                }
            },
        value = query,
        onValueChange = onQueryChange,
        visualTransformation = VisualTransformation.None,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onQueryChange(query)
            },
        ),
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                isError = false,
                leadingIcon = null,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_label),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.noRippleClickable { onQueryChange("") },
                            painter = painterResource(R.drawable.ic_close),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentDescription = null,
                        )
                    }
                },
                shape = SearchBarDefaults.inputFieldShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
                    errorPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.outlineVariant,
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
                container = {},
            )
        }
    )
}
