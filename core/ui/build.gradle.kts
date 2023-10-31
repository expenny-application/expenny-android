@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("expenny.android.library")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("expenny.android.library.compose")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "org.expenny.core.ui"
}

dependencies {
    api(project(":core:resources"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    api(libs.vico.compose.m3)
    api(libs.coil.kt.compose)
    api(libs.kotlinx.collections.immutable)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.metrics)

    debugApi(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.wheelPicker)
    implementation(libs.threeten)
    implementation(libs.destinations.core)
    implementation(libs.hilt.android)
    implementation(libs.joda.money)
}