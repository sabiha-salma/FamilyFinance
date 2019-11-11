package io.github.zwieback.familyfinance.business.backup.helper

import android.content.Context
import io.github.zwieback.familyfinance.core.model.Models
import io.github.zwieback.familyfinance.core.storage.helper.ExternalStorageHelper.getExternalDbFile
import io.github.zwieback.familyfinance.core.storage.helper.ExternalStorageHelper.getExternalSharedPrefsFile
import io.github.zwieback.familyfinance.core.storage.helper.InternalStorageHelper.getInternalDbFile
import io.github.zwieback.familyfinance.core.storage.helper.InternalStorageHelper.getInternalSharedPrefsFile
import io.github.zwieback.familyfinance.util.FileUtils
import java.io.IOException

object BackupHelper {

    private val databaseName: String
        get() = Models.DEFAULT.name

    @Throws(IOException::class)
    fun backupDatabase(
        context: Context,
        backupPath: String?
    ): Boolean {
        val internalDb = getInternalDbFile(context, databaseName)
        val externalDb = getExternalDbFile(context, databaseName, backupPath) ?: return false
        FileUtils.copyFile(internalDb, externalDb)
        return externalDb.exists()
    }

    @Throws(IOException::class)
    fun backupSharedPreferences(
        context: Context,
        sharedPrefsName: String,
        backupPath: String?
    ): Boolean {
        val internalFile = getInternalSharedPrefsFile(context, sharedPrefsName) ?: return false
        val externalFile =
            getExternalSharedPrefsFile(context, sharedPrefsName, backupPath) ?: return false
        FileUtils.copyFile(internalFile, externalFile)
        return externalFile.exists()
    }

    @Throws(IOException::class)
    fun restoreDatabase(
        context: Context,
        backupPath: String?
    ): Boolean {
        val internalDb = getInternalDbFile(context, databaseName)
        val externalDb = getExternalDbFile(context, databaseName, backupPath) ?: return false
        FileUtils.copyFile(externalDb, internalDb)
        return internalDb.exists()
    }

    @Throws(IOException::class)
    fun restoreSharedPreferences(
        context: Context,
        sharedPrefsName: String,
        backupPath: String?
    ): Boolean {
        val internalFile = getInternalSharedPrefsFile(context, sharedPrefsName) ?: return false
        val externalFile =
            getExternalSharedPrefsFile(context, sharedPrefsName, backupPath) ?: return false
        FileUtils.copyFile(externalFile, internalFile)
        return internalFile.exists()
    }
}
