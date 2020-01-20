package io.github.zwieback.familyfinance.business.preference.custom.filter

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.preference.config.FilterPrefs
import io.github.zwieback.familyfinance.core.preference.custom.SeekBarPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class NumberOfIncludedLastDaysPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        R.attr.seekBarPreferenceStyle,
        android.R.attr.seekBarStyle
    ),
    defStyleRes: Int = 0
) : SeekBarPreference(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var filterPrefs: FilterPrefs

    override fun onAttached() {
        super.onAttached()
        runBlocking(Dispatchers.IO) {
            filterPrefs = FilterPrefs.with(context)
        }
        launch {
            val numberOfIncludedLastDays = withContext(Dispatchers.IO) {
                filterPrefs.numberOfIncludedLastDays
            }
            callChangeListener(numberOfIncludedLastDays)
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (newValue is Int) {
            launch(Dispatchers.IO) {
                filterPrefs.numberOfIncludedLastDays = newValue
            }
            return true
        }
        return false
    }
}
