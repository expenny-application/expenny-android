package org.expenny.core.ui.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@SuppressLint("DiscouragedApi")
@Composable
fun getResourceIdByName(name: String, type: String): Int {
    val context = LocalContext.current
    return remember(name) {
        context.resources.getIdentifier(name, type, context.packageName)
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun getDrawableIdByName(name: String): Int {
    val context = LocalContext.current
    return remember(name) {
        context.resources.getIdentifier(name, "drawable", context.packageName)
    }
}