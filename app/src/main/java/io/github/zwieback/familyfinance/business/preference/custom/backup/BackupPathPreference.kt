package io.github.zwieback.familyfinance.business.preference.custom.backup

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import androidx.preference.R
import com.nononsenseapps.filepicker.FilePickerActivity
import com.nononsenseapps.filepicker.Utils
import io.github.zwieback.familyfinance.business.dashboard.activity.DashboardActivity.Companion.BACKUP_PATH_CODE
import io.github.zwieback.familyfinance.core.preference.custom.ActivityResultPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BackupPathPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        R.attr.preferenceStyle,
        android.R.attr.preferenceStyle
    ),
    defStyleRes: Int = 0
) : ActivityResultPreference(context, attrs, defStyleAttr, defStyleRes) {

    override val requestCode: Int
        get() = BACKUP_PATH_CODE

    override val requestIntent: Intent
        get() = Intent(context, FilePickerActivity::class.java)
            .putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true)
            .putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR)
            .putExtra(
                FilePickerActivity.EXTRA_START_PATH,
                Environment.getExternalStorageDirectory().path
            )

    override fun onAttached() {
        super.onAttached()
        launch {
            val backupPath = withContext(Dispatchers.IO) {
                backupPrefs.backupPath
            }
            callChangeListener(backupPath)
        }
    }

    override fun onSuccessResult(resultIntent: Intent) {
        val files = Utils.getSelectedFilesFromResult(resultIntent)
        if (files.isNotEmpty()) {
            val file = Utils.getFileForUri(files.iterator().next())
            launch(Dispatchers.IO) {
                backupPrefs.backupPath = file.absolutePath
            }
            callChangeListener(file.absolutePath)
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (newValue is String) {
            summary = newValue
            return true
        }
        return false
    }
}
