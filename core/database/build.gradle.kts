@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("expenny.android.library")
    id("expenny.android.hilt")
    id("expenny.android.room")
}

android {
    namespace = "org.example.core.database"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.threeten)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlin.reflect)
    implementation(libs.timber.core)
}