package org.expenny.core.common.utils

import android.content.ContentResolver.SCHEME_CONTENT
import android.content.ContentResolver.SCHEME_FILE
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExternalFilesDirectoryHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun copyFileToCacheDirectory(uri: Uri): Uri? {
        if (isExternalStorageWritable()) {
            return copyFileToDirectory(uri, context.externalCacheDir!!)?.let {
                getContentUriForFile(it)
            }
        }
        return null
    }

    fun copyFileToPersistentDirectory(uri: Uri): Uri? {
        if (isExternalStorageWritable()) {
            return copyFileToDirectory(uri, context.getExternalFilesDir(DIRECTORY_PICTURES)!!)?.let {
                getContentUriForFile(it)
            }
        }
        return null
    }

    fun moveCacheFileToPersistentDirectory(uri: Uri): Uri? {
        if (isExternalStorageWritable()) {
            val isFileAlreadyPersistent = uri.getContentName()?.let {
                File(context.getExternalFilesDir(DIRECTORY_PICTURES), it)
            }?.exists() ?: false

            if (!isFileAlreadyPersistent) {
                val outputFile = copyFileToDirectory(uri, context.getExternalFilesDir(DIRECTORY_PICTURES)!!)
                if (outputFile != null) {
                    deleteCacheFile(uri)
                    return getContentUriForFile(outputFile)
                }
            } else {
                return uri
            }
        }
        return null
    }

    fun deleteCacheFile(uri: Uri): Boolean {
        if (isExternalStorageWritable()) {
            uri.getContentName()?.let { fileName ->
                return File(context.externalCacheDir, fileName).delete()
            }
        }
        return false
    }

    fun deletePersistentFile(uri: Uri): Boolean {
        if (isExternalStorageWritable()) {
            uri.getContentName()?.let { fileName ->
                return File(context.getExternalFilesDir(DIRECTORY_PICTURES), fileName).delete()
            }
        }
        return false
    }

    fun getPersistentImageUri(): Uri {
        val imageFile = File(context.getExternalFilesDir(DIRECTORY_PICTURES), getNewImageFileName())
        return getContentUriForFile(imageFile)
    }

    fun getCacheImageUri(): Uri {
        val imageFile = File(context.externalCacheDir, getNewImageFileName())
        return getContentUriForFile(imageFile)
    }

    private fun copyFileToDirectory(uri: Uri, fileDirectory: File): File? {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                // val filename = uri.getContentName()
                val filename = getNewImageFileName()
                val outputFile = File(fileDirectory, filename)
                val outputStream = FileOutputStream(outputFile)
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable = inputStream.available()
                val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
                val buffer = ByteArray(bufferSize)
                while (inputStream.read(buffer) > 0) {
                    outputStream.write(buffer)
                }
                inputStream.close()
                outputStream.close()
                return outputFile
            }
        } catch (e: java.lang.Exception) {
            return null
        }
        return null
    }

    private fun Uri.getContentName(): String? {
        return when (scheme) {
            SCHEME_FILE -> toFile().takeIf { it.exists() }?.name
            SCHEME_CONTENT -> {
                context.contentResolver.query(this, null, null, null, null)?.let { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    val name = cursor.getString(nameIndex)
                    cursor.close()
                    return@let name
                }
            }
            else -> null
        }
    }

    private fun getContentUriForFile(file: File): Uri {
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    private fun getNewImageFileName(): String {
        return buildString {
            append(SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis()))
            append(".jpg")
        }
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}