package org.expenny.core.common.extensions

import android.content.res.Resources
import org.expenny.core.common.models.StringResource

fun StringResource.asRawString(resources: Resources) : String = when (this) {
    is StringResource.SimpleStringResource -> this.str
    is StringResource.IdStringResource -> resources.getString(this.id, *this.args)
    is StringResource.IdArrayStringResource -> resources.getStringArray(this.id)[this.index]
    is StringResource.IdQuantityStringResource -> resources.getQuantityString(this.pluralId, this.quantity)
}