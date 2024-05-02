package org.expenny

import java.io.FileInputStream
import java.util.Properties

fun loadProperties(path: String): Properties =
    FileInputStream(path).use { inputStream ->
        Properties().apply {
            load(inputStream)
        }
    }