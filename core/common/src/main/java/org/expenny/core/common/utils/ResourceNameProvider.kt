package org.expenny.core.common.utils

import android.content.Context
import android.content.res.Resources
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceNameProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    operator fun invoke(id: Int): String? {
        return try {
            context.resources.getResourceEntryName(id)
        } catch (e: Resources.NotFoundException) {
            null
        }
    }
}