package org.expenny.core.common.utils

import android.annotation.SuppressLint
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceIdProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    @SuppressLint("DiscouragedApi")
    operator fun invoke(name: String, resourceType: String = "drawable"): Int? {
        return try {
            val id = context.resources.getIdentifier(name, resourceType, context.packageName)
            if (id == 0) null else id
        } catch (e: RuntimeException) {
            null
        }
    }
}
