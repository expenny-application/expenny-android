package org.expenny.core.common.extensions

fun <T> List<T>.sumByFloat(selector: (T) -> Float): Float {
    var sum = 0f
    for (element in this) sum += selector(element)
    return sum
}