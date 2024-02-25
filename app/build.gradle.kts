@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("expenny.android.application")
    id("expenny.android.application.compose")
    id("expenny.android.application.firebase")
    id("expenny.android.hilt")
}

android {
    defaultConfig {
        applicationId = "org.expenny"
        versionCode = 33
        versionName = "0.0.33"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        resourceConfigurations += listOf("en", "be", "ru")
    }

    signingConfigs {
        getByName("debug") {
            storeFile = project.file("expenny-debug-store.jks")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        create("release") {
            storeFile = project.file("expenny-release-store.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
            manifestPlaceholders += mapOf(
                "appName" to "Expenny Debug",
                "crashlyticsCollectionEnabled" to "false",
                "performanceCollectionEnabled" to "false",
                "analyticsCollectionEnabled" to "false",
            )
            signingConfig = signingConfigs.getByName("debug")
        }

        release {
            isDebuggable = false
            isMinifyEnabled = true
            applicationIdSuffix = ".release"
            manifestPlaceholders += mapOf(
                "appName" to "Expenny",
                "crashlyticsCollectionEnabled" to "true",
                "performanceCollectionEnabled" to "true",
                "analyticsCollectionEnabled" to "true",
            )
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            signingConfig = signingConfigs.getByName("release")
        }
    }

    kapt {
        correctErrorTypes = true
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    namespace = "org.expenny"
}

task("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}

task("printVersionCode") {
    doLast {
        println(android.defaultConfig.versionCode)
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
    implementation(project(":core:datastore"))
    implementation(project(":core:database"))
    implementation(project(":core:domain"))
    implementation(project(":core:model"))

    implementation(project(":feature:splash"))
    implementation(project(":feature:welcome"))
    implementation(project(":feature:profilesetup"))
    implementation(project(":feature:currencyunits"))
    implementation(project(":feature:dashboard"))
    implementation(project(":feature:accounts"))
    implementation(project(":feature:accountdetails"))
    implementation(project(":feature:currencies"))
    implementation(project(":feature:currencydetails"))
    implementation(project(":feature:records"))
    implementation(project(":feature:recorddetails"))
    implementation(project(":feature:categories"))
    implementation(project(":feature:categorydetails"))
    implementation(project(":feature:daterangepicker"))
    implementation(project(":feature:analytics"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:accountoverview"))
    implementation(project(":feature:passcode"))

    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.coil.kt)
    implementation(libs.destinations.core)
    implementation(libs.threeten)
    implementation(libs.gson)
    implementation(libs.timber.core)
    implementation(libs.hilt.android)
    implementation(libs.hilt.ext.work)
    kapt(libs.hilt.ext.compiler)

    debugImplementation(libs.leakcanary.android)

    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.espresso.core)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
}

// androidx.test is forcing JUnit, 4.12. This forces it to use 4.13
configurations.configureEach {
    resolutionStrategy {
        force(libs.junit4)
        // Temporary workaround for https://issuetracker.google.com/174733673
        force("org.objenesis:objenesis:2.6")
    }
}
