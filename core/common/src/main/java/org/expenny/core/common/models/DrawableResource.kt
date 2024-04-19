package org.expenny.core.common.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrawableResource(@DrawableRes val id: Int) : Parcelable
