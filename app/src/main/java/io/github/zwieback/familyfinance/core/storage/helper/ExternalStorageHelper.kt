package io.github.zwieback.familyfinance.core.storage.helper

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import java.io.File

object ExternalStorageHelper {

    @JvmStatic
    val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

    @JvmStatic
    val isExternalStorageReadable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
                    || Environment.MEDIA_MOUNTED_READ_ONLY == state
        }

    @JvmStatic
    fun getExternalDbFile(
        context: Context,
        databaseName: String,
        backupPath: String?
    ): File? {
        return getExternalFile(context, databaseName, backupPath)
    }

    @JvmStatic
    fun getExternalSharedPrefsFile(
        context: Context,
        sharedPrefsName: String,
        backupPath: String?
    ): File? {
        return getExternalFile(context, sharedPrefsName, backupPath)
    }

    private fun getExternalFile(
        context: Context,
        sharedPrefsName: String,
        backupPath: String?
    ): File? {
        return if (!backupPath.isNullOrEmpty()) {
            getNullableExternalFile(backupPath, sharedPrefsName)
        } else {
            getDefaultExternalFile(context, sharedPrefsName)
        }
    }

    private fun getDefaultExternalFile(context: Context, fileName: String): File? {
        val externalDirs = ContextCompat.getExternalFilesDirs(context, null)
        val externalDir = externalDirs.firstOrNull()
        return externalDir?.let {
            getNullableExternalFile(externalDir.absolutePath, fileName)
        }
    }

    private fun getNullableExternalFile(backupPath: String, fileName: String): File? {
        val externalFile = File(backupPath, fileName)
        if (externalFile.exists()) {
            return externalFile
        }
        return if (externalFile.createNewFile()) {
            externalFile
        } else {
            null
        }
    }
}
