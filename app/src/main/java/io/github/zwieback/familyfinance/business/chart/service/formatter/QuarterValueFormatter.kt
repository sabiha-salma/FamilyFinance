package io.github.zwieback.familyfinance.business.chart.service.formatter

import io.github.zwieback.familyfinance.constant.DateConstants
import io.github.zwieback.familyfinance.extension.QuartersFromEpoch
import org.threeten.bp.format.DateTimeFormatter

class QuarterValueFormatter : SpecificPeriodFormatter() {

    override fun getFormatter(): DateTimeFormatter = DateConstants.ISO_LOCAL_QUARTER

    override fun getSpecificPeriod(value: Long) = QuartersFromEpoch(value)
}
