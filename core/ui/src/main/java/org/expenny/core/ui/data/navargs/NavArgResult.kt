package org.expenny.core.ui.data.navargs

import android.os.Parcelable

sealed interface NavArgResult : Parcelable {
    val resultCode: Int
}