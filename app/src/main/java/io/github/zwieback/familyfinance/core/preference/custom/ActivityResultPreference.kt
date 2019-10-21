package io.github.zwieback.familyfinance.core.preference.custom

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import androidx.preference.R

import com.takisoft.preferencex.PreferenceActivityResultListener
import com.takisoft.preferencex.PreferenceFragmentCompat

import io.github.zwieback.familyfinance.business.preference.activity.SettingsActivity
import io.github.zwieback.familyfinance.core.preference.config.BackupPrefs
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs

abstract class ActivityResultPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        R.attr.preferenceStyle,
        android.R.attr.preferenceStyle
    ),
    defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes),
    PreferenceActivityResultListener,
    OnSuccessPreferenceActivityResultListener,
    Preference.OnPreferenceChangeListener {

    protected lateinit var activity: SettingsActivity
    protected lateinit var databasePrefs: DatabasePrefs
    protected lateinit var backupPrefs: BackupPrefs

    protected abstract val requestCode: Int

    protected abstract val requestIntent: Intent

    init {
        init(context)
    }

    protected open fun init(context: Context) {
        activity = determineActivity(context)
        databasePrefs = DatabasePrefs.with(activity)
        backupPrefs = BackupPrefs.with(activity)
        onPreferenceChangeListener = this
    }

    private fun determineActivity(context: Context): SettingsActivity {
        if (context is SettingsActivity) {
            return context
        }
        if (context is ContextWrapper) {
            if (context.baseContext is SettingsActivity) {
                return context.baseContext as SettingsActivity
            }
        }
        throw ClassCastException("$context must implement SettingsActivity")
    }

    override fun onPreferenceClick(fragment: PreferenceFragmentCompat, preference: Preference) {
        fragment.startActivityForResult(requestIntent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultIntent: Intent?) {
        if (requestCode == this.requestCode
            && resultCode == Activity.RESULT_OK
            && resultIntent != null
        ) {
            onSuccessResult(resultIntent)
        }
    }
}
