package org.expenny.core.ui.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment.*
import android.widget.Toast
import androidx.annotation.StringRes
import java.util.*


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


@SuppressLint("DiscouragedApi")
fun Context.getDrawableResId(name: String): Int? {
    val id = resources.getIdentifier(name, "drawable", packageName)
    return if (id == 0) null else id
}

