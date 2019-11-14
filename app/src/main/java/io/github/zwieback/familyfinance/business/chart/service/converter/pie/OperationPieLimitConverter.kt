package io.github.zwieback.familyfinance.business.chart.service.converter.pie

import android.content.Context
import com.github.mikephil.charting.data.PieEntry
import io.github.zwieback.familyfinance.R
import io.github.zwieback.familyfinance.business.chart.display.type.PieChartGroupByType
import io.github.zwieback.familyfinance.core.model.OperationView
import java.math.BigDecimal
import java.math.RoundingMode

class OperationPieLimitConverter(context: Context, groupByType: PieChartGroupByType) :
    OperationPieSimpleConverter(context, groupByType) {

    private val otherGroupName: String = context.getString(R.string.pie_group_other)
    private val minPercent: Int = context.resources.getInteger(R.integer.pie_min_percent)

    override fun convertToEntries(operations: Map<Float, List<OperationView>>): List<PieEntry> {
        val sumMap = convertToSumMap(operations)
        val reorderedSumMap = reorderSumMap(sumMap)
        return reorderedSumMap
            .asSequence()
            .map { operationEntry -> convertToPieEntry(operationEntry) }
            .sortedBy { pieEntry -> pieEntry.value }
            .toList()
    }

    /**
     * Reorder the card as follows: those sums that are less than the minimum
     * percentage will be placed in a separate group, the rest will not be
     * changed.
     *
     * @param sumMap source map calculated in the [convertToSumMap]
     * @return reordered map
     */
    private fun reorderSumMap(sumMap: Map<String, BigDecimal>): Map<String, BigDecimal> {
        val totalSum = calculateTotalSum(sumMap)
        val minSum = calculateMinSum(totalSum)
        val otherSum = sumMap
            .asSequence()
            .filter { entry -> entry.value <= minSum }
            .map { entry -> entry.value }
            .fold(BigDecimal.ZERO, { result, value -> result.add(value) })

        if (otherSum == BigDecimal.ZERO) {
            return sumMap
        }

        val reorderedMap = sumMap
            .filter { entry -> entry.value > minSum }
            .toMutableMap()
        reorderedMap[otherGroupName] = otherSum
        return reorderedMap
    }

    private fun calculateMinSum(totalSum: BigDecimal): BigDecimal {
        val minPercentDecimal = BigDecimal.valueOf(minPercent.toLong())
        return totalSum.multiply(minPercentDecimal).divide(MAX_PERCENT, RoundingMode.DOWN)
    }

    private fun calculateTotalSum(sumMap: Map<String, BigDecimal>): BigDecimal {
        return sumMap
            .asSequence()
            .map { it.value }
            .fold(BigDecimal.ZERO, { result, value -> result.add(value) })
    }

    companion object {
        private val MAX_PERCENT = BigDecimal.valueOf(100)
    }
}
