package io.github.zwieback.familyfinance.core.preference.config

import android.content.Context
import com.afollestad.rxkprefs.Pref
import com.afollestad.rxkprefs.RxkPrefs
import com.afollestad.rxkprefs.rxkPrefs

class BackupPrefs private constructor(context: Context) {

    private val prefs: RxkPrefs = rxkPrefs(context, FILE_NAME)

    private val backupPathPref: Pref<String>
        get() = prefs.string(BACKUP_PATH)

    var backupPath
        get() = backupPathPref.get()
        set(value) = backupPathPref.set(value)

    companion object {
        const val FILE_NAME = "backup_prefs"
        // region preferences
        private const val BACKUP_PATH = "backupPath"
        // endregion preferences

        fun with(context: Context) = BackupPrefs(context)
    }
}
