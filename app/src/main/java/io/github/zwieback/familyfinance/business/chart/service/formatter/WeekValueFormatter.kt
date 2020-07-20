package io.github.zwieback.familyfinance.business.chart.service.formatter

import io.github.zwieback.familyfinance.constant.DateConstants
import io.github.zwieback.familyfinance.extension.WeeksFromEpoch
import org.threeten.bp.format.DateTimeFormatter

class WeekValueFormatter : SpecificPeriodFormatter() {

    override fun getFormatter(): DateTimeFormatter = DateConstants.ISO_LOCAL_WEEK

    override fun getSpecificPeriod(value: Long) = WeeksFromEpoch(value)
}
