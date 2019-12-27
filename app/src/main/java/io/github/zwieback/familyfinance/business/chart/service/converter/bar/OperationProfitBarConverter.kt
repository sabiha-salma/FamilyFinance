package io.github.zwieback.familyfinance.business.chart.service.converter.bar

import com.github.mikephil.charting.data.BarEntry
import java.math.BigDecimal

class OperationProfitBarConverter {

    fun convertToEntries(profitOperations: Map<Float, BigDecimal>): List<BarEntry> {
        return profitOperations
            .asSequence()
            .map { operationEntry ->
                // period depends on the implementation of the BarOperationGrouper
                // it may be day, week, month, etc.
                val period = operationEntry.key
                val sum = operationEntry.value.toFloat()
                BarEntry(period, sum)
            }
            .sortedBy { barEntry -> barEntry.x }
            .toList()
    }
}
