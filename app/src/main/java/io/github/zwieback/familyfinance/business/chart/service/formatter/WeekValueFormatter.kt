package io.github.zwieback.familyfinance.business.chart.service.formatter

import com.github.mikephil.charting.formatter.ValueFormatter
import io.github.zwieback.familyfinance.constant.DateConstants
import io.github.zwieback.familyfinance.util.DateUtils
import org.threeten.bp.LocalDate

class WeekValueFormatter : ValueFormatter() {

    override fun getFormattedValue(weeksFromEpoch: Float): String {
        val month = determineCorrectWeek(weeksFromEpoch)
        return DateUtils.localDateToString(month, DateConstants.ISO_LOCAL_WEEK)
    }

    private fun determineCorrectWeek(weeksFromEpoch: Float): LocalDate {
        return DateUtils.epochWeekToLocalDate(weeksFromEpoch.toLong())
    }
}
