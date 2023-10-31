@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("expenny.android.library")
}

android {
    namespace = "org.expenny.core.model"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.threeten)
    implementation(libs.kotlin.reflect)
    implementation(libs.joda.money)
}