package io.github.zwieback.familyfinance.business.preference.fragment

import android.os.Bundle
import androidx.preference.Preference
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.IconicsSize
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.takisoft.preferencex.PreferenceFragmentCompat
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.preference.config.BackupPrefs
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs
import io.github.zwieback.familyfinance.core.preference.config.FilterPrefs
import io.github.zwieback.familyfinance.core.preference.config.InterfacePrefs

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setupSharedPreferences(rootKey)
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        setupPreferenceScreenIcons(rootKey)
    }

    private fun setupSharedPreferences(rootKey: String?) {
        if (DATABASE_PREFERENCES_NAME == rootKey) {
            preferenceManager.sharedPreferencesName = DATABASE_PREFERENCES_NAME
        }
    }

    private fun setupPreferenceScreenIcons(rootKey: String?) {
        if (rootKey == null) {
            setPreferenceScreenIcon(INTERFACE_PREFERENCES_NAME, CommunityMaterial.Icon.cmd_eye)
            setPreferenceScreenIcon(DATABASE_PREFERENCES_NAME, FontAwesome.Icon.faw_database)
            setPreferenceScreenIcon(FILTER_PREFERENCES_NAME, FontAwesome.Icon.faw_filter)
            setPreferenceScreenIcon(BACKUP_PREFERENCES_NAME, CommunityMaterial.Icon2.cmd_sync)
            setPreferenceScreenIcon(ACRA_PREFERENCES_NAME, FontAwesome.Icon.faw_bug)
        }
    }

    private fun setPreferenceScreenIcon(
        preferenceScreenKey: String,
        icon: IIcon
    ) {
        findPreference<Preference>(preferenceScreenKey)?.let { preference ->
            preference.icon =
                IconicsDrawable(preference.context)
                    .icon(icon)
                    .size(IconicsSize.res(R.dimen.preference_icon_size))
        }
    }

    companion object {
        private const val INTERFACE_PREFERENCES_NAME = InterfacePrefs.FILE_NAME
        private const val DATABASE_PREFERENCES_NAME = DatabasePrefs.FILE_NAME
        private const val FILTER_PREFERENCES_NAME = FilterPrefs.FILE_NAME
        private const val BACKUP_PREFERENCES_NAME = BackupPrefs.FILE_NAME
        private const val ACRA_PREFERENCES_NAME = "acra_prefs"
    }
}
