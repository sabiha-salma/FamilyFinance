package io.github.zwieback.familyfinance.business.chart.service.grouper.bar

import io.github.zwieback.familyfinance.core.model.OperationView
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalUnit

class OperationGrouperByYear : BarOperationGrouper() {

    override val temporalUnit: TemporalUnit
        get() = ChronoUnit.YEARS

    /**
     * Result.Key - year.
     *
     * Result.Value - operations in that year.
     *
     * @param operations source operations
     * @return operations grouped by year
     */
    override fun group(
        operations: List<OperationView>,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<Float, List<OperationView>> {
        val result = linkedMapOf<Float, List<OperationView>>()
        val yearsBetween = calculatePeriodBetween(startDate, endDate)
        for (i in 0 until yearsBetween) {
            val currentYear = startDate.plusYears(i).year
            val yearlyOperations = filterByYear(currentYear, operations)
            result[currentYear.toFloat()] = yearlyOperations
        }
        return result
    }

    private fun filterByYear(year: Int, operations: List<OperationView>): List<OperationView> {
        return operations.filter { operation -> year == operation.date.year }
    }
}
