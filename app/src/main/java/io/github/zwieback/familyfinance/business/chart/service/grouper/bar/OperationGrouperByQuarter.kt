package io.github.zwieback.familyfinance.business.chart.service.grouper.bar

import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.extension.getQuarterOfYear
import io.github.zwieback.familyfinance.extension.plusQuarters
import io.github.zwieback.familyfinance.extension.toQuartersFromEpoch
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.IsoFields
import org.threeten.bp.temporal.TemporalUnit

class OperationGrouperByQuarter : BarOperationGrouper() {

    override val temporalUnit: TemporalUnit
        get() = IsoFields.QUARTER_YEARS

    /**
     * Result.Key - quarters from epoch day.
     *
     * Result.Value - operations in that quarter.
     *
     * @param operations source operations
     * @return operations grouped by quarter
     */
    override fun group(
        operations: List<OperationView>,
        startDate: LocalDate,
        endDate: LocalDate
    ): Map<Float, List<OperationView>> {
        val result = linkedMapOf<Float, List<OperationView>>()
        val quartersBetween = calculatePeriodBetween(startDate, endDate)
        for (i in 0 until quartersBetween) {
            val currentQuarter = startDate.plusQuarters(i)
            val quarter = currentQuarter.getQuarterOfYear()
            val year = currentQuarter.year
            val quartersFromEpoch = currentQuarter.toQuartersFromEpoch().toFloat()
            val quarterlyOperations = filterByQuarter(year, quarter, operations)
            result[quartersFromEpoch] = quarterlyOperations
        }
        return result
    }

    private fun filterByQuarter(
        year: Int,
        quarter: Int,
        operations: List<OperationView>
    ): List<OperationView> {
        return operations.filter { operation -> operationInQuarter(year, quarter, operation) }
    }

    private fun operationInQuarter(year: Int, quarter: Int, operation: OperationView): Boolean {
        val operationDate = operation.date
        val operationQuarter = operationDate.getQuarterOfYear()
        return year == operationDate.year && quarter == operationQuarter
    }
}
