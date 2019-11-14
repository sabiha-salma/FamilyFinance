package io.github.zwieback.familyfinance.business.chart.service.converter

import android.content.Context
import io.github.zwieback.familyfinance.business.chart.service.calculator.OperationCalculator
import io.github.zwieback.familyfinance.core.model.OperationView
import java.math.BigDecimal

class OperationSumConverter(context: Context) {

    private val calculator: OperationCalculator = OperationCalculator(context)

    /**
     * Calculate and return map with sum of operations.
     *
     * @param operations source operations
     * @return key - same key as input key, value - sum of operations
     */
    fun convertToSumMap(operations: Map<Float, List<OperationView>>): Map<Float, BigDecimal> {
        return operations.mapValues { entry -> calculator.calculateSum(entry.value) }
    }
}
