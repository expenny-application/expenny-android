import org.expenny.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("expenny.android.library")
                apply("expenny.android.hilt")
            }

            dependencies {
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:common"))
                add("implementation", project(":core:model"))
                add("implementation", project(":core:domain"))

                // TODO move to core:testing module when available
                add("androidTestImplementation", libs.findLibrary("hilt.android.testing").get())
                add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
                add("testImplementation", libs.findLibrary("junit4").get())
                add("androidTestImplementation", libs.findLibrary("androidx.junit").get())
                add("androidTestImplementation", libs.findLibrary("androidx.junit.ktx").get())
                add("androidTestImplementation", libs.findLibrary("androidx.compose.ui.test").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.ext").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.core").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.espresso.core").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.rules").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.runner").get())

                add("testImplementation", kotlin("test"))
                add("androidTestImplementation", kotlin("test"))

                add("implementation", libs.findLibrary("coil.kt").get())
                add("implementation", libs.findLibrary("coil.kt.compose").get())

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())

                add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }
}