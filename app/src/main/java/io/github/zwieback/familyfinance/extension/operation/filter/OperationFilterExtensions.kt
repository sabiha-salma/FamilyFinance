package io.github.zwieback.familyfinance.extension.operation.filter

import android.content.Context
import io.github.zwieback.familyfinance.business.operation.filter.OperationFilter
import io.github.zwieback.familyfinance.core.preference.config.FilterPrefs
import io.github.zwieback.familyfinance.extension.startOfMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate

fun OperationFilter.applyPreferences(context: Context) {
    startDate = LocalDate.now().startOfMonth()
    runBlocking(Dispatchers.IO) {
        val filterPrefs = FilterPrefs.with(context)
        if (filterPrefs.includeLastDaysOfPreviousMonthOnStartDate) {
            val numberOfIncludedLastDays = filterPrefs.numberOfIncludedLastDays
            val currentDate = LocalDate.now()
            if (currentDate.dayOfMonth <= numberOfIncludedLastDays) {
                startDate = startDate.minusDays(numberOfIncludedLastDays.toLong())
            }
        }
    }
}
