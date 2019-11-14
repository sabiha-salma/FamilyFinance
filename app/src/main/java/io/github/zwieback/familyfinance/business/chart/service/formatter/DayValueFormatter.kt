package io.github.zwieback.familyfinance.business.chart.service.formatter

import com.github.mikephil.charting.formatter.ValueFormatter
import io.github.zwieback.familyfinance.util.DateUtils
import org.threeten.bp.LocalDate

class DayValueFormatter : ValueFormatter() {

    override fun getFormattedValue(daysFromEpoch: Float): String {
        val day = determineCorrectDay(daysFromEpoch)
        return DateUtils.localDateToString(day)
    }

    private fun determineCorrectDay(daysFromEpoch: Float): LocalDate {
        return DateUtils.epochDayToLocalDate(daysFromEpoch.toLong())
    }
}
