package io.github.zwieback.familyfinance.business.chart.service.grouper.bar

import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.extension.getWeekOfYear
import io.github.zwieback.familyfinance.extension.toWeeksFromEpoch
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalUnit

class OperationGrouperByWeek : BarOperationGrouper() {

    override val temporalUnit: TemporalUnit
        get() = ChronoUnit.WEEKS

    /**
     * Result.Key - weeks from epoch day.
     *
     * Result.Value - operations in that week.
     *
     * @param operations source operations
     * @return operations grouped by week
     */
    override fun group(
        operations: List<OperationView>,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<Float, List<OperationView>> {
        val result = linkedMapOf<Float, List<OperationView>>()
        val weeksBetween = calculatePeriodBetween(startDate, endDate)
        for (i in 0 until weeksBetween) {
            val currentWeek = startDate.plusWeeks(i)
            val week = currentWeek.getWeekOfYear()
            val year = currentWeek.year
            val weeksFromEpoch = currentWeek.toWeeksFromEpoch().toFloat()
            val weeklyOperations = filterByWeek(year, week, operations)
            result[weeksFromEpoch] = weeklyOperations
        }
        return result
    }

    private fun filterByWeek(
        year: Int,
        week: Int,
        operations: List<OperationView>
    ): List<OperationView> {
        return operations.filter { operation -> operationInWeek(year, week, operation) }
    }

    private fun operationInWeek(year: Int, week: Int, operation: OperationView): Boolean {
        val operationDate = operation.date
        val operationWeek = operationDate.getWeekOfYear()
        return year == operationDate.year && week == operationWeek
    }
}
