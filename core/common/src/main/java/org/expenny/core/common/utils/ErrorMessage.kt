package org.expenny.core.common.utils

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import org.expenny.core.common.utils.StringResource.Companion.fromRes
import org.expenny.core.resources.R

class ErrorMessage(throwable: Throwable) {

    val text: StringResource = when (throwable) {
        is SQLiteConstraintException -> {
            if (throwable.message.orEmpty().startsWith("UNIQUE")) {
                fromRes(R.string.duplicate_data_error)
            } else {
                fromRes(R.string.database_error)
            }
        }
        is SQLiteException -> fromRes(R.string.database_error)
        is IllegalArgumentException -> fromRes(R.string.invalid_argument_error)
        is IllegalStateException -> fromRes(R.string.init_error)
        is NullPointerException -> fromRes(R.string.data_not_exists_error)
        else -> fromRes(R.string.internal_error)
    }

}