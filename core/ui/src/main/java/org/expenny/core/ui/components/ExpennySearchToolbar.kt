package org.expenny.core.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.expenny.core.resources.R
import org.expenny.core.ui.extensions.noRippleClickable
import org.expenny.core.ui.foundation.transparent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpennySearchToolbar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    searchQuery: String,
    isSearchShown: Boolean = false,
    onQueryChange: (String) -> Unit,
    title: @Composable ExpennyToolbarScope.() -> Unit = {},
    actions: @Composable ExpennyToolbarScope.() -> Unit = {},
    navigationIcon: @Composable ExpennyToolbarScope.() -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSearch by rememberSaveable(isSearchShown) { mutableStateOf(isSearchShown) }

    if (showSearch) {
        val searchFocusRequester = remember { FocusRequester() }

        DisposableEffect(Unit) {
            searchFocusRequester.requestFocus()
            onDispose {
                showSearch = false
                onQueryChange("")
            }
        }

        BackHandler(enabled = true) {
            showSearch = false
            onQueryChange("")
        }

        ExpennyToolbar(
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            title = {
                SearchToolbarInput(
                    query = searchQuery,
                    focusRequester = searchFocusRequester,
                    onQueryChange = onQueryChange,
                    onFocused = {
                        keyboardController?.show()
                    }
                )
            },
            navigationIcon = {
                ToolbarIcon(
                    painter = painterResource(R.drawable.ic_back),
                    onClick = {
                        showSearch = false
                        onQueryChange("")
                    }
                )
            }
        )
    } else {
        ExpennyToolbar(
            modifier = modifier,
            scrollBehavior = scrollBehavior,
            title = title,
            navigationIcon = navigationIcon,
            actions = {
                ToolbarIcon(
                    painter = painterResource(R.drawable.ic_search),
                    onClick = {
                        showSearch = true
                    }
                )
                actions(this)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchToolbarInput(
    modifier: Modifier = Modifier,
    query: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit,
    onFocused: () -> Unit,
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.transparent,
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
            TextFieldDefaults.DecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                isError = false,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_label),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                },
                leadingIcon = null,
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.noRippleClickable {
                                onQueryChange("")
                            },
                            painter = painterResource(R.drawable.ic_close),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentDescription = null,
                        )
                    }
                },
                shape = SearchBarDefaults.inputFieldShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.transparent,
                    errorContainerColor = MaterialTheme.colorScheme.transparent,
                    disabledContainerColor = MaterialTheme.colorScheme.transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.transparent,
                    disabledIndicatorColor = MaterialTheme.colorScheme.transparent,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    errorTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    errorPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                contentPadding = PaddingValues(horizontal = 16.dp),
                container = {},
            )
        }
    )
}
