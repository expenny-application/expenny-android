package org.expenny.core.ui.data.navargs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.expenny.core.common.types.RecordType

@Parcelize
data class RecordsListFilterNavArg(
    val accounts: List<Long> = emptyList(),
    val categories: List<Long> = emptyList(),
    val labels: List<Long> = emptyList(),
    val types: List<RecordType> = emptyList(),
) : Parcelable
