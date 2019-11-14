package io.github.zwieback.familyfinance.business.chart.service.converter.bar

import android.content.Context
import com.github.mikephil.charting.data.BarEntry
import io.github.zwieback.familyfinance.business.chart.service.converter.OperationConverter
import io.github.zwieback.familyfinance.business.chart.service.converter.OperationSumConverter
import io.github.zwieback.familyfinance.core.model.OperationView

class OperationBarConverter(context: Context) : OperationConverter<BarEntry> {

    private val sumConverter: OperationSumConverter = OperationSumConverter(context)

    override fun convertToEntries(operations: Map<Float, List<OperationView>>): List<BarEntry> {
        val sumMap = sumConverter.convertToSumMap(operations)
        return sumMap
            .asSequence()
            .map { operationEntry ->
                // period depends on the implementation of the BarOperationGrouper
                // it may be day, week, month, etc.
                val periodOfOperations = operationEntry.key
                val sumOfOperations = operationEntry.value.toFloat()
                BarEntry(periodOfOperations, sumOfOperations)
            }
            .sortedBy { barEntry -> barEntry.x }
            .toList()
    }
}
