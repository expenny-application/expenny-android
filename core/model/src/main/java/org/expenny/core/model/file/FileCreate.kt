package org.expenny.core.model.file

import android.net.Uri

data class FileCreate(
    val profileId: Long,
    val uri: Uri
)
