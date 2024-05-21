package org.expenny

import java.io.FileInputStream
import java.util.Properties

fun loadProperties(path: String): Properties? {
    runCatching {
        FileInputStream(path).use { inputStream ->
            Properties().apply {
                load(inputStream)
            }
        }
    }.onSuccess { properties ->
        return properties
    }
    return null
}
