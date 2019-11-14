package io.github.zwieback.familyfinance.business.chart.service.converter.bar

import android.content.Context
import android.util.Pair
import com.github.mikephil.charting.data.BarEntry
import io.github.zwieback.familyfinance.business.chart.service.builder.IdIndexMapStatefulBuilder
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.util.CollectionUtils
import java.math.BigDecimal
import java.math.RoundingMode

class OperationHorizontalBarPercentConverter(
    context: Context,
    builder: IdIndexMapStatefulBuilder
) : OperationHorizontalBarConverter(context, builder) {

    /**
     * Result.X - bar index.
     *
     * Result.Y - sum of operations in percent.
     *
     * @param operations key - entity id, value - list of operations
     * @return list of entries to display in horizontal bar chart
     */
    override fun convertToEntries(operations: Map<Float, List<OperationView>>): List<BarEntry> {
        val sumMap = sumConverter.convertToSumMap(operations)
        val swappedSumMap = CollectionUtils.swapMap(sumMap)
        val percentSumMap = convertToPercentMap(swappedSumMap)
        val idIndexMap = builder.withSumMap(percentSumMap).build()

        return percentSumMap
            .asSequence()
            .map { entry ->
                val barIndex = idIndexMap[entry.second]
                    ?: error("BarIndex is null on ${entry.second}")
                val sumOfOperationsInPercent = entry.first.toFloat()
                BarEntry(barIndex, sumOfOperationsInPercent)
            }
            .sortedBy { barEntry -> barEntry.x }
            .toList()
    }

    private fun convertToPercentMap(
        swappedSumMap: List<Pair<BigDecimal, Float>>
    ): List<Pair<BigDecimal, Float>> {
        val sums = swappedSumMap.map { pair -> pair.first }
        val totalSum = calculateTotalSum(sums)
        return swappedSumMap
            .map { pair -> Pair.create(calculatePercent(totalSum, pair.first), pair.second) }
    }

    private fun calculatePercent(totalSum: BigDecimal, sumOfOperations: BigDecimal): BigDecimal {
        return sumOfOperations.multiply(MAX_PERCENT).divide(totalSum, RoundingMode.DOWN)
    }

    private fun calculateTotalSum(collectionWithSums: Collection<BigDecimal>): BigDecimal {
        return collectionWithSums.fold(BigDecimal.ZERO, { result, value -> result.add(value) })
    }

    companion object {
        private val MAX_PERCENT = BigDecimal.valueOf(100)
    }
}
