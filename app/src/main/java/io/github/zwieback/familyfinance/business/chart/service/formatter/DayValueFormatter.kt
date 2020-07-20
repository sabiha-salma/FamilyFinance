package io.github.zwieback.familyfinance.business.chart.service.formatter

import io.github.zwieback.familyfinance.extension.DaysFromEpoch
import org.threeten.bp.format.DateTimeFormatter

class DayValueFormatter : SpecificPeriodFormatter() {

    override fun getFormatter(): DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun getSpecificPeriod(value: Long) = DaysFromEpoch(value)
}
