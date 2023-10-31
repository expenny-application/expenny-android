package org.expenny.core.common.extensions

import org.expenny.core.common.utils.StringResource

fun String.toStringResource() = StringResource.fromStr(this)

fun Int.toStringResource(vararg args: Any) = StringResource.fromRes(this, args)