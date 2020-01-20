package io.github.zwieback.familyfinance.business.preference.custom.filter

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.core.preference.config.FilterPrefs
import io.github.zwieback.familyfinance.core.preference.custom.BooleanPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class IncludeLastDaysOfPreviousMonthOnStartDatePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        R.attr.checkBoxPreferenceStyle,
        android.R.attr.checkBoxPreferenceStyle
    ),
    defStyleRes: Int = 0
) : BooleanPreference(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var filterPrefs: FilterPrefs

    override fun onAttached() {
        super.onAttached()
        runBlocking(Dispatchers.IO) {
            filterPrefs = FilterPrefs.with(context)
        }
        launch {
            val includeLastDays = withContext(Dispatchers.IO) {
                filterPrefs.includeLastDaysOfPreviousMonthOnStartDate
            }
            callChangeListener(includeLastDays)
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (newValue is Boolean) {
            launch(Dispatchers.IO) {
                filterPrefs.includeLastDaysOfPreviousMonthOnStartDate = newValue
            }
            return true
        }
        return false
    }
}
