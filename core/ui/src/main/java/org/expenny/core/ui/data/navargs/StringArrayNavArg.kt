package org.expenny.core.ui.data.navargs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.expenny.core.common.utils.Constants

@Suppress("ArrayInDataClass")
@Parcelize
data class StringArrayNavArg(
    val values: Array<String>,
    override val resultCode: Int = Constants.DEFAULT_RESULT_CODE,
) : NavArgResult, Parcelable