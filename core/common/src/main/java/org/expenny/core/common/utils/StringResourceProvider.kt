package org.expenny.core.common.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.expenny.core.common.extensions.asRawString
import org.expenny.core.common.models.StringResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StringResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(stringResource: StringResource) = stringResource.asRawString(context.resources)
}