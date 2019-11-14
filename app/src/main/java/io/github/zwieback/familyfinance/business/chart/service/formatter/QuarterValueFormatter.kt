package io.github.zwieback.familyfinance.business.chart.service.formatter

import com.github.mikephil.charting.formatter.ValueFormatter
import io.github.zwieback.familyfinance.util.DateUtils
import org.threeten.bp.LocalDate

class QuarterValueFormatter : ValueFormatter() {

    override fun getFormattedValue(quartersFromEpoch: Float): String {
        val quarter = determineCorrectQuarter(quartersFromEpoch)
        return DateUtils.localDateToString(quarter, DateUtils.ISO_LOCAL_QUARTER)
    }

    private fun determineCorrectQuarter(quartersFromEpoch: Float): LocalDate {
        return DateUtils.epochQuarterToLocalDate(quartersFromEpoch.toLong())
    }
}
