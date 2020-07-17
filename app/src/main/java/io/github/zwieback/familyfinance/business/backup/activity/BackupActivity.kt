package io.github.zwieback.familyfinance.business.backup.activity

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.backup.helper.BackupHelper
import io.github.zwieback.familyfinance.core.activity.ActivityWrapper
import io.github.zwieback.familyfinance.core.preference.config.BackupPrefs
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs
import io.github.zwieback.familyfinance.core.storage.helper.ExternalStorageHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import permissions.dispatcher.*

@RuntimePermissions
class BackupActivity : ActivityWrapper() {

    private lateinit var backupPrefs: BackupPrefs
    private lateinit var databasePrefs: DatabasePrefs

    override val titleStringId: Int
        get() = R.string.backup_activity_title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(this)
    }

    override fun setupContentView() {
        setContentView(R.layout.activity_backup)
    }

    private fun init(context: Context) {
        runBlocking(Dispatchers.IO) {
            databasePrefs = DatabasePrefs.with(context)
            backupPrefs = BackupPrefs.with(context)
        }

        findViewById<View>(R.id.backup_database_button).setOnClickListener {
            onBackupDatabaseClickWithPermissionCheck()
        }
        findViewById<View>(R.id.restore_database_button).setOnClickListener {
            onRestoreDatabaseClickWithPermissionCheck()
        }
        findViewById<View>(R.id.backup_shared_prefs_button).setOnClickListener {
            onBackupSharedPrefsClickWithPermissionCheck()
        }
        findViewById<View>(R.id.restore_shared_prefs_button).setOnClickListener {
            onRestoreSharedPrefsClickWithPermissionCheck()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        this.onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onBackupDatabaseClick() {
        if (!ExternalStorageHelper.isExternalStorageWritable) {
            showWriteExternalStoragePermissionIsMissingMsg()
            return
        }
        val backupPath = getBackupPath()
        val backupCompletedSuccessfully = runBlocking(Dispatchers.IO) {
            BackupHelper.backupDatabase(this@BackupActivity, backupPath)
        }
        val resultMessage = getBackupResultMessage(backupCompletedSuccessfully)
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onRestoreDatabaseClick() {
        if (!ExternalStorageHelper.isExternalStorageReadable) {
            showReadExternalStoragePermissionIsMissingMsg()
            return
        }
        AlertDialog.Builder(this)
            .setMessage(R.string.restore_rewrite_current_db)
            .setPositiveButton(android.R.string.yes) { _, _ -> restoreDatabase() }
            .setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun restoreDatabase() {
        val backupPath = getBackupPath()
        val restoreCompletedSuccessfully = runBlocking(Dispatchers.IO) {
            BackupHelper.restoreDatabase(this@BackupActivity, backupPath)
        }
        // todo: refresh database after a successful restore
        val resultMessage = getRestoreResultMessage(restoreCompletedSuccessfully)
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onBackupSharedPrefsClick() {
        if (!ExternalStorageHelper.isExternalStorageWritable) {
            showWriteExternalStoragePermissionIsMissingMsg()
            return
        }
        val backupPath = getBackupPath()
        var backupCompletedSuccessfully = runBlocking(Dispatchers.IO) {
            BackupHelper.backupSharedPreferences(
                this@BackupActivity,
                BACKUP_SHARED_PREFERENCES_NAME,
                backupPath
            )
        }
        backupCompletedSuccessfully = backupCompletedSuccessfully or runBlocking(Dispatchers.IO) {
            BackupHelper.backupSharedPreferences(
                this@BackupActivity,
                DATABASE_SHARED_PREFERENCES_NAME,
                backupPath
            )
        }
        backupCompletedSuccessfully = backupCompletedSuccessfully or runBlocking(Dispatchers.IO) {
            BackupHelper.backupSharedPreferences(
                this@BackupActivity,
                getDefaultSharedPreferencesName(this@BackupActivity),
                backupPath
            )
        }
        val resultMessage = getBackupResultMessage(backupCompletedSuccessfully)
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onRestoreSharedPrefsClick() {
        if (!ExternalStorageHelper.isExternalStorageReadable) {
            showReadExternalStoragePermissionIsMissingMsg()
            return
        }
        AlertDialog.Builder(this)
            .setMessage(R.string.restore_rewrite_current_prefs)
            .setPositiveButton(android.R.string.yes) { _, _ -> restoreSharedPrefs() }
            .setNegativeButton(android.R.string.no) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun restoreSharedPrefs() {
        val backupPath = getBackupPath()
        var restoreCompletedSuccessfully = runBlocking(Dispatchers.IO) {
            BackupHelper.restoreSharedPreferences(
                this@BackupActivity,
                BACKUP_SHARED_PREFERENCES_NAME,
                backupPath
            )
        }
        restoreCompletedSuccessfully = restoreCompletedSuccessfully or runBlocking(Dispatchers.IO) {
            BackupHelper.restoreSharedPreferences(
                this@BackupActivity,
                DATABASE_SHARED_PREFERENCES_NAME,
                backupPath
            )
        }
        restoreCompletedSuccessfully = restoreCompletedSuccessfully or runBlocking(Dispatchers.IO) {
            BackupHelper.restoreSharedPreferences(
                this@BackupActivity,
                getDefaultSharedPreferencesName(this@BackupActivity),
                backupPath
            )
        }
        // todo refresh shared preferences after a successful restore
        val resultMessage = getRestoreResultMessage(restoreCompletedSuccessfully)
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun showRationaleForWriteExternalStorage(request: PermissionRequest) {
        AlertDialog.Builder(this)
            .setMessage(R.string.permission_write_external_storage_rationale)
            .setPositiveButton(R.string.button_allow) { _, _ -> request.proceed() }
            .setNegativeButton(R.string.button_deny) { _, _ -> request.cancel() }
            .show()
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun showDeniedForWriteExternalStorage() {
        Toast
            .makeText(this, R.string.permission_write_external_storage_denied, Toast.LENGTH_LONG)
            .show()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    internal fun showNeverAskForWriteExternalStorage() {
        Toast
            .makeText(this, R.string.permission_write_external_storage_never_ask, Toast.LENGTH_LONG)
            .show()
    }

    private fun showReadExternalStoragePermissionIsMissingMsg() {
        Toast
            .makeText(
                this,
                R.string.read_external_storage_permission_is_missing,
                Toast.LENGTH_LONG
            )
            .show()
    }

    private fun showWriteExternalStoragePermissionIsMissingMsg() {
        Toast
            .makeText(
                this,
                R.string.write_external_storage_permission_is_missing,
                Toast.LENGTH_LONG
            )
            .show()
    }

    @StringRes
    private fun getBackupResultMessage(backupCompletedSuccessfully: Boolean): Int {
        return if (backupCompletedSuccessfully) {
            R.string.backup_completed_successfully
        } else {
            R.string.backup_failed
        }
    }

    @StringRes
    private fun getRestoreResultMessage(restoreCompletedSuccessfully: Boolean): Int {
        return if (restoreCompletedSuccessfully) {
            R.string.restore_completed_successfully
        } else {
            R.string.restore_failed
        }
    }

    private fun getBackupPath(): String = runBlocking(Dispatchers.IO) {
        backupPrefs.backupPath
    }

    companion object {
        private const val XML_EXTENSION = ".xml"
        private const val BACKUP_SHARED_PREFERENCES_NAME = BackupPrefs.FILE_NAME + XML_EXTENSION
        private const val DATABASE_SHARED_PREFERENCES_NAME = DatabasePrefs.FILE_NAME + XML_EXTENSION

        private fun getDefaultSharedPreferencesName(context: Context): String {
            return "${context.packageName}_preferences$XML_EXTENSION"
        }
    }
}
