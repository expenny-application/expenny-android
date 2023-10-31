package org.expenny

/**
 * This is shared in :app module to provide configurations type safety.
 */
@Suppress("unused")
enum class ExpennyBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE
}