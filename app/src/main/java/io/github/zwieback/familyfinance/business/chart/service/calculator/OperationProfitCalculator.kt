package io.github.zwieback.familyfinance.business.chart.service.calculator

import android.content.Context
import io.github.zwieback.familyfinance.core.model.OperationView
import java.math.BigDecimal

class OperationProfitCalculator(context: Context) {

    private val calculator: OperationCalculator = OperationCalculator(context)

    /**
     * Calculate and return map with profit sum (income - expenses).
     *
     * @return key - period, value - profit sum of operations
     */
    fun calculateProfitMap(
        incomeOperationMap: Map<Float, List<OperationView>>,
        expenseOperationMap: Map<Float, List<OperationView>>
    ): Map<Float, BigDecimal> {
        return incomeOperationMap.mapValues { entry ->
            val period = entry.key
            val incomeOperations = entry.value
            val expenseOperations = findExpenseOperationsForSamePeriod(period, expenseOperationMap)
            val incomeSum = calculator.calculateSum(incomeOperations)
            val expenseSum = calculator.calculateSum(expenseOperations)
            incomeSum.subtract(expenseSum)
        }
    }

    private fun findExpenseOperationsForSamePeriod(
        period: Float,
        expenseOperations: Map<Float, List<OperationView>>
    ): List<OperationView> {
        return expenseOperations[period] ?: error("No operations found for period $period")
    }
}
