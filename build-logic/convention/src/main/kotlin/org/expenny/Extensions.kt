package org.expenny

import com.android.build.api.dsl.DefaultConfig
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import java.util.Properties

val Project.libs get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.library(key: String) = libs.findLibrary(key).get()

internal fun Project.version(key: String) = libs.findVersion(key).get().requiredVersion

fun Properties.getStringPropertyOrEmpty(key: String) =
    takeIf { containsKey(key) }?.let { this[key].toString() } ?: ""

fun DefaultConfig.addBuildConfigField(key: String) {
    val property = System.getenv(key) ?: loadProperties("local.properties")?.getStringPropertyOrEmpty(key) ?: ""
    buildConfigField("String", key, "\"${property}\"")
}