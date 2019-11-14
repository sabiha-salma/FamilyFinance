package io.github.zwieback.familyfinance.business.chart.service.converter.bar

import android.content.Context
import com.github.mikephil.charting.data.BarEntry
import io.github.zwieback.familyfinance.business.chart.service.builder.IdIndexMapStatefulBuilder
import io.github.zwieback.familyfinance.business.chart.service.converter.OperationConverter
import io.github.zwieback.familyfinance.business.chart.service.converter.OperationSumConverter
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.util.CollectionUtils

open class OperationHorizontalBarConverter(
    context: Context,
    protected val builder: IdIndexMapStatefulBuilder
) : OperationConverter<BarEntry> {

    protected val sumConverter: OperationSumConverter = OperationSumConverter(context)

    /**
     * Result.X - bar index.
     *
     * Result.Y - sum of operations.
     *
     * @param operations key - entity id, value - list of operations
     * @return list of entries to display in horizontal bar chart
     */
    override fun convertToEntries(operations: Map<Float, List<OperationView>>): List<BarEntry> {
        val sumMap = sumConverter.convertToSumMap(operations)
        val swappedSumMap = CollectionUtils.swapMap(sumMap)
        val idIndexMap = builder.withSumMap(swappedSumMap).build()

        return swappedSumMap
            .asSequence()
            .map { entry ->
                val barIndex = idIndexMap[entry.second]
                    ?: error("BarIndex is null on ${entry.second}")
                val sumOfOperations = entry.first.toFloat()
                BarEntry(barIndex, sumOfOperations)
            }
            .sortedBy { barEntry -> barEntry.x }
            .toList()
    }
}
