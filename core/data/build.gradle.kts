@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("expenny.android.library")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("expenny.android.hilt")
}

android {
    namespace = "org.expenny.core.data"
}

dependencies {
    implementation(project(":core:resources"))
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))

    // Workaround for https://github.com/firebase/firebase-android-sdk/issues/5467
    // implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

    implementation(libs.androidx.biometric)
    implementation(libs.androidx.core.ktx)
    implementation(libs.threeten)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.kotlin.reflect)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.work)
    implementation(libs.hilt.ext.work)
    kapt(libs.hilt.ext.compiler)
}