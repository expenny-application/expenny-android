@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("expenny.android.library")
    id("expenny.android.hilt")
    alias(libs.plugins.kotlin.serialization) version libs.versions.kotlin
}

android {
    namespace = "org.expenny.core.network"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.ktor.core)
    implementation(libs.ktor.android)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.logging)
    implementation(libs.kotlin.reflect)
    implementation(libs.timber.core)
}