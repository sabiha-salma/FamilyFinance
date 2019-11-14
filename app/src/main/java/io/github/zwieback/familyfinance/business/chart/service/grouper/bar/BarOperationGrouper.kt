package io.github.zwieback.familyfinance.business.chart.service.grouper.bar

import io.github.zwieback.familyfinance.business.chart.service.grouper.OperationGrouper
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.TemporalUnit

abstract class BarOperationGrouper : OperationGrouper {

    protected abstract val temporalUnit: TemporalUnit

    protected fun calculatePeriodBetween(startDate: LocalDate, endDate: LocalDate): Long {
        return temporalUnit.between(startDate, endDate) + INCLUDE_END_DATE
    }

    companion object {
        private const val INCLUDE_END_DATE = 1
    }
}
