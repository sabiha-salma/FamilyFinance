package io.github.zwieback.familyfinance.core.storage.helper

import android.content.Context
import java.io.File

object InternalStorageHelper {

    private const val UPPER_LEVEL_DIR_NAME = ".."
    private const val SHARED_PREFS_DIR_NAME = "shared_prefs"
    private const val SAMSUNG_SHARED_PREFS_ROOT_PATH = "/dbdata/databases/"

    @JvmStatic
    fun getInternalDbFile(context: Context, databaseName: String): File {
        return context.getDatabasePath(databaseName)
    }

    @JvmStatic
    fun getInternalSharedPrefsFile(
        context: Context,
        sharedPrefsName: String
    ): File? {
        val sharedPrefsFile = getInternalSharedPrefsFile(context, sharedPrefsName) {
            buildRegularSharedPrefsDir(it)
        }
        return sharedPrefsFile ?: getInternalSharedPrefsFileFromSamsung(context, sharedPrefsName)
    }

    /**
     * Workaround to get the file of shared preferences from Samsung devices.
     *
     * See [Copy the shared preferences XML file from /data on Samsung device failed](https://stackoverflow.com/a/5533412/8035065)
     */
    private fun getInternalSharedPrefsFileFromSamsung(
        context: Context,
        sharedPrefsName: String
    ): File? {
        return getInternalSharedPrefsFile(context, sharedPrefsName) {
            buildSamsungSharedPrefsDir(it)
        }
    }

    private fun getInternalSharedPrefsFile(
        context: Context,
        sharedPrefsName: String,
        sharedPrefsDirBuildFunction: (context: Context) -> File
    ): File? {
        val sharedPrefsDir = sharedPrefsDirBuildFunction(context)
        val sharedPrefsFile = File(sharedPrefsDir, sharedPrefsName)
        return if (sharedPrefsFile.exists()) {
            sharedPrefsFile
        } else {
            null
        }
    }

    private fun buildRegularSharedPrefsDir(context: Context): File {
        return File(
            context.filesDir,
            UPPER_LEVEL_DIR_NAME + File.separator + SHARED_PREFS_DIR_NAME
        )
    }

    private fun buildSamsungSharedPrefsDir(context: Context): File {
        return File(
            SAMSUNG_SHARED_PREFS_ROOT_PATH
                    + context.packageName
                    + File.separator
                    + SHARED_PREFS_DIR_NAME
        )
    }
}
