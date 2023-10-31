package org.expenny.core.common.types

enum class ApplicationLanguage(val tag: String) {
    SystemDefault(tag = ""),
    English(tag = "en"),
    Belarusian(tag = "be"),
    Russian(tag = "ru");

    companion object {
        fun tagOf(tag: String): ApplicationLanguage? {
            return ApplicationLanguage.values().firstOrNull { it.tag == tag }
        }
    }
}