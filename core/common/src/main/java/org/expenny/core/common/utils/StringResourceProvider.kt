package org.expenny.core.common.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StringResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(stringResource: StringResource) = stringResource.asRawString(context.resources)
}