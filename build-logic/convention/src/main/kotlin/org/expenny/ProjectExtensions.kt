package org.expenny

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.library(key: String) = libs.findLibrary(key).get()

internal fun Project.version(key: String) = libs.findVersion(key).get().requiredVersion