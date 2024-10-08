package org.expenny.core.common.models

import android.os.Parcelable
import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
sealed class StringResource : Parcelable {
    companion object {
        fun fromStr(str: String): StringResource = SimpleStringResource(str)
        fun fromRes(@StringRes id: Int, vararg args: Any): StringResource = IdStringResource(id, *args)
        fun fromArrayRes(@ArrayRes id: Int, index: Int): StringResource = IdArrayStringResource(id, index)
        fun fromPluralRes(@PluralsRes id: Int, plural: Int): StringResource = IdQuantityStringResource(id, plural)
        fun empty(): StringResource = SimpleStringResource("")
    }

    class SimpleStringResource(val str: String) : StringResource()

    class IdStringResource(@StringRes val id: Int, vararg val args: @RawValue Any) : StringResource()

    class IdArrayStringResource(@ArrayRes val id: Int, val index: Int) : StringResource()

    class IdQuantityStringResource(@PluralsRes val pluralId: Int, val quantity: Int) : StringResource()
}