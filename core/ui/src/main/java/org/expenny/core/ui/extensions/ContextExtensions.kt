package org.expenny.core.ui.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(
    message: CharSequence,
    duration: Int = Toast.LENGTH_SHORT,
    block: Toast.() -> Unit = {}
): Toast = Toast.makeText(this, message, duration)
    .apply {
        block(this)
        show()
    }

fun Context.toast(
    @StringRes messageRes: Int,
    duration: Int = Toast.LENGTH_SHORT,
    block: Toast.() -> Unit = {}
) = toast(getString(messageRes), duration, block)