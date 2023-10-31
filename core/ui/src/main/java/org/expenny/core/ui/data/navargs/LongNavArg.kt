package org.expenny.core.ui.data.navargs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.expenny.core.common.utils.Constants.DEFAULT_RESULT_CODE

@Parcelize
data class LongNavArg(
    val value: Long,
    override val resultCode: Int = DEFAULT_RESULT_CODE,
) : NavArgResult, Parcelable
