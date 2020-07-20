package io.github.zwieback.familyfinance.business.chart.service.grouper.bar

import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.extension.toMonthsFromEpoch
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalUnit

class OperationGrouperByMonth : BarOperationGrouper() {

    override val temporalUnit: TemporalUnit
        get() = ChronoUnit.MONTHS

    /**
     * Result.Key - days from epoch day.
     *
     * Result.Value - operations in that month.
     *
     * @param operations source operations
     * @return operations grouped by month
     */
    override fun group(
        operations: List<OperationView>,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<Float, List<OperationView>> {
        val result = linkedMapOf<Float, List<OperationView>>()
        val monthsBetween = calculatePeriodBetween(startDate, endDate)
        for (i in 0 until monthsBetween) {
            val currentMonth = startDate.plusMonths(i)
            val month = currentMonth.monthValue
            val year = currentMonth.year
            val monthsFromEpoch = currentMonth.toMonthsFromEpoch().toFloat()
            val monthlyOperations = filterByMonth(year, month, operations)
            result[monthsFromEpoch] = monthlyOperations
        }
        return result
    }

    private fun filterByMonth(
        year: Int,
        month: Int,
        operations: List<OperationView>
    ): List<OperationView> {
        return operations.filter { operation -> operationInMonth(year, month, operation) }
    }

    private fun operationInMonth(year: Int, month: Int, operation: OperationView): Boolean {
        val operationDate = operation.date
        return year == operationDate.year && month == operationDate.monthValue
    }
}
