package io.github.zwieback.familyfinance.business.chart.service.grouper.bar

import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.util.DateUtils
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalUnit

class OperationGrouperByDay : BarOperationGrouper() {

    override val temporalUnit: TemporalUnit
        get() = ChronoUnit.DAYS

    /**
     * Result.Key - days from epoch day.
     *
     * Result.Value - operations is that day.
     *
     * @param operations source operations
     * @return operations grouped by day
     */
    override fun group(
        operations: List<OperationView>,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<Float, List<OperationView>> {
        val result = linkedMapOf<Float, List<OperationView>>()
        val daysBetween = calculatePeriodBetween(startDate, endDate)
        for (i in 0 until daysBetween) {
            val currentDay = startDate.plusDays(i)
            val daysFromEpoch = DateUtils.localDateToEpochDay(currentDay).toFloat()
            val dailyOperations = filterByDay(currentDay, operations)
            result[daysFromEpoch] = dailyOperations
        }
        return result
    }

    private fun filterByDay(date: LocalDate, operations: List<OperationView>): List<OperationView> {
        return operations.filter { operation -> date == operation.date }
    }
}
