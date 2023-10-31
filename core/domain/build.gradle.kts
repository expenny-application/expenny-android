@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("expenny.android.library")
    kotlin("kapt")
}

android {
    namespace = "org.expenny.core.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:resources"))
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.threeten)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}