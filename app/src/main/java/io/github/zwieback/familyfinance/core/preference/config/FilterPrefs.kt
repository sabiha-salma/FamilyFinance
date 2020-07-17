package io.github.zwieback.familyfinance.core.preference.config

import android.content.Context
import com.afollestad.rxkprefs.Pref
import com.afollestad.rxkprefs.RxkPrefs
import com.afollestad.rxkprefs.rxkPrefs

class FilterPrefs private constructor(context: Context) {

    private val prefs: RxkPrefs = rxkPrefs(context, FILE_NAME)

    private val includeLastDaysOfPreviousMonthOnStartDatePref: Pref<Boolean>
        get() = prefs.boolean(
            INCLUDE_LAST_DAYS_OF_PREVIOUS_MONTH_ON_START_DATE,
            INCLUDE_LAST_DAYS_OF_PREVIOUS_MONTH_ON_START_DATE_DEFAULT
        )

    private val numberOfIncludedLastDaysPref: Pref<Int>
        get() = prefs.integer(
            NUMBER_OF_INCLUDED_LAST_DAYS,
            NUMBER_OF_INCLUDED_LAST_DAYS_DEFAULT
        )

    var includeLastDaysOfPreviousMonthOnStartDate
        get() = includeLastDaysOfPreviousMonthOnStartDatePref.get()
        set(value) = includeLastDaysOfPreviousMonthOnStartDatePref.set(value)

    var numberOfIncludedLastDays
        get() = numberOfIncludedLastDaysPref.get()
        set(value) = numberOfIncludedLastDaysPref.set(value)

    companion object {
        const val FILE_NAME = "filter_prefs"

        // region preferences
        private const val INCLUDE_LAST_DAYS_OF_PREVIOUS_MONTH_ON_START_DATE =
            "includeLastDaysOfPreviousMonthOnStartDate"
        private const val INCLUDE_LAST_DAYS_OF_PREVIOUS_MONTH_ON_START_DATE_DEFAULT = false

        private const val NUMBER_OF_INCLUDED_LAST_DAYS = "numberOfIncludedLastDays"
        private const val NUMBER_OF_INCLUDED_LAST_DAYS_DEFAULT = 3
        // endregion preferences

        fun with(context: Context) = FilterPrefs(context)
    }
}
