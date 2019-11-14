package io.github.zwieback.familyfinance.business.chart.service.formatter

import com.github.mikephil.charting.formatter.ValueFormatter
import io.github.zwieback.familyfinance.util.DateUtils
import org.threeten.bp.LocalDate

class MonthValueFormatter : ValueFormatter() {

    override fun getFormattedValue(monthsFromEpoch: Float): String {
        val month = determineCorrectMonth(monthsFromEpoch)
        return DateUtils.localDateToString(month, DateUtils.ISO_LOCAL_MONTH)
    }

    private fun determineCorrectMonth(monthsFromEpoch: Float): LocalDate {
        return DateUtils.epochMonthToLocalDate(monthsFromEpoch.toLong())
    }
}
