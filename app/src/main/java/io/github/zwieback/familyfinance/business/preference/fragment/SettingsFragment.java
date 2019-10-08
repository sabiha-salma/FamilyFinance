package io.github.zwieback.familyfinance.business.preference.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.takisoft.preferencex.PreferenceFragmentCompat;

import io.github.zwieback.familyfinance.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String INTERFACE_PREFERENCES_NAME = "interface_prefs";
    private static final String DATABASE_PREFERENCES_NAME = "database_prefs";
    private static final String BACKUP_PREFERENCES_NAME = "backup_prefs";
    private static final String ACRA_PREFERENCES_NAME = "acra_prefs";

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setupSharedPreferences(rootKey);
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);
        setupPreferenceScreenIcons(rootKey);
    }

    private void setupSharedPreferences(@Nullable String rootKey) {
        if (DATABASE_PREFERENCES_NAME.equals(rootKey)) {
            getPreferenceManager().setSharedPreferencesName(DATABASE_PREFERENCES_NAME);
        }
    }

    private void setupPreferenceScreenIcons(@Nullable String rootKey) {
        if (rootKey == null) {
            setPreferenceScreenIcon(INTERFACE_PREFERENCES_NAME, CommunityMaterial.Icon.cmd_eye);
            setPreferenceScreenIcon(DATABASE_PREFERENCES_NAME, FontAwesome.Icon.faw_database);
            setPreferenceScreenIcon(BACKUP_PREFERENCES_NAME, CommunityMaterial.Icon2.cmd_sync);
            setPreferenceScreenIcon(ACRA_PREFERENCES_NAME, FontAwesome.Icon.faw_bug);
        }
    }

    private void setPreferenceScreenIcon(@NonNull String preferenceScreenKey,
                                         @NonNull IIcon icon) {
        Preference preference = findPreference(preferenceScreenKey);
        if (preference != null) {
            IconicsDrawable drawable = new IconicsDrawable(preference.getContext())
                    .icon(icon)
                    .size(IconicsSize.res(R.dimen.preference_icon_size));
            preference.setIcon(drawable);
        }
    }
}
