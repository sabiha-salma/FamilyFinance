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
import permissions.dispatcher.*

@RuntimePermissions
class BackupActivity : ActivityWrapper() {

    private lateinit var backupPrefs: BackupPrefs
    private lateinit var databasePrefs: DatabasePrefs

    override val titleStringId: Int
        get() = R.string.backup_activity_title

    private val backupSharedPreferencesName: String
        get() = BackupPrefs.FILE_NAME + XML_EXTENSION

    private val databaseSharedPreferencesName: String
        get() = DatabasePrefs.FILE_NAME + XML_EXTENSION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun setupContentView() {
        setContentView(R.layout.activity_backup)
    }

    private fun init() {
        backupPrefs = BackupPrefs.with(this)
        databasePrefs = DatabasePrefs.with(this)

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
        val backupCompletedSuccessfully = BackupHelper.backupDatabase(this, backupPrefs.backupPath)
        val resultMessage = getBackupResultMessage(backupCompletedSuccessfully)
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onRestoreDatabaseClick() {
        if (!ExternalStorageHelper.isExternalStorageReadable) {
            showReadExternalStoragePermissionIsMissingMsg()
            return
        }
        val restoreCompletedSuccessfully =
            BackupHelper.restoreDatabase(this, backupPrefs.backupPath)
        val resultMessage = getRestoreResultMessage(restoreCompletedSuccessfully)
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onBackupSharedPrefsClick() {
        if (!ExternalStorageHelper.isExternalStorageWritable) {
            showWriteExternalStoragePermissionIsMissingMsg()
            return
        }
        var backupCompletedSuccessfully = BackupHelper.backupSharedPreferences(
            this,
            backupSharedPreferencesName,
            backupPrefs.backupPath
        )
        backupCompletedSuccessfully =
            backupCompletedSuccessfully or BackupHelper.backupSharedPreferences(
                this,
                databaseSharedPreferencesName,
                backupPrefs.backupPath
            )
        backupCompletedSuccessfully =
            backupCompletedSuccessfully or BackupHelper.backupSharedPreferences(
                this,
                getDefaultSharedPreferencesName(this),
                backupPrefs.backupPath
            )
        val resultMessage = getBackupResultMessage(backupCompletedSuccessfully)
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onRestoreSharedPrefsClick() {
        if (!ExternalStorageHelper.isExternalStorageReadable) {
            showReadExternalStoragePermissionIsMissingMsg()
            return
        }
        var restoreCompletedSuccessfully = BackupHelper.restoreSharedPreferences(
            this,
            backupSharedPreferencesName,
            backupPrefs.backupPath
        )
        restoreCompletedSuccessfully =
            restoreCompletedSuccessfully or BackupHelper.restoreSharedPreferences(
                this,
                databaseSharedPreferencesName,
                backupPrefs.backupPath
            )
        restoreCompletedSuccessfully =
            restoreCompletedSuccessfully or BackupHelper.restoreSharedPreferences(
                this,
                getDefaultSharedPreferencesName(this),
                backupPrefs.backupPath
            )
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

    companion object {
        private const val XML_EXTENSION = ".xml"

        private fun getDefaultSharedPreferencesName(context: Context): String {
            return "${context.packageName}_preferences$XML_EXTENSION"
        }
    }
}
