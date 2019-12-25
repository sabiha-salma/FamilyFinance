package io.github.zwieback.familyfinance.business.preference.activity

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.preference.fragment.SettingsFragment
import io.github.zwieback.familyfinance.core.activity.DataActivityWrapper
import io.github.zwieback.familyfinance.core.model.IBaseEntity
import io.reactivex.functions.Consumer

class SettingsActivity :
    DataActivityWrapper(),
    PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

    override val titleStringId: Int
        get() = R.string.settings_activity_title

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initFragment()
        }
    }

    override fun setupContentView() {
        setContentView(R.layout.activity_settings)
    }

    override fun onPreferenceStartScreen(
        preferenceFragmentCompat: PreferenceFragmentCompat,
        preferenceScreen: PreferenceScreen
    ): Boolean {
        val fragment = createFragment().apply {
            arguments = Bundle().apply {
                putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.key)
            }
        }

        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.settings_fragment, fragment, preferenceScreen.key)
            .addToBackStack(preferenceScreen.key)
            .commit()

        return true
    }

    private fun initFragment() {
        val fragment = createFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_fragment, fragment)
            .commit()
    }

    private fun createFragment(): PreferenceFragmentCompat {
        return SettingsFragment()
    }

    public override fun <E : IBaseEntity> loadEntity(
        entityClass: Class<E>,
        entityId: Int,
        onSuccess: Consumer<E>,
        onError: Consumer<in Throwable>
    ) {
        super.loadEntity(entityClass, entityId, onSuccess, onError)
    }
}
