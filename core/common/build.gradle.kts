plugins {
    id("expenny.android.library")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("expenny.android.hilt")
}

android {
    namespace = "org.expenny.core.common"
}

dependencies {
    implementation(project(":core:resources"))

    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.destinations.core)
    implementation(libs.threeten)
    implementation(libs.timber.core)
    implementation(libs.kotlinx.coroutines.core)
}