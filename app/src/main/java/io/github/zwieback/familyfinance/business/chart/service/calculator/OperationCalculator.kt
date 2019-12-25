package io.github.zwieback.familyfinance.business.chart.service.calculator

import android.content.Context
import io.github.zwieback.familyfinance.core.model.OperationView
import io.github.zwieback.familyfinance.core.preference.config.DatabasePrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal

class OperationCalculator(context: Context) {

    private val databasePrefs: DatabasePrefs = runBlocking(Dispatchers.IO) {
        DatabasePrefs.with(context)
    }

    fun calculateSum(operations: List<OperationView>): BigDecimal {
        val nativeCurrencyId = runBlocking(Dispatchers.IO) {
            databasePrefs.currencyId
        }
        val nativeSum = calculateSumInNativeCurrency(nativeCurrencyId, operations)
        val foreignSum = calculateSumInForeignCurrency(nativeCurrencyId, operations)
        return nativeSum.add(foreignSum)
    }

    private fun calculateSumInNativeCurrency(
        nativeCurrencyId: Int,
        operations: List<OperationView>
    ): BigDecimal {
        return operations
            .asSequence()
            .filter { operation -> nativeCurrencyId == operation.currencyId }
            .map { operation -> operation.value }
            .fold(BigDecimal.ZERO, { result, value -> result.add(value) })
    }

    private fun calculateSumInForeignCurrency(
        nativeCurrencyId: Int,
        operations: List<OperationView>
    ): BigDecimal {
        return operations
            .asSequence()
            .filter { operation -> nativeCurrencyId != operation.currencyId }
            .map { operation -> operation.value.multiply(operation.exchangeRateValue) }
            .fold(BigDecimal.ZERO, { result, value -> result.add(value) })
    }
}
