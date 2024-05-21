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
    implementation(project(":core:datastore"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.androidx.biometric)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.threeten)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
}