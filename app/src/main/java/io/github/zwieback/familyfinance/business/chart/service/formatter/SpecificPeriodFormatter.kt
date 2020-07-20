package io.github.zwieback.familyfinance.business.chart.service.formatter

import com.github.mikephil.charting.formatter.ValueFormatter
import io.github.zwieback.familyfinance.extension.SpecificPeriodFromEpoch
import org.threeten.bp.format.DateTimeFormatter

abstract class SpecificPeriodFormatter : ValueFormatter() {

    abstract fun getFormatter(): DateTimeFormatter

    abstract fun getSpecificPeriod(value: Long): SpecificPeriodFromEpoch

    override fun getFormattedValue(value: Float): String {
        val specificPeriod = getSpecificPeriod(value.toLong())
        val formattedDate = specificPeriod.toLocalDate()
        return formattedDate.format(getFormatter())
    }
}
