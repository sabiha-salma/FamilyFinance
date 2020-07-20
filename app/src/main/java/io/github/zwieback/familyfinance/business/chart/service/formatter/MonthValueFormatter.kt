package io.github.zwieback.familyfinance.business.chart.service.formatter

import io.github.zwieback.familyfinance.constant.DateConstants
import io.github.zwieback.familyfinance.extension.MonthsFromEpoch
import org.threeten.bp.format.DateTimeFormatter

class MonthValueFormatter : SpecificPeriodFormatter() {

    override fun getFormatter(): DateTimeFormatter = DateConstants.ISO_LOCAL_MONTH

    override fun getSpecificPeriod(value: Long) = MonthsFromEpoch(value)
}
