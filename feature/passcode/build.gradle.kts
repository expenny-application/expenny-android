@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.google.devtools.ksp") version libs.versions.ksp
    id("expenny.android.feature")
    id("expenny.android.library.compose")
}

android {
    namespace = "org.expenny.feature.passcode"

    ksp {
        arg("compose-destinations.mode", "destinations")
        arg("compose-destinations.moduleName", "passcode")
    }

    libraryVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.threeten)
    implementation(libs.orbit.core)
    implementation(libs.orbit.compose)

    implementation(libs.destinations.core)
    ksp(libs.destinations.ksp)
}