@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("expenny.android.library")
    id("expenny.android.hilt")
}

android {
    namespace = "org.expenny.core.datastore"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlin.reflect)
    implementation(libs.androidx.datastore.preferences)
}