package org.expenny.core.common.extensions

import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KMutableProperty1

fun <T> List<T>.groupedAlphabetically(ref: KMutableProperty1<T, String>): SortedMap<String, List<T>> {
    return groupBy { ref.get(it).first().toString() }.toSortedMap(compareBy { it })
}

fun <T> List<T>.sumByDecimal(selector: (T) -> BigDecimal): BigDecimal {
    var sum = BigDecimal(0)
    for (element in this) sum += selector(element)
    return sum
}

infix fun <T> List<T>.equalsIgnoreOrder(other: List<T>): Boolean {
    return this.size == other.size && this.toSet() == other.toSet()
}

fun <T> List<T>.addOrRemoveIfExist(item: T): MutableList<T> {
    return toMutableList().apply {
        if (contains(item)) remove(item) else add(item)
    }
}
