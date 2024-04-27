package org.expenny.core.ui.base

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum

class ExpennyLoremIpsum(private val  words: Int) {

    val text: String
        get() {
            return LoremIpsum(words).values.joinToString().replace("\n", " ")
        }
}